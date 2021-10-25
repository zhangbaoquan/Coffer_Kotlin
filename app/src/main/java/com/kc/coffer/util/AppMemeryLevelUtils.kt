package com.kc.coffer.util

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.Log
import org.w3c.dom.Text
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException
import java.lang.reflect.InvocationTargetException

/**
 * author      : coffer
 * date        : 10/15/21
 * description : 运行时内存情况工具
 */

const val TAG = "AppMemeryLevelUtils_tag"

/** 低内存运行模式  */
const val LOW_MEMERY = 1

/** 中内存运行模式  */
const val MIDDLE_MEMERY = 2

/** 高内存运行模式  */
const val HIGHT_MEMERY = 3

/** 最小系统可用内存，用来判断运行时内存情况  */
private const val MINIMUM_SYSTEM_AVAIALBE_MEMORY = 512

/** 最小应用可用内存，用来判断运行时内存情况  */
private const val MINIMUM_APP_AVAIALBE_MEMORY = 96

// 判断运行时内存情况是 应用可用内存/系统可用内存 最小比例
private const val MINIMUM_RATIO = 0.3f

/** 低于值表明内存不充足，系统剩余内存占总内存的比值 */
private const val MINIMUM_RATIO1 = 0.2f

/** 高于此值表明内存充足，系统剩余内存占总内存的比值 */
private const val MAXIMUM_RATIO1 = 0.25f

/**
 * 获取APP内存模式
 * @author huangyahong
 * @return {@link #LOW_MEMERY} ,{@link #MIDDLE_MEMERY} ,{@link #HIGHT_MEMERY}
 */
fun getAppMemeryLevel(context: Context): Int {
    try {
        var activityManager: ActivityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        //系统最大可用内存
        val avaialbeMemory = activityManager.memoryClass;
        activityManager.runningAppProcesses
        val avaialbeMemorySize = avaialbeMemory * 1024 * 1024
        // 获得MemoryInfo对象
        val memoryInfo = ActivityManager.MemoryInfo()
        // 获得系统可用内存，保存在MemoryInfo对象上
        activityManager.getMemoryInfo(memoryInfo)
        // 获得系统内存信息
        val systemAvaialbeMemorySize = memoryInfo.availMem
        // 应用可用最大内存/系统剩余内存
        val ratio = (avaialbeMemorySize.toFloat() / systemAvaialbeMemorySize.toFloat())
        //系统剩余内存/系统总内存
        var ratio1 = 1.0f
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ratio1 = systemAvaialbeMemorySize.toFloat() / memoryInfo.totalMem.toFloat()
        }
        // 是否是小屏幕
        var smallScreen = false
        val displayMetrics = context.resources.displayMetrics
        if (displayMetrics.widthPixels <= 800) {
            smallScreen = true
        }

//        Log.d(TAG, "totalMemory::" + Math.round((float)((float)getTotalMemory() / (float)(1024 << 20))) + "   1G::" + (1024 << 20) + "  memorySize::" + avaialbeMemory
//            + "  systemAvaialbeMemorySize::" + systemAvaialbeMemory + "  ratio::" + ratio);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || !smallScreen &&
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
            memoryInfo.totalMem / (1024 shl 10) >= 1800 &&
            ratio1 > MAXIMUM_RATIO1
        ) {
            // 分辨率大于720P并且大于2G总内存并且android 5.0以上
            return HIGHT_MEMERY
        } else if (memoryInfo.lowMemory ||
            ratio > MINIMUM_RATIO && ratio1 < MINIMUM_RATIO1 &&
            Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP
        ) {
            //小于android 5.0 系统或者剩余内存占比低于0.2并剩余内存不足以启动3个同样的应用
            return LOW_MEMERY
        }
        return MIDDLE_MEMERY

    } catch (e: Exception) {
        Log.e(TAG, "getAppMemeryLevel :: error", e)
    }
    return MIDDLE_MEMERY
}

fun isLowRamDevice():Boolean{
    try {
        val SystemProperties = Class.forName("android.os.SystemProperties")
        val method = SystemProperties.getMethod(
            "get", String::class.java, String::class.java
        )
        val value = method.invoke(null,"ro.config.low_ram","false") as String
        return TextUtils.equals("true",value)
    } catch (e: IllegalArgumentException) {
        Log.e(
            TAG,
            "isLowRamDevice ::IllegalArgumentException error",
            e
        )
    } catch (e: ClassNotFoundException) {
        Log.e(
            TAG,
            "isLowRamDevice ::ClassNotFoundException error",
            e
        )
    } catch (e: NoSuchMethodException) {
        Log.e(
            TAG,
            "isLowRamDevice ::NoSuchMethodException error",
            e
        )
    } catch (e: IllegalAccessException) {
        Log.e(
            TAG,
            "isLowRamDevice ::IllegalAccessException error",
            e
        )
    } catch (e: InvocationTargetException) {
        Log.e(
            TAG,
            "isLowRamDevice ::InvocationTargetException error",
            e
        )
    }
    return false
}

/**
 * 获取手机内存大小
 */
fun getTotalMemory():Long{
    var str1 = "/proc/meminfo"
    var str2: String?
    // String[] arrayOfString
    var arrayOfString: Array<String?>
    var totalMemory = 0L

    try {
        val localFileReader = FileReader(str1)
        val localBufferedReader = BufferedReader(localFileReader,8192)
        str2 = localBufferedReader.readLine()
        arrayOfString = str2.split("\\s+").toTypedArray()
        localBufferedReader.close()
    } catch (e: NumberFormatException) {
        Log.e(
            TAG,
            "getTotalMemory ::NumberFormatException error",
            e
        )
    } catch (e: FileNotFoundException) {
        Log.e(
            TAG,
            "getTotalMemory ::FileNotFoundException error",
            e
        )
    } catch (e: IOException) {
        Log.e(TAG, "getTotalMemory ::IOException error", e)
    }

    return totalMemory
}















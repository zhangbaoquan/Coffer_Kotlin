package com.kc.coffer.util

import android.os.Environment
import android.text.TextUtils
import java.io.File
import java.io.IOException

/**
 * author      : coffer
 * date        : 10/16/21
 * description : 文件工具类
 */

var mSDCardDir = ""

fun getSDCardDir(): String {
    if (!TextUtils.isEmpty(mSDCardDir)) {
        return mSDCardDir
    }
    if (hasSdcard()) {
        mSDCardDir = Environment.getExternalStorageDirectory().toString()
    } else {
        mSDCardDir = "/sdcard"
    }
    return mSDCardDir
}

fun hasSdcard() = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)

fun getPicPath() = "${getSDCardDir()}/coffer/pic/"

fun getApkPath() = "${getSDCardDir()}/coffer/apk/"

fun getTxtPath() = "${getSDCardDir()}/coffer/txt/"

fun getCrashPath() = "${getSDCardDir()}/coffer/crash/"

fun createFile(path: String): File? {
    val file = File(path)
    if (file.exists()){
        return file
    }
    try {
        val parentFile = file.parentFile
        val existDir = parentFile.exists()
        if (existDir) {
            val newFile = file.createNewFile()
            return if (newFile){
                file
            }else{
                null
            }
        }
    }catch (e:IOException){

    }
    return file
}
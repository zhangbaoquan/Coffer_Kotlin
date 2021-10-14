package com.kc.coffer;

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.dianping.logan.Logan
import com.dianping.logan.LoganConfig
import com.didichuxing.doraemonkit.DoraemonKit.install
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import com.tencent.mmkv.MMKV
import java.io.File

/**
 * author      : coffer
 * date        : 10/14/21
 * description :
 */
open class CofferApplication : Application() {

    companion object {
        const val TAG = "CofferApplication_tag"
        var mInstance: CofferApplication? = null

        fun getInstance(): CofferApplication? {
            return mInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
        install(this)
        initLog()
        MMKV.initialize(this)
        // 初始化ARouter

        // 初始化ARouter
        ARouter.init(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        mInstance = this
        MultiDex.install(this)
    }

    private fun initImageLoader(context: Context){
        // 创建DisplayImageOptions对象
        val defaultOptions = DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build()
        // 创建ImageLoaderConfiguration对象
        val configuration = ImageLoaderConfiguration.Builder(
            context
        ).defaultDisplayImageOptions(defaultOptions)
            .threadPriority(Thread.NORM_PRIORITY - 2)
            .denyCacheImageMultipleSizesInMemory()
            .diskCacheFileNameGenerator(Md5FileNameGenerator())
            .tasksProcessingOrder(QueueProcessingType.LIFO).build()
        // ImageLoader对象的配置
        // ImageLoader对象的配置
        ImageLoader.getInstance().init(configuration)
    }

    private fun initLog() {
        val config = LoganConfig.Builder()
            .setCachePath(applicationContext.filesDir.absolutePath)
            .setPath(
                applicationContext.getExternalFilesDir(null).absolutePath
                        + File.separator + "logan_v1"
            )
            .setEncryptKey16("0123456789012345".toByteArray())
            .setEncryptIV16("0123456789012345".toByteArray())
            .build()
        Logan.init(config)
    }
}

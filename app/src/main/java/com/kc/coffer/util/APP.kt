package com.kc.coffer.util

import android.annotation.SuppressLint
import android.app.Activity

/**
 * author      : coffer
 * date        : 10/13/21
 * description : 补充，关于Synchronized 和 volatile的使用，在kotlin中都是以注解的形式声明。
 * 使用companion object （伴生对象）可以直接通过容器类名来访问这个对象的方法和属性，写法上非常像Java的静态方法调用
 */
class APP {

    companion object {
        /**
         * 当前activity
         */
        @SuppressLint("StaticFieldLeak")
        private var mCurrActivity: Activity? = null

        private var mIsCanScrollToRight = true

        /**
         * 设置当前activity和handler
         */
        @Synchronized
        fun setCurrActivity(activity: Activity?) {
            mCurrActivity = activity
        }

        /**
         * 使用的时候要判断是否为null
         *
         * @return mCurrActivity
         */
        @Deprecated("能不用就不用，必须要用一定要判空")
        fun getCurrActivity(): Activity? {
            return mCurrActivity
        }

        fun setEnableScrollToRight(enable: Boolean) {
            mIsCanScrollToRight = enable
        }

        fun getEnableScrollToRigh(): Boolean {
            return mIsCanScrollToRight
        }
    }

}
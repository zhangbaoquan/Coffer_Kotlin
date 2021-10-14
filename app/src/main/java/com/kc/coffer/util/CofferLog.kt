package com.kc.coffer.util;

import android.util.Log
import com.kc.coffer.BuildConfig

/**
 * author      : coffer
 * date        : 10/14/21
 * description :
 */
open class CofferLog {
    companion object{
        const val COFFER_TAG = "coffer_tag"
        private val sDebuggable: Boolean = BuildConfig.DEBUG

        private fun CofferLog() {}

        fun D(tag: String?, msg: String?) {
            if (sDebuggable && msg != null) {
                Log.d(tag, msg)
            }
        }

        fun I(tag: String?, msg: String?) {
            if (sDebuggable && msg != null) {
                Log.i(tag, msg)
            }
        }

        fun e(tr: Throwable?) {
            if (sDebuggable && tr != null) {
                Log.e(COFFER_TAG, "error is ", tr)
            }
        }
    }
}

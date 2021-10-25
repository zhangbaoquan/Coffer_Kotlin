package com.kc.coffer.util;

import android.animation.TimeInterpolator

/**
 * author      : coffer
 * date        : 10/22/21
 * description :
 */
open class DecelerateAccelerateInterpolator : TimeInterpolator {

    override fun getInterpolation(input: Float): Float {
        var result: Float
        if (input <= 0.5f) {
            result = (Math.sin(Math.PI * input)).toFloat() / 2
        } else {
            result = (2 - Math.sin(Math.PI * input)).toFloat() / 2
        }
        return result
    }
}

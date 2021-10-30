package com.kc.coffer.util

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import com.kc.coffer.CofferApplication
import java.lang.Exception

/**
 * author      : coffer
 * date        : 10/25/21
 * description :
 */

private var displayMetrics: DisplayMetrics? = getDisplayMetrics()


fun dp2px(dp: Int): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), displayMetrics)
        .toInt()
}

fun dp2px(dp: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics)
        .toInt()
}

fun sp2px(sp: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, displayMetrics)
        .toInt()
}

fun px2dp(px: Int): Int {
    val scale = displayMetrics!!.density
    return (px / scale + 0.5f).toInt()
}

fun px2dp(px: Float): Int {
    val scale = displayMetrics!!.density
    return (px / scale + 0.5f).toInt()
}


fun getScreenWidth(): Int {
    return displayMetrics!!.widthPixels
}

fun getScreenHeight(): Int {
    return displayMetrics!!.heightPixels
}

fun getScreenDisplay(): IntArray? {
    val temp = IntArray(2)
    temp[0] = displayMetrics!!.widthPixels
    temp[1] = displayMetrics!!.heightPixels
    return temp
}

fun getDisplayMetrics(): DisplayMetrics? {
    val wm =
        CofferApplication.getInstance()?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val displayMetrics = DisplayMetrics()
    wm.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics
}

/**
 * 获取状态栏的高度
 *
 * @param activity Activity
 * @return 状态栏的高度，px
 */
fun getStatusBarHeight(activity: Activity): Int {
    val frame = Rect()
    activity.window.decorView.getWindowVisibleDisplayFrame(frame)
    return frame.top
}

/**
 * 获取状态栏高度
 */
fun getStatusBarHeight(context: Context): Int {
    return try {
        val c = Class.forName("com.android.internal.R\$dimen")
        val `object` = c.newInstance()
        val field = c.getField("status_bar_height")
        val x = field[`object`] as Int
        context.resources.getDimensionPixelSize(x)
    } catch (e: Exception) {
        0
    }
}


/**
 * 获取View的宽度
 *
 * @param view 带测量的View
 * @return 宽度
 */
fun getWidgetWidth(view: View): Int {
    val w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    val h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    view.measure(w, h)
    return view.measuredWidth
}

/**
 * 获取View的高度
 *
 * @param view 带测量的View
 * @return 高度
 */
fun getWidgetHeight(view: View): Int {
    val w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    val h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    view.measure(w, h)
    return view.measuredHeight
}
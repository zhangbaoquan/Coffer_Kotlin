package com.kc.coffer.util

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener
import java.lang.IllegalArgumentException

/**
 * author      : coffer
 * date        : 10/15/21
 * description : 工具
 */

fun dipToPixel(context: Context, dip: Float): Int {
    return (TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dip,
        context.resources.displayMetrics
    ) + 1).toInt()
}


fun dipToPixel(r: Resources, dip: Int): Int {
    return (TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dip.toFloat(),
        r.displayMetrics
    ) + 1).toInt()
}

fun spToPixel(context: Context, sp: Int): Int {
    return (TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, sp.toFloat(),
        context.resources.displayMetrics
    ) + 0.5f).toInt()
}

fun setNavVisibility(visible: Boolean, activity: Activity) {
//    val newVis = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    var newVis = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    if (!visible){
        newVis = (256 //View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or 512 //View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or 1024 //View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or 4 //View.SYSTEM_UI_FLAG_FULLSCREEN
                or 2 //View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                //                    | 4096 //View.SYSTEM_UI_FLAG_FULLSCREEN
                //                    | 2048; //View.SYSTEM_UI_FLAG_IMMERSIVE
                //                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY; //View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or 4096) //View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
          activity.window.decorView.systemUiVisibility = newVis
    }else{
        activity.window.setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
        activity.window.decorView.systemUiVisibility = newVis
    }
}

fun setPauseOnScrollListener(view: RecyclerView,customScrollListener: RecyclerView.OnScrollListener){
    if (view == null){
        throw IllegalArgumentException("view 不能为空")
    }
    var pauseOnScrollListener: PauseOnScrollListener
//    val level =
}






















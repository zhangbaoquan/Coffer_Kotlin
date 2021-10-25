package com.kc.coffer.util

import android.animation.*
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import java.lang.ref.WeakReference

/**
 * author      : coffer
 * date        : 10/16/21
 * description :
 */

/**
 * 这个是ValueAnimator的Demo
 * 将TextView的宽度从150变成500的动画
 */
fun animDemo1(view: View) {
    val animator = ValueAnimator.ofInt(view.layoutParams.height, 0)
    animator.setDuration(500)
    animator.startDelay = 500
    animator.repeatCount = 0
    animator.repeatMode = ValueAnimator.RESTART
    animator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) {
            super.onAnimationStart(animation)
        }

        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)
        }
    })

    // 步骤3：将改变的值手动赋值给对象的属性值：通过动画的更新监听器
    // 设置 值的更新监听器
    // 即：值每次改变、变化一次,该方法就会被调用一次
    animator.addUpdateListener { animation ->
        val currentValue: Int = animation.animatedValue as Int
        view.layoutParams.height = currentValue
        view.requestLayout()
    }
    animator.start()
}

fun animDemo2(view: View) {
    val objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f, 1f)
    objectAnimator.setDuration(5000)
    objectAnimator.start()
}

fun animDemo3(view: View) {
    val curTranslationX: Float = view.translationX
    val translation =
        ObjectAnimator.ofFloat(view, "translationX", curTranslationX, 300f, curTranslationX)
    translation.setInterpolator(BounceInterpolator())
    translation.setDuration(3000)

    val alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f, 1f)
    alpha.setDuration(2000)

    val animationSet = AnimatorSet()
    animationSet.playSequentially(translation, alpha)
    animationSet.start()
}

fun animDemo4(view: View) {
    var x = view.x
    var y = view.y
    view.animate().alpha(0f)
    view.animate().alpha(0f).setDuration(5000).setInterpolator(BounceInterpolator())
    view.animate().alpha(0f).x(500f).y(500f)
    view.animate().setUpdateListener { animation ->
        animation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                // 动画结束时，将位置和状态还原回去
                view.animate().x(x).y(y).alpha(1f)
            }
        })
    }
}

/**
 * 组合动画2
 * 这里的使用PropertyValuesHolder，可以将任意动画效果复合
 * 例如：下面的例子就是将平移动画、透明动画融合在一起。即两个动画一起执行。
 * 注意和上面的动画的区别。
 * 组合1的效果，其实还是两个动画按照顺序执行。
 */
fun animDemo5(view: View) {
    // 将平移动画、透明动画整合
    val propertyValuesHolder = arrayOf(
        PropertyValuesHolder.ofFloat("translationX", view.translationX, 300f, view.translationX),
        PropertyValuesHolder.ofFloat("alpha", 1f, 0f, 1f)
    )
    val animator = ObjectAnimator
        .ofPropertyValuesHolder(view, *propertyValuesHolder)
        .setDuration(5000)
    val animatorSet = AnimatorSet()
    animatorSet.playSequentially(animator, animator)
    animatorSet.start()
}

/**
 * 气泡动画（属性动画实现版）
 * 1、气泡从小到大时间为0.4秒
 * 2、气泡停留4秒
 * 3、气泡从大到消失时间为0.6秒
 * 这里使用了PropertyValuesHolder，PropertyValuesHolder这个类可以先将动画属性和值暂时的存储起来，后一起执行，在有些时候可以使用替换掉AnimatorSet
 */
fun animDemo6(view: View) {
    // 看到这个描述
    // 1、因为需要放大缩小，所以动画属性是scale动画，
    // 2、需要放大和缩小，因此就需要View的X、Y方向上同时操作，这就需要将scaleX、scaleY 复合
    // 3、需要放大完成后再缩小，所以需要将放大、缩小两个动画组合

//    // 第一阶段，将气泡从小放大
//    val enlarge = arrayOf(
//        PropertyValuesHolder.ofFloat("scaleX",0f,1f),
//        PropertyValuesHolder.ofFloat("scaleY",0f,1f)
//    )
//    // 第二阶段，不变
//    val normal = arrayOf(
//        PropertyValuesHolder.ofFloat("scaleX",1f,1f),
//        PropertyValuesHolder.ofFloat("scaleY",1f,1f)
//    )
    // 第三阶段，将气泡从大变小
    val shrink = arrayOf(
        PropertyValuesHolder.ofFloat("scaleX", 1f, 0.3f),
        PropertyValuesHolder.ofFloat("scaleY", 1f, 0.3f)
    )
    val animator = ObjectAnimator.ofPropertyValuesHolder(view, *shrink)
    animator.duration = 2000
    val animatorSet = AnimatorSet()
    animatorSet.playSequentially(animator)
    animator.start()
}

fun animDemo7(view: View) {
    val keyframe1 = Keyframe.ofFloat(0.0f, 0f)
    val keyframe2 = Keyframe.ofFloat(0.25f, -30f)
    val keyframe3 = Keyframe.ofFloat(0.5f, 0f)
    val keyframe4 = Keyframe.ofFloat(0.75f, 30f)
    val keyframe5 = Keyframe.ofFloat(1.0f, 0f)
    // 这个动画就是实现View的左右两边上下抖动的效果，抖动有旋转的特征，所以这里使用的是"rotation"属性
    // 这里分五帧实现。
    // 第一帧是在执行的时间的内保持水平位置
    // 第二帧是在执行的时间的1/4区间内，向上旋转30度
    // 第三帧是在执行的时间的1/4区间内，回到水平位置
    // 第四帧是在执行的时间的1/4区间内，向下旋转30度
    // 第五帧是在执行的时间的1/4区间内，回到水平位置
    val rotation = PropertyValuesHolder.ofKeyframe(
        "rotation",
        keyframe1,
        keyframe2,
        keyframe3,
        keyframe4,
        keyframe5
    )
    val alpha = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.2f, 1.0f)
    val scaleX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 0.2f, 1.0f)
    val scaleY = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0.2f, 1.0f)
    val color = PropertyValuesHolder.ofInt("BackgroundColor", -0x100, -0xffff01)
    val animator =
        ObjectAnimator.ofPropertyValuesHolder(view, alpha, scaleX, scaleY, color, rotation)
    animator.setInterpolator(OvershootInterpolator())
    animator.duration = 5000
    animator.start()
}

fun animDemo8(view: View) {
// 将平移动画、透明动画整合
    // 将平移动画、透明动画整合
    val propertyValuesHolder = arrayOf(
        PropertyValuesHolder.ofFloat("translationY", 0.0f, 300f, 0.0f),
        PropertyValuesHolder.ofFloat("alpha", 0f, 1f, 0f)
    )
    val animator = ObjectAnimator
        .ofPropertyValuesHolder(view, *propertyValuesHolder)
        .setDuration(5000)
    val animatorSet = AnimatorSet()
    animatorSet.playSequentially(animator)
    animatorSet.start()
}

/**
 * 将一个 view 渐影消失
 * @param view view 实例
 * @param animTime 动画时长
 * @param endRunnable onAnimationEnd() 执行后的操作
 */
fun fadeOut(
    view: View,
    animTime: Int,
    endRunnable: WeakReference<Runnable?>?
) {
    val objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
    objectAnimator.duration = animTime.toLong()
    objectAnimator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {}
        override fun onAnimationEnd(animation: Animator) {
            view.alpha = 1f
            if (endRunnable != null && endRunnable.get() != null) {
                endRunnable.get()!!.run()
            }
        }

        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    })
    objectAnimator.start()
}


/**
 * 动画效果：
 * 进行：1、View上移，2、View变小。
 * 结束：1、文字颜色变化，2、文字字体变细（Normal）
 */
fun showFocusAnim1(view: TextView) {
    val shrink = arrayOf(
        PropertyValuesHolder.ofFloat("scaleX", 1f, 0.5f),
        PropertyValuesHolder.ofFloat("scaleY", 1f, 0.5f),
        PropertyValuesHolder.ofFloat("translationY", 0.0f, -25f)
    )

    val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, *shrink)
    view.pivotX = 0f
    view.pivotY = 0f
    objectAnimator.duration = 3000
    objectAnimator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)
            view.setTypeface(null, Typeface.NORMAL)
            view.setTextColor(Color.BLUE)
        }
    })
    objectAnimator.start()
}

/**
 * 动画效果：
 * 进行：1、View下移，2、View变大。
 * 结束：1、文字颜色变化，2、文字字体变粗（Bold）
 */
fun showNoFocusAnim(view: TextView) {
    var shrink = arrayOf(
        PropertyValuesHolder.ofFloat("scaleX", 0.67f, 1f),
        PropertyValuesHolder.ofFloat("scaleY", 0.67f, 1f),
        PropertyValuesHolder.ofFloat("translationY", view.translationY, 0f)
    )
    val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, *shrink)
    view.pivotX = 0f
    view.pivotY = 0f
    objectAnimator.duration = 3000
    objectAnimator.addUpdateListener {
        object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                view.setTypeface(null, Typeface.BOLD)
                view.setTextColor(Color.BLUE)
            }
        }

    }
    objectAnimator.start()

}





























































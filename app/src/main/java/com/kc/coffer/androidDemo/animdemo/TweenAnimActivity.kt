package com.kc.coffer.androidDemo.animdemo;

import android.view.View
import android.view.animation.*
import android.widget.Button
import com.kc.coffer.BaseDefaultActivity
import com.kc.coffer.R

/**
 * author      : coffer
 * date        : 10/22/21
 * description : 补间动画
 */
open class TweenAnimActivity : BaseDefaultActivity() {

    override fun initView() {
        setContentView(R.layout.activity_tween_anim_main);
        findViewById<Button>(R.id.bt1).setOnClickListener {
            translateAnim(it)
        }
        findViewById<Button>(R.id.bt2).setOnClickListener {
            scaleAnim(it)
        }
        findViewById<Button>(R.id.bt3).setOnClickListener {
            rotateAnim(it)
        }
        findViewById<Button>(R.id.bt4).setOnClickListener {
            listAnim(it)
        }
    }

    override fun initData() {

    }

    /**
     * 平移动画
     */
    private fun translateAnim(view: View) {
        val translation = TranslateAnimation(0f, 100f, 0f, 100f)
        translation.duration = 3000
        translation.fillAfter = false
        view.startAnimation(translation)
    }

    /**
     * 缩放动画
     */
    private fun scaleAnim(view: View) {
        val scale = ScaleAnimation(
            0f, 2f, 0f, 2f, Animation.RELATIVE_TO_SELF,
            0f, Animation.RELATIVE_TO_SELF, 1f
        );
        scale.duration = 3000
        scale.fillAfter = false
        view.startAnimation(scale)
    }

    /**
     * 旋转动画
     */
    private fun rotateAnim(view: View) {

        // 使用java 的方式
        // 设置旋转中心的位置是在相对于View自身的左下角（0，1），若设置中心位置则（0.5f,0.5f）
        val rotate: Animation = RotateAnimation(
            0f, 360f, Animation.RELATIVE_TO_SELF,
            0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotate.fillAfter = false
        rotate.duration = 3000
        view.startAnimation(rotate)
    }

    /**
     * 动画集合
     */
    private fun listAnim(view: View) {
        val animationSet = AnimationSet(true)
        animationSet.duration = 3000
        animationSet.fillAfter = false

        val translation: Animation = TranslateAnimation(
            0f, 100f, 0f,
            100f
        )
        val scale: Animation = ScaleAnimation(
            0f, 2f, 0f, 2f, Animation.RELATIVE_TO_SELF,
            0f, Animation.RELATIVE_TO_SELF, 1f
        )
        val rotate: Animation = RotateAnimation(
            0f, 360f, Animation.RELATIVE_TO_SELF,
            0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        val alpha: Animation = AlphaAnimation(0f, 1f)

        // 按照顺序添加动画
        animationSet.addAnimation(translation)
        animationSet.addAnimation(scale)
        animationSet.addAnimation(rotate)
        animationSet.addAnimation(alpha)
        view.startAnimation(animationSet)

        animationSet.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }
            override fun onAnimationEnd(animation: Animation) {

            }
            override fun onAnimationRepeat(animation: Animation) {

            }
        })
    }
}

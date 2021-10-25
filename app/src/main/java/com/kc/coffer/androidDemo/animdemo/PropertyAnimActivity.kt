package com.kc.coffer.androidDemo.animdemo;

import android.animation.*
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.kc.coffer.BaseDefaultActivity
import com.kc.coffer.R
import com.kc.coffer.util.CofferLog
import com.kc.coffer.util.DecelerateAccelerateInterpolator
import com.kc.coffer.util.dipToPixel

/**
 * author      : coffer
 * date        : 10/15/21
 * description : 属性动画
 */
open class PropertyAnimActivity : BaseDefaultActivity() {

    val TAG = "PropertyAnimActivity_tag"

    lateinit var mIv: ImageView
    lateinit var mArrow: ImageView
    var flag = false

    override fun initView() {
        setContentView(R.layout.activity_property_anim_main)
        mIv = findViewById(R.id.iv)
        mArrow = findViewById(R.id.arrow)
        findViewById<TextView>(R.id.bt1).setOnClickListener {
            changeHeight()
//            if (!flag) {
//                flag = true
//                rotate()
//            } else {
//                flag = false
//                rotate2()
//            }
        }
        mIv.setOnClickListener {
            Toast.makeText(
                this@PropertyAnimActivity,
                "啦啦",
                Toast.LENGTH_SHORT
            ).show()
        }

        findViewById<TextView>(R.id.bt2).setOnClickListener(View.OnClickListener { moveLeftSide() })

        findViewById<TextView>(R.id.bt3).setOnClickListener(View.OnClickListener { animatorGroup() })

        findViewById<TextView>(R.id.bt4).setOnClickListener(View.OnClickListener { useFastAnim() })

        findViewById<TextView>(R.id.bt5).setOnClickListener(View.OnClickListener { changeScale() })
    }

    override fun initData() {
    }

    private fun rotate(){
        val objectAnimator = ObjectAnimator.ofFloat(mArrow,"rotationX",0f,180f)
        objectAnimator.setDuration(200)
        objectAnimator.start()
    }

    private fun rotate2(){
        val objectAnimator = ObjectAnimator.ofFloat(mArrow,"rotationX",180f,360f)
        objectAnimator.setDuration(200)
        objectAnimator.start()
    }

    private fun changeHeight(){
        val valueAnimator = ValueAnimator.ofInt(0,dipToPixel(this,100f))
        valueAnimator.duration = 2000
        valueAnimator.interpolator = DecelerateAccelerateInterpolator()
        // 将属性值手动赋值给对象的属性
        valueAnimator.addUpdateListener { animation ->
            val currentValue = animation.animatedValue as Int
            // 每次值变化，手动赋值给View的属性
            mIv.layoutParams.height = currentValue
            mIv.requestLayout()
        }
        valueAnimator.start()

    }

    /**
     * 改变View 缩放
     */
    private fun changeScale() {
        val objectAnimator = ObjectAnimator.ofFloat(mIv, "scaleX", 1f, 0.7f)
        mIv.pivotX = 0f
        mIv.pivotY = 0f
        objectAnimator.duration = 3000
        objectAnimator.start()
    }

    /**
     * 从左侧飞入
     */
    private fun moveLeftSide() {
        // 方式二，使用
        mIv.x = (-dipToPixel(this, 100f)).toFloat()
        CofferLog.D(TAG, "getTranslationX ： " + mIv.translationX)
        val objectAnimator = ObjectAnimator.ofFloat(
            mIv, "translationX",
            mIv.translationX, (dipToPixel(this, 100f)).toFloat()
        )
        objectAnimator.duration = 3000
        objectAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                CofferLog.D(TAG, "getTranslationX2 ： " + mIv.translationX)
            }
        })
        objectAnimator.start()
    }

    /**
     * 组合动画
     */
    private fun animatorGroup() {
        val animationSet = AnimatorSet()

//        ObjectAnimator alpha = ObjectAnimator.ofFloat(mIv,"alpha",0,1);
//        ObjectAnimator rotate = ObjectAnimator.ofFloat(mIv,"rotation",0,360);
//        ObjectAnimator translate = ObjectAnimator.ofFloat(mIv,"translationX",mIv.getTranslationX(),500);
        //  这一句设置的意思是动画先执行透明，再执行旋转
//        animationSet.playSequentially(alpha,rotate);

        // 下面透明、旋转、平移一起执行
//        animationSet.play(alpha).with(rotate).with(translate);
        // 一起执行还有一种办法
        val propertyValuesHolder = arrayOf(
            PropertyValuesHolder.ofFloat("alpha", 0f, 1f),
            PropertyValuesHolder.ofFloat("rotation", 0f, 360f),
            PropertyValuesHolder.ofFloat("translationX", mIv.translationX, 500f)
        )
        val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(mIv, *propertyValuesHolder)
        animationSet.play(objectAnimator)
        animationSet.duration = 3000
        animationSet.start()
    }

    /**
     * 使用快捷方式写动画
     */
    private fun useFastAnim() {
        mIv.animate().setDuration(100).translationX(dipToPixel(this, 100f).toFloat()).start()
    }

}















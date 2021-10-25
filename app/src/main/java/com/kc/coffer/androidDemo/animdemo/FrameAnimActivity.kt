package com.kc.coffer.androidDemo.animdemo;

import android.annotation.SuppressLint
import android.graphics.drawable.AnimationDrawable
import android.widget.Button
import android.widget.ImageView
import com.kc.coffer.BaseDefaultActivity
import com.kc.coffer.R

/**
 * author      : coffer
 * date        : 10/22/21
 * description : 逐帧动画
 */
open class FrameAnimActivity : BaseDefaultActivity() {

    lateinit var mIv:ImageView
    lateinit var mAnimationDrawable:AnimationDrawable

    override fun initView() {
        setContentView(R.layout.activity_frame_anim_main);
        mIv = findViewById(R.id.iv)
        findViewById<Button>(R.id.bt1).setOnClickListener {
            start()
        }
        findViewById<Button>(R.id.bt2).setOnClickListener {
            stop()
        }

    }

    override fun initData() {
    }

    private fun start(){
        mIv.setImageDrawable(resources.getDrawable(R.drawable.guide_smile_anim))
        mAnimationDrawable = mIv.drawable as AnimationDrawable
        mAnimationDrawable.start()
    }

    private fun stop(){
        mAnimationDrawable = mIv.drawable as AnimationDrawable
        mAnimationDrawable.stop()
    }
}

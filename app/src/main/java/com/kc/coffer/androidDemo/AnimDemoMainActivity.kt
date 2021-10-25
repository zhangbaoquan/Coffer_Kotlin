package com.kc.coffer.androidDemo;

import android.content.Intent
import android.view.View
import android.widget.Button
import com.kc.coffer.BaseDefaultActivity
import com.kc.coffer.R
import com.kc.coffer.androidDemo.animdemo.AnimTestActivity
import com.kc.coffer.androidDemo.animdemo.FrameAnimActivity
import com.kc.coffer.androidDemo.animdemo.PropertyAnimActivity
import com.kc.coffer.androidDemo.animdemo.TweenAnimActivity
import javax.annotation.meta.When

/**
 * author      : coffer
 * date        : 10/14/21
 * description : 动画练习
 */
open class AnimDemoMainActivity: BaseDefaultActivity(),View.OnClickListener{
    override fun initView() {
        setContentView(R.layout.activity_anim_main)
        findViewById<Button>(R.id.b1).setOnClickListener(this)
        findViewById<Button>(R.id.b2).setOnClickListener(this)
        findViewById<Button>(R.id.b3).setOnClickListener(this)
        findViewById<Button>(R.id.b4).setOnClickListener(this)
    }

    override fun initData() {

    }

    override fun onClick(v: View?) {
        v?.let {
            when(v.id){
                R.id.b1 ->{
                    // 属性动画
                    startActivity(Intent(this,PropertyAnimActivity::class.java))
                }
                R.id.b2 ->{
                    // 逐帧动画
                    startActivity(Intent(this,FrameAnimActivity::class.java))
                }
                R.id.b3 ->{
                    // 补间动画
                    startActivity(Intent(this,TweenAnimActivity::class.java))
                }
                R.id.b4 ->{
                    // 动画demo
                    startActivity(Intent(this,AnimTestActivity::class.java))
                }
            }
        }
    }
}

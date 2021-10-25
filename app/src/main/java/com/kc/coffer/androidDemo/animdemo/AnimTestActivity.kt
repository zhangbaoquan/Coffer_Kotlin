package com.kc.coffer.androidDemo.animdemo;

import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.kc.coffer.BaseDefaultActivity
import com.kc.coffer.R
import com.kc.coffer.util.*
import kotlinx.android.synthetic.main.activity_anim_test_main.*
import java.util.logging.Handler

/**
 * author      : coffer
 * date        : 10/22/21
 * description : 动画demo
 */
open class AnimTestActivity : BaseDefaultActivity() {

    lateinit var tv10: TextView
    lateinit var mParent: LinearLayout

    override fun initView() {
        setContentView(R.layout.activity_anim_test_main)
        mParent = findViewById(R.id.parent)
        findViewById<TextView>(R.id.tv1).setOnClickListener {
            animDemo1(it)
        }
        findViewById<TextView>(R.id.tv2).setOnClickListener {
            animDemo2(it)
        }
        findViewById<TextView>(R.id.tv3).setOnClickListener {
            animDemo3(it)
        }
        findViewById<TextView>(R.id.tv4).setOnClickListener {
            animDemo4(it)
        }
        findViewById<TextView>(R.id.tv5).setOnClickListener {
            animDemo5(it)
        }
        findViewById<TextView>(R.id.tv6).setOnClickListener {
            animDemo6(it)
        }
        findViewById<TextView>(R.id.tv7).setOnClickListener {
            animDemo7(it)
        }

        val button = Button(this)
        button.text = "锚点测试"
        button.layoutParams = ViewGroup.LayoutParams(200, 100)
        button.setBackgroundColor(resources.getColor(R.color.blue))
        button.x = 100f
        button.y = 100f
        mParent.addView(button)
        findViewById<TextView>(R.id.tv8).setOnClickListener {
            animDemo8(button)
        }

        findViewById<TextView>(R.id.tv9).setOnClickListener {
            fadeOut(it, 200, null)
        }

        tv10 = findViewById(R.id.tv10)
        findViewById<TextView>(R.id.bt1).setOnClickListener {
            showFocusAnim1(tv10);
        }

        findViewById<TextView>(R.id.bt2).setOnClickListener {
            showFocusAnim1(tv10);
        }


    }

    override fun initData() {

    }
}

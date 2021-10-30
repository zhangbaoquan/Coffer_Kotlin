package com.kc.coffer.androidDemo;

import android.content.Intent
import android.view.View
import com.kc.coffer.BaseDefaultActivity
import com.kc.coffer.R
import com.kc.coffer.androidDemo.customview.*

/**
 * author      : coffer
 * date        : 10/14/21
 * description : 自定义View练习系列
 */
open class CustomViewMainActivity : BaseDefaultActivity() {
    override fun initView() {
        setContentView(R.layout.activity_custom_view_main)
        // 绘制练习
        findViewById<View>(R.id.b0).setOnClickListener {
            val intent = Intent(
                this@CustomViewMainActivity,
                DrawActivity::class.java
            )
            startActivity(intent)
        }
        // 绘制练习
        findViewById<View>(R.id.b1).setOnClickListener {
            val intent = Intent(
                this@CustomViewMainActivity,
                DrawViewActivity::class.java
            )
            startActivity(intent)
        }
        // 自定义View的滑动和绘制
        findViewById<View>(R.id.b2).setOnClickListener {
            val intent = Intent(
                this@CustomViewMainActivity,
                ScrollActivity2::class.java
            )
            startActivity(intent)
        }
        // 弹性RecycleView 的实现
        findViewById<View>(R.id.b3).setOnClickListener {
            val intent = Intent(
                this@CustomViewMainActivity,
                FlexibleViewActivity::class.java
            )
            startActivity(intent)
        }
        // 自定义ViewGroup 的实现
        findViewById<View>(R.id.b4).setOnClickListener {
            val intent = Intent(
                this@CustomViewMainActivity,
                ArrangeViewActivity::class.java
            )
            startActivity(intent)
        }
        // 画廊 的实现
        findViewById<View>(R.id.b5).setOnClickListener {
            val intent = Intent(
                this@CustomViewMainActivity,
                GalleryActivity::class.java
            )
            startActivity(intent)
        }
        // 左右弹性阻尼的View 的实现
        findViewById<View>(R.id.b6).setOnClickListener {
            val intent = Intent(
                this@CustomViewMainActivity,
                DampViewActivity::class.java
            )
            startActivity(intent)
        }
        // ListView
        findViewById<View>(R.id.b7).setOnClickListener {
            val intent = Intent(
                this@CustomViewMainActivity,
                ListViewActivity::class.java
            )
            startActivity(intent)
        }
        // 多布局样式
        findViewById<View>(R.id.b8).setOnClickListener {
            val intent = Intent(
                this@CustomViewMainActivity,
                MutiTypeActivity::class.java
            )
            startActivity(intent)
        }
        // Banner 系列
        findViewById<View>(R.id.b9).setOnClickListener {
            val intent = Intent(
                this@CustomViewMainActivity,
                BannerActivity::class.java
            )
            startActivity(intent)
        }
        // Banner 系列
        findViewById<View>(R.id.b10).setOnClickListener {
            val intent = Intent(
                this@CustomViewMainActivity,
                ScrollLoginActivity::class.java
            )
            startActivity(intent)
        }
        // 滑动事件练习
        findViewById<View>(R.id.b11).setOnClickListener {
            val intent = Intent(
                this@CustomViewMainActivity,
                ScrollContainerActivity::class.java
            )
            startActivity(intent)
        }
        // 绘制练习2
        findViewById<View>(R.id.b12).setOnClickListener {
            val intent = Intent(
                this@CustomViewMainActivity,
                DrawViewActivity2::class.java
            )
            startActivity(intent)
        }
    }

    override fun initData() {

    }
}

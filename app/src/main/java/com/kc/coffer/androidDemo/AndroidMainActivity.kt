package com.kc.coffer.androidDemo;

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kc.coffer.BaseActivity
import com.kc.coffer.BaseDefaultActivity
import com.kc.coffer.R

/**
 * author      : coffer
 * date        : 10/14/21
 * description :
 */
open class AndroidMainActivity: BaseDefaultActivity() {

    override fun initView() {
        setContentView(R.layout.activity_android_main)
        // 动画练习系列
        findViewById<Button>(R.id.b1).setOnClickListener {
            startActivity(Intent(this,AnimDemoMainActivity::class.java))
        }
        // 自定义View 系列
        findViewById<Button>(R.id.b2).setOnClickListener {
            startActivity(Intent(this,CustomViewMainActivity::class.java))
        }
        // 消息机制
        findViewById<Button>(R.id.b3).setOnClickListener {

        }
        // BottomSheetBehavior
        findViewById<Button>(R.id.b4).setOnClickListener {

        }

        findViewById<Button>(R.id.b5).setOnClickListener {

        }
    }

    override fun initData() {
        val content = "今日剩余10次机会"
        val spannableString = SpannableString(content)
        val colorSpan = ForegroundColorSpan(Color.parseColor("#5c6273"))
        val colorSpan2 = ForegroundColorSpan(Color.parseColor("#D81B60"))
        val colorSpa1 = ForegroundColorSpan(Color.parseColor("#0099EE"))
        spannableString.setSpan(colorSpan2, 0, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(colorSpa1, 4, 4 + 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(colorSpan, 4 + 2, content.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        val textView = findViewById<TextView>(R.id.t1)
        textView.text = spannableString
    }
}

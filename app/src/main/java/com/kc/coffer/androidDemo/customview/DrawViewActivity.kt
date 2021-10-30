package com.kc.coffer.androidDemo.customview;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.kc.coffer.BaseDefaultActivity
import com.kc.coffer.R
import com.kc.coffer.widget.BannerContentLayout
import com.nostra13.universalimageloader.core.ImageLoader

/**
 * author      : coffer
 * date        : 10/25/21
 * description :
 */
open class DrawViewActivity: BaseDefaultActivity() {

    private lateinit var mBanner: BannerContentLayout

    override fun initView() {
        setContentView(R.layout.activity_draw_view_main)
        mBanner = findViewById(R.id.banner)
    }

    override fun initData() {
        createBannerContentView()
    }

    fun createBannerContentView(){
        for (i in 0 until 3){
            mBanner.addView(getView(i))
        }
    }

    private fun getView(i: Int): View {
        val adView: View =
            LayoutInflater.from(this@DrawViewActivity).inflate(R.layout.activity_view_item, null)
        adView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val body = adView.findViewById<View>(R.id.body)
        val ivAd = adView.findViewById<ImageView>(R.id.iv_ad)
        val tvTitle = adView.findViewById<TextView>(R.id.tv_title)
        val tvDesc = adView.findViewById<TextView>(R.id.tv_desc)
        body.setOnClickListener {
            Toast.makeText(
                this@DrawViewActivity,
                "点啦",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (i == 0) {
            tvTitle.text = "啦啦"
            tvDesc.text = "凉宫春日的忧郁"
            ImageLoader.getInstance()
                .displayImage("http://img15.3lian.com/2015/f3/08/d/02.jpg", ivAd)
        } else if (i == 1) {
            tvTitle.text = "哈哈"
            tvDesc.text = "凉宫春日的烦闷"
            ImageLoader.getInstance()
                .displayImage("http://pic2.16pic.com/00/15/80/16pic_1580359_b.jpg", ivAd)
        } else {
            tvTitle.text = "露露"
            tvDesc.text = "凉宫春日的动摇"
            ImageLoader.getInstance()
                .displayImage("http://img15.3lian.com/2015/f3/08/d/02.jpg", ivAd)
        }
        return adView
    }
}

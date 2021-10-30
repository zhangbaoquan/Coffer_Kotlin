package com.kc.coffer.androidDemo.customview;

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.kc.coffer.BaseDefaultActivity
import com.kc.coffer.R
import com.kc.coffer.androidDemo.adapter.RcAdapter
import com.kc.coffer.model.BaseData
import com.kc.coffer.widget.StretchRecycleView

/**
 * author      : coffer
 * date        : 10/25/21
 * description :
 */
open class FlexibleViewActivity: BaseDefaultActivity() {

    private lateinit var mRecycleVie:StretchRecycleView
    private val mList:ArrayList<BaseData> = ArrayList()

    override fun initView() {
        setContentView(R.layout.activity_flexible_view_main)
        mRecycleVie = findViewById(R.id.rc)
        mRecycleVie.layoutManager = LinearLayoutManager(this)
        mRecycleVie.setHasFixedSize(true)
    }

    override fun initData() {
        initDataSource()
        val adapter = RcAdapter(this)
        adapter.setData(mList)
        mRecycleVie.adapter = adapter
    }

    private fun initDataSource(){
        mList.add(BaseData("亚特兰大老鹰"))
        mList.add(BaseData("夏洛特黄蜂"))
        mList.add(BaseData("迈阿密热火"))
        mList.add(BaseData("奥兰多魔术"))
        mList.add(BaseData("华盛顿奇才"))
        mList.add(BaseData("波士顿凯尔特人"))
        mList.add(BaseData("布鲁克林篮网"))
        mList.add(BaseData("纽约尼克斯"))
        mList.add(BaseData("费城76人"))
        mList.add(BaseData("多伦多猛龙"))
        mList.add(BaseData("芝加哥公牛"))
        mList.add(BaseData("克里夫兰骑士"))
        mList.add(BaseData("底特律活塞"))
        mList.add(BaseData("印第安纳步行者"))
        mList.add(BaseData("密尔沃基雄鹿"))
        mList.add(BaseData("达拉斯独行侠"))
        mList.add(BaseData("休斯顿火箭"))
        mList.add(BaseData("孟菲斯灰熊"))
        mList.add(BaseData("新奥尔良鹈鹕"))
        mList.add(BaseData("圣安东尼奥马刺"))
        mList.add(BaseData("丹佛掘金"))
        mList.add(BaseData("明尼苏达森林狼"))
        mList.add(BaseData("俄克拉荷马城雷霆"))
        mList.add(BaseData("波特兰开拓者"))
        mList.add(BaseData("犹他爵士"))
        mList.add(BaseData("金州勇士"))
        mList.add(BaseData("洛杉矶快船"))
        mList.add(BaseData("洛杉矶湖人"))
        mList.add(BaseData("菲尼克斯太阳"))
        mList.add(BaseData("萨克拉门托国王"))
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            val h1 = mRecycleVie.height
            val h2 = (mRecycleVie.parent as ViewGroup).height
            if (h1 != h2) {
                val layoutParams = mRecycleVie.layoutParams as LinearLayout.LayoutParams
                layoutParams.height = h2
                mRecycleVie.layoutParams = layoutParams
            }
        }
    }
}
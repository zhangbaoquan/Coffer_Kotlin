package com.kc.coffer

import android.content.Intent
import android.content.Intent.ACTION_DEFAULT
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Button
import android.widget.Toast
import com.kc.coffer.androidDemo.AndroidMainActivity
import com.kc.coffer.util.CofferLog
import com.tencent.mmkv.MMKV
import java.lang.Exception

class MainActivity : BaseActivity(){

    override fun initView() {
        setContentView(R.layout.activity_main)
        // 插件业务
        findViewById<Button>(R.id.b0).setOnClickListener {
            Toast.makeText(this,"haha",Toast.LENGTH_SHORT).show()
        }
        // java 综合
        findViewById<Button>(R.id.b2).setOnClickListener {

        }
        // android 综合
        findViewById<Button>(R.id.b5).setOnClickListener {
            startActivity(Intent(this@MainActivity,AndroidMainActivity::class.java))
        }
        // 网络框架学习
        findViewById<Button>(R.id.b8).setOnClickListener {

        }
        // Jetpack 组件练习
        findViewById<Button>(R.id.b11).setOnClickListener {

        }
    }

    override fun initData() {
        // deepLink业务
        findViewById<Button>(R.id.b15).setOnClickListener {
//                String url = "iqiyi://mobile/player?aid=239741901&tvid=14152064500&ftype=27&subtype=vivoqd_2843";
//                String url = "ireader://com.chaozh.iReader/readbook?bookid=11591589";
//                String url = "ireader://com.oppo.reader/openurl?url=https%3A%2F%2Fah2.zhangyue.com%" +
//                        "2Fzybook3%2Fapp%2Fapp.php%3Fca%3DChannel.Index%26pk%3Dqd%26key%3Dch_free%26a0%3Dbanner_oppo_sd_mfpd";
//                String url = "ireader://com.oppo.reader/openurl?url=https%3A%2F%2Fah2.zhangyue.com%" +
//                        "2Fzyvr%2Frender%3Fid%3D10608%26a0%3Dpush_oppo_sd_wbhs&fromname=应用商店&flags=4&from=com.oppo.market";
//                String url = "ireader://com.oppo.reader/openurl?url=https%3A%2F%2Fah2.zhangyue.com%2Fzybook3%2Fapp%2" +
//                        "Fapp.php%3Fca%3DChannel.Index%26pk%3Dqd%26key%3Dch_feature%26a0%3Dtoufang01s&fromname=浏览器&flags=4&" +
//                        "from=com.heytap.browser";
//                String url = "ireaderplugin://com.coloros.browser/readbook?bookid=11006182";
//                String url = "ireaderplugin://com.coloros.browser/maintab?nightmode=100&tabindex=1";
//                String url = "dididriver://com.sdu.didi.gsui.DidiMsgActivity";
            val url = "unidriver://web?url=https://page.didiglobal.com/driver-page/mid-page/"
            val uri = Uri.parse(url)
            val intent = Intent(ACTION_DEFAULT, uri)
            try {
                startActivity(intent)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    private val handler = Handler()

    private fun useMkv() {
        val mmkv = MMKV.defaultMMKV()
        // 不用再如SharedPreferences一样调用apply或commit：非常方便
        mmkv.encode("bool", true)
        mmkv.encode("int", 1)
        mmkv.decodeBool("hehe")
        CofferLog.I("lalal", "info: " + mmkv.decodeBool("bool"))
        CofferLog.I("lalal", "info: " + mmkv.decodeInt("int"))
        // 删除数据
        mmkv.clear()
        // 查询数据是否存在
        val hasBool = mmkv.containsKey("bool")
        handler.postDelayed(Runnable { CofferLog.D("coffer_tag", "嘿嘿") }, 500)
        val message = Message.obtain()
        message.what = 1
        message.obj = "coffer"
        handler.sendMessage(message)
    }

}
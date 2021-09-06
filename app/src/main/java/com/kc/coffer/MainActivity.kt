package com.kc.coffer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

private const val packageName1 = "com.kc.dynamic_feature1"
private const val instantPackageName1 = "$packageName1.ui.login.LoginActivity"

private const val packageName2 = "com.kc.dynamicfeature"
private const val instantPackageName2 = "$packageName2.FeatureActivity"

class MainActivity : AppCompatActivity(),View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.btn -> launchActivity()
            }
        }
    }

    fun launchActivity(){
        Intent().setClassName(BuildConfig.APPLICATION_ID, instantPackageName2).also {
            Log.d("hahah","哈哈")
            startActivity(it)
        }
    }
}
package com.kc.coffer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kc.coffer.util.APP

const val PARAM_TARGET_INTENT = "targetIntent"

abstract class BaseDefaultActivity :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCurrentActivity()
        initView()
        initData()
    }

    abstract fun initView()

    abstract fun initData()

    override fun onResume() {
        super.onResume()
        setCurrentActivity()
    }

    protected fun setCurrentActivity(){
        if (parent == null){
            APP.setCurrActivity(this)
        }
    }

    protected fun clearCurrentActivity(){
        // 只有当前activity是自己时，才能清除，因为在acivity切换过程中设置和清除方法可能交替调用多次
        // 注意下面的 === 相当于Java的 == 比较的是对象的引用
        if (APP.getCurrActivity() === this){
            APP.setCurrActivity(null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
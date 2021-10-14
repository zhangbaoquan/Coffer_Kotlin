package com.kc.coffer;

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.ArrayList

/**
 * author      : coffer
 * date        : 10/13/21
 * description :
 */
abstract class BaseActivity : BaseDefaultActivity() {
    companion object{
        /**
         * 权限请求码
         */
        private const val REQUESTCODE = 100
        private const val mPackName = "coffer.androidjatpack"
    }

    /**
     * 声明一个数组将所有需要申请的权限都放入，这里的arrayOf 相当于Java的数组
     */
    private val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)

    /**
     * 创建一个mPermissionList，逐个判断哪些权限未授权，将未授权的权限存储到mPermissionList中
     * 注意这里声明的类型是MutableList（可变集合），可以调用例如clear、addAll()这样的方法
     */
    var mPermissionList: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPermission()
    }

    /***************   权限申请    **************/
    private fun initPermission(){
        //清空已经允许的没有通过的权限
        mPermissionList.clear()
        // 逐个判断是否还有未通过的权限。注意遍历数组，这里使用的是indices
        for (i in permissions.indices){
            if (ContextCompat.checkSelfPermission(this,permissions[i]) !=
                PackageManager.PERMISSION_GRANTED){
                //添加还未授予的权限到mPermissionList中
                mPermissionList.add(permissions[i])
            }
        }
        if (mPermissionList.size>0){
            //有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, REQUESTCODE)
        } else {
            // 权限已经都通过了，可以将程序继续打开了
            // 注意这里的 this@BaseActivity 写法，相当于Java的BaseActivity.this
            Toast.makeText(this@BaseActivity,"申请成功", Toast.LENGTH_SHORT).show()
        }
    }

    var mPermissionDialog: AlertDialog? = null

    private fun showPermissionDialog(){
        if (mPermissionDialog == null) {
            mPermissionDialog = AlertDialog.Builder(this)
                .setMessage("已禁用权限，请手动授予")
                .setPositiveButton("设置") { dialog, which ->
                    cancelPermissionDialog()
                    val packageURI = Uri.parse("package:$mPackName")
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        packageURI
                    )
                    startActivity(intent)
                }
                .setNegativeButton("取消") { dialog, which -> //关闭页面或者做其他操作
                    cancelPermissionDialog()
                    finish()
                }
                .create()
        }
        mPermissionDialog!!.show()
    }

    private fun cancelPermissionDialog(){
        mPermissionDialog!!.cancel()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var hasPermissionDismiss = false
        if (REQUESTCODE == requestCode){
            for (i in grantResults.indices){
                if (grantResults[i] == -1){
                    hasPermissionDismiss = true
                    break
                }
            }
        }
        if (hasPermissionDismiss){
            //如果有没有被允许的权限
            showPermissionDialog()
        }else{
            //权限已经都通过了，可以将程序继续打开了
            Toast.makeText(this@BaseActivity,"申请成功", Toast.LENGTH_SHORT).show()
        }
    }

}

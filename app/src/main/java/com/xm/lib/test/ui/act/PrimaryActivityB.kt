package com.xm.lib.test.ui.act

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.xm.lib.common.log.BKLog
import com.xm.lib.test.R

class PrimaryActivityB : AppCompatActivity() {

    companion object {
        const val TAG = "PrimaryActivityB"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_primary_b)
        BKLog.d(TAG, "$TAG   onCreate()  PrimaryActivityA请求 ： ${intent.getStringExtra("request")}")
    }

    /**
     * 当然还可以在onCreate()就调用setResult，不过我觉得这种方法没有重写onBackPressed()方法好.
     * setResult调用必须在finish之前，查看源码便知
     */
    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("result", "PrimaryActivityB返回的结果")
        setResult(RESULT_OK, intent)
        super.onBackPressed()
    }

    override fun onStart() {
        super.onStart()
        BKLog.d(TAG, "$TAG   onStart()")
    }

    override fun onStop() {
        super.onStop()
        BKLog.d(TAG, "$TAG   onStop()")
    }

    override fun onResume() {
        super.onResume()
        BKLog.d(TAG, "$TAG   onResume()")
    }

    override fun onPause() {
        super.onPause()
        BKLog.d(TAG, "$TAG   onPause()")
    }

    override fun onDestroy() {
        super.onDestroy()
        BKLog.d(TAG, "$TAG   onDestroy()")
    }
}

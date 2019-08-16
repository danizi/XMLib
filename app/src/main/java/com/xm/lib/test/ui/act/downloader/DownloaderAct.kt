package com.xm.lib.test.ui.act.downloader

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.xm.lib.test.R

/**
 * 下载模块相关测试
 */
class DownloaderAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_downloader)
        startActivity(Intent(this, DownloaderActivity::class.java))
    }
}

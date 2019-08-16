package com.xm.lib.test.utils

import android.app.Activity
import android.content.Intent
import com.xm.lib.test.DownloaderAct
import com.xm.lib.test.ui.act.*

/**
 * 跳转
 */
object IntoTarget {
    fun start(target: String?, act: Activity?) {
        when (target) {
            "Common" -> {
                act?.startActivity(Intent(act, CommonAct::class.java))
            }
            "Component" -> {
                act?.startActivity(Intent(act, ComponentAct::class.java))
            }
            "Downloader" -> {
                act?.startActivity(Intent(act, DownloaderAct::class.java))
            }
            "Media" -> {
                act?.startActivity(Intent(act, MediaAct::class.java))
            }
            "Pay" -> {
                act?.startActivity(Intent(act, PayAct::class.java))
            }
            "Share" -> {
                act?.startActivity(Intent(act, ShareAct::class.java))
            }
            "Statistics" -> {
                act?.startActivity(Intent(act, StatisticsAct::class.java))
            }
            "Web" -> {
                act?.startActivity(Intent(act, WebAct::class.java))
            }
        }
    }
}
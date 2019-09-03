package com.xm.lib.test.utils

import android.app.Activity
import android.content.Intent
import com.xm.lib.test.ui.act.downloader.DownloaderV2Act
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
                act?.startActivity(Intent(act, DownloaderV2Act::class.java))
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
            "PrimaryTest" -> {
                act?.startActivity(Intent(act, PrimaryTestActivity::class.java))
            }

            PrimaryActivityA::class.java.simpleName -> {
                act?.startActivity(Intent(act, PrimaryActivityA::class.java))
            }

            PrimaryActivityB::class.java.simpleName -> {
                act?.startActivity(Intent(act, PrimaryActivityB::class.java))
            }

            PrimaryCache::class.java.simpleName -> {
                act?.startActivity(Intent(act, PrimaryCache::class.java))
            }

            PrimaryAni::class.java.simpleName -> {
                act?.startActivity(Intent(act, PrimaryAni::class.java))
            }

            PrimaryStandard::class.java.simpleName -> {
                act?.startActivity(Intent(act, PrimaryStandard::class.java))
            }

            PrimarySingleTop::class.java.simpleName -> {
                act?.startActivity(Intent(act, PrimarySingleTop::class.java))
            }

            PrimarySingleTask::class.java.simpleName -> {
                act?.startActivity(Intent(act, PrimarySingleTask::class.java))
            }

            PrimarySingleInstance::class.java.simpleName -> {
                act?.startActivity(Intent(act, PrimarySingleInstance::class.java))
            }
        }
    }
}
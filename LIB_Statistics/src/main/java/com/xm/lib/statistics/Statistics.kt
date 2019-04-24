package com.xm.lib.statistics

import android.content.Context
import com.tencent.bugly.crashreport.CrashReport

/**
 * 统计相关类
 */
object Statistics {

    /**
     * bugly统计
     * @ctx getApplicationContext()
     * @appId 注册时申请的APPID
     */
    fun initCrashReport(ctx: Context, appId: String) {
        CrashReport.initCrashReport(ctx, appId, true)
    }
}
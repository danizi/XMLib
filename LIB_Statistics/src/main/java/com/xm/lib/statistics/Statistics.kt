package com.xm.lib.statistics

import android.app.Activity
import android.content.Context
import android.os.Environment
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.crashreport.CrashReport

/**
 * 统计相关类
 */
object Statistics {
    /**
     * bugly统计 ps:把这个配置打开会导致无法弹出升级框，后续需要再查看下
     * @ctx getApplicationContext()
     * @appId 注册时申请的APPID
     */
    fun initCrashReport(ctx: Context, appId: String) {
        CrashReport.initCrashReport(ctx, appId, true)
    }

    /**
     * bugly版本更新
     */
    fun initUpgradeCheck(largeIconId: Int, java: Class<*>) {
        //初始化腾讯Bugly ps:需要重新进入应用才能再次检查更新
        Beta.autoInit = true
        Beta.autoCheckUpgrade = true
        Beta.upgradeCheckPeriod = 60 * 1000 * 60 * 24 * 2 //2天检查一次
        Beta.upgradeCheckPeriod = 1 //2天检查一次
        Beta.initDelay = 30 * 1000//30秒延迟检查
        Beta.initDelay = 1//30秒延迟检查
        Beta.largeIconId = largeIconId //设置通知栏大图标
        Beta.smallIconId = largeIconId //设置通知栏小图标
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        Beta.enableNotification = true
        Beta.canShowApkInfo = true
        Beta.canShowUpgradeActs.add(java as Class<out Activity>?)
        Beta.showInterruptedStrategy = false
    }

    /**
     * 初始化bugly
     */
    fun initBugly(ctx: Context, appId: String) {
        //Bugly.init(ctx, appId, BuildConfig.DEBUG)
        Bugly.init(ctx, appId, true)
    }
}
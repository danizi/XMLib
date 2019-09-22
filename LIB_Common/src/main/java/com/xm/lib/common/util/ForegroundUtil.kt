package com.xm.lib.common.util

import android.app.ActivityManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.text.TextUtils

/**
 * 判断前台窗口
 * 四种判断方式
 */
object ForegroundUtil {

    private val END_TIME = System.currentTimeMillis()
    private val TIME_INTERVAL = 7 * 24 * 60 * 60 * 1000L
    private val START_TIME = END_TIME - TIME_INTERVAL

    /**
     * 获取栈顶的应用包名
     */
    fun getForegroundActivityName(context: Context): String {
        var currentClassName = ""
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val manager = context.applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            currentClassName = manager.getRunningTasks(1)[0].topActivity.packageName
        } else {
            val initStat = getForegroundUsageStats(context, START_TIME, END_TIME)
            if (initStat != null) {
                currentClassName = initStat.packageName
            }
        }
        return currentClassName
    }

    /**
     * 判断当前应用是否在前台
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun isForegroundApp(context: Context): Boolean {
        return TextUtils.equals(getForegroundActivityName(context), context.packageName)
    }

    /**
     * 获取时间段内，
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun getTotleForegroundTime(context: Context): Long {
        val usageStats = getCurrentUsageStats(context, START_TIME, END_TIME)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            usageStats?.totalTimeInForeground ?: 0
        } else 0
    }

    /**
     * 获取记录前台应用的UsageStats对象
     */
    private fun getForegroundUsageStats(context: Context, startTime: Long, endTime: Long): UsageStats? {
        var usageStatsResult: UsageStats? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val usageStatses = getUsageStatsList(context, startTime, endTime)
            if (usageStatses == null || usageStatses.isEmpty()) return null
            for (usageStats in usageStatses) {
                if (usageStatsResult == null || usageStatsResult.lastTimeUsed < usageStats.lastTimeUsed) {
                    usageStatsResult = usageStats
                }
            }
        }
        return usageStatsResult
    }

    /**
     * 获取记录当前应用的UsageStats对象
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun getCurrentUsageStats(context: Context, startTime: Long, endTime: Long): UsageStats? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val usageStatses = getUsageStatsList(context, startTime, endTime)
            if (usageStatses == null || usageStatses.isEmpty()) return null
            for (usageStats in usageStatses) {
                if (TextUtils.equals(usageStats.packageName, context.getPackageName())) {
                    return usageStats
                }
            }
        }
        return null
    }

    /**
     * 通过UsageStatsManager获取List<UsageStats>集合
    </UsageStats> */
    fun getUsageStatsList(context: Context, startTime: Long, endTime: Long): List<UsageStats>? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val manager = context.applicationContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            //UsageStatsManager.INTERVAL_WEEKLY，UsageStatsManager的参数定义了5个，具体查阅源码
            val usageStatses = manager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, startTime, endTime)
            if (usageStatses == null || usageStatses.size == 0) {// 没有权限，获取不到数据
                val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.applicationContext.startActivity(intent)
                return null
            }
            return usageStatses
        }
        return null
    }
}

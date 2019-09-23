package com.xm.lib.common.util

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.xm.lib.common.bean.AppInfoBean

object AppUtil {

    /**
     * 获取App信息
     */
    fun getApps(pm: PackageManager): ArrayList<AppInfoBean> {
        val appInfos = ArrayList<AppInfoBean>()
        val packageInfos = pm.getInstalledPackages(0)
        for (info in packageInfos) {
            val appInfo = AppInfoBean()
            appInfo.versionName = info.versionName
            appInfo.packageName = info.packageName
            appInfo.appName = info.applicationInfo.loadLabel(pm).toString()
            appInfo.icon = info.applicationInfo.loadIcon(pm)
            if (info.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                appInfo.system = 1 //true 非系统应用
            } else {
                appInfo.system = 0
            }

            appInfos.add(appInfo)
        }
        return appInfos
    }

    /**
     * 获取栈顶的窗口包名的方法
     */
    fun getTopActivity(context: Context): String {
        return ForegroundUtil.getForegroundActivityName(context)
    }
}
package com.xm.lib.common.bean

import android.graphics.drawable.Drawable

/**
 * app应用信息
 */
open class AppInfoBean {
    var icon: Drawable? = null
    var appName: String? = null
    var packageName: String? = null
    var versionName: String? = null
    /**
     * 0 系统 1非系统
     */
    var system: Int = 0
    /**
     * 0 false 未选中 1 true
     */
    var check: Int = 0

    override fun toString(): String {
        return "AppInfoBean(icon=$icon, appName=$appName, packageName=$packageName, versionName=$versionName, system=$system, check=$check)"
    }
}
package com.xm.lib.common.bean

import android.graphics.drawable.Drawable

/**
 * app应用信息
 */
class AppInfoBean {
    var icon: Drawable? = null
    var appName: String? = null
    var packageName: String? = null
    var versionName: String? = null
    var system: Boolean = false
    var isCheck: Boolean = false
}
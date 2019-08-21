package com.xm.lib.common.util

import android.annotation.SuppressLint
import android.os.Build

/**
 * SDK版本工具
 */
object SDKVersionUtil {

    @SuppressLint("ObsoleteSdkInt")
    fun hasFroyo(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO
    }

    @SuppressLint("ObsoleteSdkInt")
    fun hasGingerbread(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
    }

    @SuppressLint("ObsoleteSdkInt")
    fun hasHoneycomb(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
    }

    @SuppressLint("ObsoleteSdkInt")
    fun hasHoneycombMR1(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1
    }

    fun hasJellyBean(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
    }

    fun hasKitKat(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
    }

    fun hasN(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }

    fun hasM(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    fun hasL(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }


}

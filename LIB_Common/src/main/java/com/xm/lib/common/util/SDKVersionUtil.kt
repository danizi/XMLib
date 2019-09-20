package com.xm.lib.common.util

import android.annotation.SuppressLint
import android.os.Build

/**
 * SDK版本工具
 */
object SDKVersionUtil {

    @SuppressLint("ObsoleteSdkInt")
    fun hasFroyo(): Boolean {
        // >= android 2.2
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO
    }

    @SuppressLint("ObsoleteSdkInt")
    fun hasGingerbread(): Boolean {
        // >= android 2.3
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
    }

    @SuppressLint("ObsoleteSdkInt")
    fun hasHoneycomb(): Boolean {
        // >= android 3.0
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
    }

    @SuppressLint("ObsoleteSdkInt")
    fun hasHoneycombMR1(): Boolean {
        // >= android 3.1
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1
    }

    fun hasJellyBean(): Boolean {
        // >= android 4.1
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
    }

    fun hasKitKat(): Boolean {
        // AP19 >= android 4.4
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
    }

    fun hasL(): Boolean {
        // API21 >= android 5.0
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    fun hasM(): Boolean {
        // API23 >= android 6.0
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    fun hasN(): Boolean {
        // API24 android 7.0
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }


    fun hasO(): Boolean {
        // API26 android 8.0
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

    fun hasP(): Boolean {
        // API28 android 9.0
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    }
}

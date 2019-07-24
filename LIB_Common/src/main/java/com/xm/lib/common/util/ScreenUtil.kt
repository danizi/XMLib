package com.xm.lib.common.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager


/**
 * 横竖切屏幕相关
 */
object ScreenUtil {

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     */
    fun isPad(context: Context): Boolean {
        return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    /**
     * 是否竖屏
     */
    fun isPortrait(context: Context): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    /**
     * 是否横屏
     */
    fun isLandscape(context: Context): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(activity: Activity?): Int {
        val resources = activity?.resources
        val resourceId = resources?.getIdentifier("status_bar_height", "dimen", "android")!!
        return resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 获取屏幕宽高
     */
    @SuppressLint("ObsoleteSdkInt")
    fun getNormalWH(activity: Activity?): IntArray {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            val dm = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(dm)
            intArrayOf(dm.widthPixels, dm.heightPixels)
        } else {
            val point = Point()
            val wm = activity?.windowManager
            wm?.defaultDisplay?.getSize(point)
            //{宽,高}
            intArrayOf(point.x, point.y)
        }
    }

    /**
     * 隐藏状态栏
     */
    fun hideStatusBar(activity: Activity?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            val decorView = activity?.window?.decorView
            activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
            val uiOptions = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            or
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or
                            View.SYSTEM_UI_FLAG_IMMERSIVE
                            or
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            decorView?.systemUiVisibility = uiOptions
        }
    }

    /**
     * 显示状态栏
     */
    fun setDecorVisible(activity: Activity?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            val decorView = activity?.window?.decorView
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            val uiOptions = View.SYSTEM_UI_FLAG_VISIBLE
            decorView?.systemUiVisibility = uiOptions
        }
    }

    /**
     * 设置竖屏
     */
    fun setPortrait(activity: Activity?) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
    }

    /**
     * 设置横屏
     */
    fun setLandscape(activity: Activity?) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    }


    /**
     * dp 转 px
     */
    fun dip2px(context: Context, dipValue: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * px 转 dp
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }
}
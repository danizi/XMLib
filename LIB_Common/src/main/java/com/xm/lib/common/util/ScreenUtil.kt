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

    fun hideStatusBar(activity: Activity?) {
        /*隐藏状态栏*/
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

    fun setDecorVisible(activity: Activity?) {
        /*显示状态栏*/
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            val decorView = activity?.window?.decorView
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            val uiOptions = View.SYSTEM_UI_FLAG_VISIBLE
            decorView?.systemUiVisibility = uiOptions
        }
    }

    fun isPortrait(context: Context): Boolean {
        /*是否竖屏*/
        return context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    fun isLandscape(context: Context): Boolean {
        /*是否横屏*/
        return context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    fun setPortrait(activity: Activity?) {
        /*设置竖屏*/
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
    }

    fun setLandscape(activity: Activity?) {
        /*设置横屏*/
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    }

    /**
     * dip 转 px
     *
     * @return 返回dp值对应的像素值
     */
    fun dip2px(context: Context, dipValue: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }


    /**
     * px转 dp
     *
     * @return 返回像素值对应的dp值
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }
}
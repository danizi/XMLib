package com.xm.lib.common.util

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.provider.Settings

/**
 * 设置手机屏幕亮度相关
 */
object BrightnessUtil {
    fun setLandscape(activity: Activity) {
        /*设置横屏*/
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    }

    fun getScreenBrightness(context: Context?): Float {
        //获取当前屏幕的亮度
        var nowBrightnessValue = 0
        val resolver = context?.contentResolver
        try {
            nowBrightnessValue = android.provider.Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return nowBrightnessValue / 255F
    }

    fun setCurWindowBrightness(context: Context?, brightness: Int?) {
        /*
         *设置亮度
         * 程序退出之后亮度失效
         * @param brightness 0 ` 255
         */
        var brightness = brightness
        // 如果开启自动亮度，则关闭。否则，设置了亮度值也是无效的
        if (IsAutoBrightness(context)) {
            stopAutoBrightness(context)
        }

        // context转换为Activity
        val activity = context as Activity
        val lp = activity.window.attributes

        // 异常处理
        if (brightness != null) {
            if (brightness < 1) {
                brightness = 1
            }
        }
        // 异常处理
        if (brightness != null) {
            if (brightness > 255) {
                brightness = 255
            }
        }
        lp.screenBrightness = java.lang.Float.valueOf(brightness?.toFloat()!!) * (1f / 255f)
        activity.window.attributes = lp
    }

    fun setSystemBrightness(context: Context?, brightness: Float?) {
        /*
         * 设置系统亮度
         * 程序退出之后亮度依旧有效
         *  @param brightness 0 ` 255
         */
        var brightness = brightness
        // 异常处理
        if (brightness != null) {
            if (brightness < 0) {
                brightness = 0.0f
            }
        }
        // 异常处理
        if (brightness != null) {
            if (brightness > 1) {
                brightness = 1f
            }
        }
        saveBrightness(context, (brightness!! * 255).toInt())
    }

    fun stopAutoBrightness(context: Context?) {
        /*
         * 停止自动亮度调节
         */
        Settings.System.putInt(
                context?.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL)
    }

    fun IsAutoBrightness(context: Context?): Boolean {
        /*
         * 判断是否开启了自动亮度调节
         */
        var IsAutoBrightness = false
        try {
            IsAutoBrightness = Settings.System.getInt(context?.contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE) === Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }
        return IsAutoBrightness
    }

    fun startAutoBrightness(context: Context?) {
        /*
         * 开启亮度自动调节
         * 首先，需要明确屏幕亮度有两种调节模式：
         * Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC：值为1，自动调节亮度。
         * Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL：值为0，手动模式。
         * 如果需要实现亮度调节，首先需要设置屏幕亮度调节模式为手动模式。
         */
        Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
        Settings.System.putInt(context?.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC)
    }

    fun saveBrightness(context: Context?, brightness: Int?) {
        /*
         * 保存亮度设置状态
         */
        val uri = android.provider.Settings.System.getUriFor("screen_brightness")
        android.provider.Settings.System.putInt(context?.contentResolver, "screen_brightness", brightness!!)
        context?.contentResolver?.notifyChange(uri, null)
    }
}
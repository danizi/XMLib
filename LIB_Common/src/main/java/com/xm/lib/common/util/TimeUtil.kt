package com.xm.lib.common.util

import android.annotation.SuppressLint
import java.util.*

/**
 * 时间相关工具类
 */
object TimeUtil {

    /**
     * 生成指定时间格式
     * @msec 毫秒
     */
    @SuppressLint("SimpleDateFormat")
    fun unixStr(pattern: String? = "yyyy/MM/dd HH:mm:ss", msec: Long): String {
        return java.text.SimpleDateFormat(pattern).format(Date(msec)).toString()
    }

    /**
     * 生成指定时间戳
     * @return 返回毫秒
     */
    @SuppressLint("SimpleDateFormat")
    fun unix(pattern: String, source: String): Long {
        return java.text.SimpleDateFormat(pattern).parse(source).time
    }

    fun currentTime(pattern: String): String {
        return unixStr(pattern, (System.currentTimeMillis()))
    }

    @Deprecated("")
    @SuppressLint("SimpleDateFormat")
    fun hhmmss(msec: Long, pattern: String = "HH:mm:ss"): String {
        val h = msec / 1000 / 60 / 60 % 60
        return if (h > 0) {
            java.text.SimpleDateFormat(pattern).format(Date(msec))
        } else {
            java.text.SimpleDateFormat("mm:ss").format(Date(msec))
        }
    }

    @Deprecated("")
    @SuppressLint("SimpleDateFormat")
    fun yyyyMMdd(msec: Long, pattern: String = "yyyy/MM/dd"): String {
        val h = msec / 1000 / 60 / 60 % 60
        return if (h > 0) {
            java.text.SimpleDateFormat(pattern).format(Date(msec))
        } else {
            java.text.SimpleDateFormat("mm:ss").format(Date(msec))
        }
    }

    /**
     * 测试
     */
    @JvmStatic
    fun main(args: Array<String>) {
        println(unixStr("yyyy/MM/dd HH:mm:ss", 100000))
        println(unix("yyyy/MM/dd HH:mm:ss", "2019/4/16 17:43:30"))
        println(currentTime("yyyy/MM/dd HH:mm:ss"))
    }
}
package com.xm.lib.common.util

import java.util.*

/**
 * 时间相关工具类
 */
object TimeUtil {

    /**
     * 生成指定时间戳
     */
    fun unixStr() {
        println("" + java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Date(60 * 1000)))
    }

    /**
     * 生成指定时间戳
     */
    fun unix() {
        print(java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2019/4/16 17:43:30"))
    }

    /**
     * 输出hhmmsss
     */
    fun hhmmss(msec: Long, pattern: String = "HH:mm:ss"): String {
        val h = msec / 1000 / 60 / 60 % 60
        return if (h > 0) {
            java.text.SimpleDateFormat(pattern).format(Date(msec))
        } else {
            java.text.SimpleDateFormat("mm:ss").format(Date(msec))
        }
    }
}
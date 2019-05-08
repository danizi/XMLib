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

        test2()
    }

    private fun test1() {
        //输入多组a+b相加
        while (true) {
            val i = Scanner(System.`in`)
            println(i.nextInt() + i.nextInt())
        }
    }

    private fun test2() {
        while (true) {
            val i = Scanner(System.`in`)
            val a = i.nextInt()
            val b = i.nextInt()
            var count = 0
            val maxValue = 1000000
            if (a > maxValue || b > maxValue) {
                println("水仙花数量：$count")
                continue
            }

            for (i in a..b) {
                val strI = i.toString()
                val length = i.toString().length
                var temp = 0
                for (num in strI) {
                    temp += Math.pow(Integer.parseInt(num.toString()).toDouble(), length.toDouble()).toInt()
                }
                if (temp == i) {
                    count++
                    //println("水仙花：$i")
                }
            }
            println("水仙花数量：$count")
        }
    }
}
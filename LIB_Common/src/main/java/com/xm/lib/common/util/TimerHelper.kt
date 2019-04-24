package com.xm.lib.common.util

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import java.util.*

/**
 * 定时器帮助类
 */
class TimerHelper {
    //定时器
    private val handler = Handler(Looper.getMainLooper())
    //开启一个定时器显示当前播放时长
    private var task: TimerTask? = null
    private var timer: Timer? = null

    fun start(listener: OnPeriodListener?, period: Long) {
        if (task != null) {
            stop()
        }
        task = object : TimerTask() {
            @SuppressLint("SetTextI18n")
            override fun run() {
                handler.post {
                    listener?.onPeriod()
                }
            }
        }
        if (null == timer) {
            timer = Timer()
        }
        timer?.schedule(task, 0, period)
    }

    fun start(listener: OnDelayTimerListener?, delay: Long) {
        if (task != null) {
            stop()
        }
        task = object : TimerTask() {
            @SuppressLint("SetTextI18n")
            override fun run() {
                handler.post {
                    listener?.onDelayTimerFinish()
                }
            }
        }
        if (null == timer) {
            timer = Timer()
        }
        timer?.schedule(task, delay)
    }

    fun stop() {
        timer?.cancel()
        task?.cancel()
        timer = null
        task = null
    }

    interface OnPeriodListener {
        fun onPeriod()
    }

    interface OnDelayTimerListener {
        fun onDelayTimerFinish()
    }
}
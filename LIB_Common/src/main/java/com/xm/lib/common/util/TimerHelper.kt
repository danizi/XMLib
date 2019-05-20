package com.xm.lib.common.util

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import java.util.*

/**
 * 定时器帮助类
 */
class TimerHelper {
    /**
     * 主线程handler
     */
    private val handler = Handler(Looper.getMainLooper())
    /**
     * 定时器任务
     */
    private var task: TimerTask? = null
    /**
     * 定时器
     */
    private var timer: Timer? = null

    /**
     * 定时器
     */
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

    /**
     * 延时执行
     */
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

    /**
     * 停止定时器
     */
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
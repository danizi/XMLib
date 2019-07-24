package com.xm.lib.common.helper

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import java.util.*

/**
 * 定时器帮助类
 */
class TimerHelper {
    /**
     * 倒计时监听
     */
    interface OnCountDownListener {
        fun onDelayTimer(ms: Long)
        fun onComplete()
    }

    /**
     * 周期执行监听
     */
    interface OnPeriodListener {
        fun onPeriod()
    }

    /**
     * 延时执行监听
     */
    interface OnDelayTimerListener {
        fun onDelayTimerFinish()
    }

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
     * 循环执行
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
                    stop()
                }
            }
        }
        if (null == timer) {
            timer = Timer()
        }
        timer?.schedule(task, delay)
    }

    /**
     * 倒计时
     * @param listener 倒计时监听
     * @param period 倒计时周期一般是1000ms 单位毫秒
     * @param countDown 倒计时时间 单位毫秒
     */
    fun countDown(listener: OnCountDownListener?, period: Long, countDown: Long) {
        if (task != null) {
            stop()
        }
        var count = countDown / 1000
        task = object : TimerTask() {
            @SuppressLint("SetTextI18n")
            override fun run() {
                handler.post {
                    count -= 1
                    if (count > 0) {
                        listener?.onDelayTimer(count)
                    } else {
                        listener?.onComplete()
                        stop()
                    }
                }
            }
        }
        if (null == timer) {
            timer = Timer()
        }
        timer?.schedule(task, 0, period)
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
}


fun main(args: Array<String>) {
    TimerHelper().countDown(object : TimerHelper.OnCountDownListener {
        override fun onDelayTimer(ms: Long) {
            System.out.print("时间:$ms")
        }

        override fun onComplete() {
            System.out.print("完成")
        }

    }, 1000L, 5 * 1000L)
}
package com.xm.lib.media.broadcast.receiver

import android.content.Context
import android.content.Intent
import android.content.IntentFilter

/**
 * 系统时间广播接收器
 */
class SystemTimeReceiver(private val listener: OnSystemTimeListener) : BaseBroadcastReceiver() {
    override fun createIntentFilter(): IntentFilter {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_TIME_TICK)
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED)
        return intentFilter
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_TIME_TICK -> {
                listener.onTick()
            }
            Intent.ACTION_TIME_CHANGED -> {
                listener.onTimeChanged()
            }
        }
    }

    /**
     * 定时广播
     */
    interface OnSystemTimeListener {
        /**
         * 一分钟触发一次
         */
        fun onTick()

        /**
         * 系统时间改变触发
         */
        fun onTimeChanged()
    }
}
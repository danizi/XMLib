package com.xm.lib.media.broadcast.receiver

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager


/**
 * 电池广播接收器
 */
class BatteryLevelReceiver(private val listener: OnBatteryLevelListener) : BaseBroadcastReceiver() {
    companion object {
        val TYPE_BATTERY_CHANGED = "batteryChanged"
        val TYPE_BATTERY_LOW = "batteryLow"
        val TYPE_BATTERY_OKAY = "batteryOKay"
    }

    /**
     * 创建广播过滤器 记得添加权限哟
     *
     */
    override fun createIntentFilter(): IntentFilter {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED)
        intentFilter.addAction(Intent.ACTION_BATTERY_LOW)
        intentFilter.addAction(Intent.ACTION_BATTERY_OKAY)
        return intentFilter
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        var type = ""
        val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val batteryPct = level!! / scale?.toFloat()!!
        when (intent.action) {
            Intent.ACTION_BATTERY_CHANGED -> {
                type = TYPE_BATTERY_CHANGED
            }
            Intent.ACTION_BATTERY_LOW -> {
                type = TYPE_BATTERY_LOW
            }
            Intent.ACTION_BATTERY_OKAY -> {
                type = TYPE_BATTERY_OKAY
            }
        }
        listener.onBatteryLevel(type, batteryPct)
    }

    /**
     * 广播接收器
     */
    interface OnBatteryLevelListener {
        fun onBatteryLevel(type: String, batteryPct: Float)
    }
}
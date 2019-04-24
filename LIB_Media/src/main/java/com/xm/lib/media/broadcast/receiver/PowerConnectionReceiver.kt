package com.xm.lib.media.broadcast.receiver

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager


/**
 * 手机电源广播接收器
 * https://developer.android.com/training/monitoring-device-state/battery-monitoring?hl=zh-cn
 */
class PowerConnectionReceiver(private val listener: OnPowerConnectionListener?) : BaseBroadcastReceiver() {

    companion object {
        val TYPE_CHARGE_USB = "usb"
        val TYPE_CHARGE_AC = "ac"
    }

    /**
     * 广播过滤器
     */
    override fun createIntentFilter(): IntentFilter {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED)
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED)
        intentFilter.addAction(Intent.ACTION_POWER_USAGE_SUMMARY)
        return intentFilter
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL

        val chargePlug = intent?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
        val usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
        val acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC

        if (usbCharge) {
            listener?.onPowerConnection(isCharging, TYPE_CHARGE_USB)
        } else {
            listener?.onPowerConnection(isCharging, TYPE_CHARGE_AC)
        }
    }

    /**
     * 充电状态监听
     */
    interface OnPowerConnectionListener {
        /**
         * @param charger 是否正在充电
         * @param type 充电的方式 usb 充电器
         */
        fun onPowerConnection(charger: Boolean, type: String)
    }
}
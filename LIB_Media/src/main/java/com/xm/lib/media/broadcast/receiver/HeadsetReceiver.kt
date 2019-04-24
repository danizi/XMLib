package com.xm.lib.media.broadcast.receiver

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothHeadset
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

/**
 * 耳机状态广播接收器
 * 注意：分为有线和无线蓝牙耳机
 */
class HeadsetReceiver(private var listener: OnHeadsetListener?) : BaseBroadcastReceiver() {
    companion object {
        val TYPE_HEADSET = "headset"
        val TYPE_BLUETOOTH_HEADSET = "bluetoothHeadset"
    }

    /**
     * 创建广播过滤器，记得添加权限哟
     * <uses-permission android:name="android.permission.BLUETOOTH" />
     */
    override fun createIntentFilter(): IntentFilter {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG)
        intentFilter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED)
        return intentFilter
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_HEADSET_PLUG -> {
                listener?.onHeadset(TYPE_HEADSET, intent.getIntExtra("state", 0))
            }
            BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED -> {
                val adapter = BluetoothAdapter.getDefaultAdapter()
                if (BluetoothProfile.STATE_DISCONNECTED == adapter?.getProfileConnectionState(BluetoothProfile.HEADSET)) {
                    //Bluetooth headset is now disconnected
                    listener?.onHeadset(TYPE_BLUETOOTH_HEADSET, 0)
                } else {
                    listener?.onHeadset(TYPE_BLUETOOTH_HEADSET, 1)
                }
            }
        }
    }

    interface OnHeadsetListener {
        /**
         * @param type
         * @param state 0拔出 1插入
         */
        fun onHeadset(type: String, state: Int?)
    }
}
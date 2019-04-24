package com.xm.lib.media.broadcast.receiver

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.telephony.TelephonyManager

/**
 * 来电去电广播接收器
 * https://www.cnblogs.com/popfisher/p/5650969.html
 */
class PhoneStateReceiver(private val listener: OnPhoneStateListener?) : BaseBroadcastReceiver() {
    /**
     * 记得添加权限哟
     * <uses-permission android:name="android.permission.CALL_PHONE" />
     * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     */
    override fun createIntentFilter(): IntentFilter {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL)
        intentFilter.addAction("android.intent.action.PHONE_STATE")
        return intentFilter
    }

    override fun onReceive(context: Context?, intent: Intent?) {


        val resultData = this.resultData

        if (intent?.action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            // 去电，可以用定时挂断
            // 双卡的手机可能不走这个Action
            val phoneNumber = intent?.getStringExtra(Intent.EXTRA_PHONE_NUMBER)
            //Log.d(PhoneListenService.TAG, "PhoneStateReceiver EXTRA_PHONE_NUMBER: " + phoneNumber);
        } else {

            // 来电去电都会走
            // 获取当前电话状态
            val state = intent?.getStringExtra(TelephonyManager.EXTRA_STATE)

            // 获取电话号码
            val extraIncomingNumber = intent?.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING, true)) {
                listener?.onStateRinging()
            }else
            listener?.onPhoneState()
        }
    }

    interface OnPhoneStateListener {
        fun onPhoneState()
        fun onStateRinging()
    }
}
package com.xm.lib.media.broadcast.receiver

import android.content.Context
import android.content.Intent
import android.content.IntentFilter

/**
 * 息屏亮屏幕广播接收器
 */
class ScreenReceiver(private val listener: onScreenListener?) : BaseBroadcastReceiver() {

    override fun createIntentFilter(): IntentFilter {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF)
        intentFilter.addAction(Intent.ACTION_SCREEN_ON)
        intentFilter.addAction(Intent.ACTION_USER_PRESENT)
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        return intentFilter
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_SCREEN_OFF -> {
                listener?.state(false)
            }
            Intent.ACTION_SCREEN_ON -> {
                listener?.state(true)
            }
            Intent.ACTION_USER_PRESENT -> {
                listener?.userPresent()
            }
            Intent.ACTION_CLOSE_SYSTEM_DIALOGS -> {
                listener?.closeSystemDialogs()
            }
        }
    }

    interface onScreenListener {
        /**
         * 屏幕状态
         * @param isBright true 亮屏 false 息屏
         */
        fun state(isBright: Boolean)

        /**
         * 屏幕解锁
         */
        fun userPresent()

        /**
         * 锁屏
         */
        fun closeSystemDialogs()
    }
}
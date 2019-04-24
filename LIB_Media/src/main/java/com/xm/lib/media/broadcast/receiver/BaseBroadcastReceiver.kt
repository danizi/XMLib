package com.xm.lib.media.broadcast.receiver

import android.content.BroadcastReceiver
import android.content.IntentFilter

abstract class BaseBroadcastReceiver : BroadcastReceiver() {
    protected val TAG = "BroadcastReceiver"
    /**
     * 广播过滤器
     */
    abstract fun createIntentFilter(): IntentFilter
}
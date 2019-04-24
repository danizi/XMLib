package com.xm.lib.media.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.text.TextUtils
import android.util.Log
import com.xm.lib.common.log.BKLog
import java.util.concurrent.LinkedBlockingDeque

/**
 * 1 动态&静态 注册广播接收器，携带action
 * 2 发送&系统 发送动作，和intent
 * 3 广播接收器处理
 */
class BroadcastManager constructor(private val context: Context?, private val receivers: LinkedBlockingDeque<BroadcastReceiver>?) {
    companion object {
        private const val TAG = "BroadcastManager"
        fun create(context: Context?): BroadcastManager {
            if (context == null) {
                throw NullPointerException("context is null")
            }
            return BroadcastManager(context, LinkedBlockingDeque<BroadcastReceiver>())
        }
    }

    fun registerReceiver(action: String, receiver: BroadcastReceiver) {
        if (TextUtils.isEmpty(action)) {
            BKLog.e(TAG, Log.getStackTraceString(Throwable("action is null")))
            return
        }
        val filter = IntentFilter()
        filter.addAction(action)
        context?.registerReceiver(receiver, filter)
        receivers?.add(receiver)
    }

    fun registerReceiver(filter: IntentFilter?, receiver: BroadcastReceiver) {
        if (filter == null) {
            BKLog.e(TAG, Log.getStackTraceString(Throwable("filter is null")))
            return
        }
        context?.registerReceiver(receiver, filter)
        receivers?.add(receiver)
    }

    fun sendBroadcast(action: String) {
        context?.sendBroadcast(Intent(action))
    }

    fun sendBroadcast(intent: Intent) {
        context?.sendBroadcast(intent)
    }

    fun unRegisterReceiver(receiver: BroadcastReceiver? = null) {
        try {
            if (receivers == null) {
                BKLog.e(TAG, Log.getStackTraceString(Throwable("receivers is null")))
                return
            }

            for (r in receivers) {
                if (r == receiver) {
                    context?.unregisterReceiver(r)
                    return
                }
            }

            BKLog.w(TAG, "receiver 未注册过。")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun unRegisterReceiverAll() {
        try {
            if (receivers == null) {
                BKLog.e(TAG, Log.getStackTraceString(Throwable("receivers is null")))
                return
            }
            for (r in receivers) {
                context?.unregisterReceiver(r)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}
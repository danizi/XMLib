package com.xm.lib.media.broadcast.receiver

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.xm.lib.common.log.BKLog

/**
 * 网络改变广播接收器
 */
class NetworkConnectChangedReceiver(private var listener: OnNetworkConnectChangedListener?) : BaseBroadcastReceiver() {

    /**
     * ps:记得添加权限哟
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
     * <uses-permission android:name="android.permission.INTERNET"/>
     *
     */
    override fun createIntentFilter(): IntentFilter {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        return intentFilter
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            BKLog.d(TAG, "网络状态改变")
            val connectivityManager = (context as Activity).getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            val netInfo = connectivityManager?.activeNetworkInfo ?: return
            listener?.onChange(netInfo.isConnected, netInfo.type)
        }
    }

    /**
     * 网络变化监听
     */
    interface OnNetworkConnectChangedListener {
        /**
         * @param isConnect 网络是否连接
         * @param type  ConnectivityManager.TYPE_WIFI
         *               ConnectivityManager.TYPE_MOBILE
         */
        fun onChange(isConnect: Boolean, type: Int)
    }

    private fun test(netInfo: NetworkInfo) {
        if (/*netInfo.isAvailable && */netInfo.isConnected) {
            when (netInfo.type) {
                ConnectivityManager.TYPE_WIFI -> {
                    BKLog.d(TAG, "正在使用，WIFI")
                }
                ConnectivityManager.TYPE_MOBILE -> {
                    BKLog.d(TAG, "正在使用，手机流量")
                }
            }
        } else {
            BKLog.w(TAG, "网络不可用")
        }
    }

}
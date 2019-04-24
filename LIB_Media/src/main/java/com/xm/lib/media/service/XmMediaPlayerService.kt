package com.xm.lib.media.service

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.widget.RemoteViews
import com.xm.lib.common.log.BKLog
import com.xm.lib.media.R
import com.xm.lib.media.base.XmVideoView
import com.xm.lib.media.broadcast.BroadcastManager
import com.xm.lib.media.test.MainActivity


class XmMediaPlayerService : Service() {

    var binder: XmMediaPlayerBinder? = null

    @SuppressLint("WrongConstant")
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate() {
        super.onCreate()

        /**
         * 自定义remoteViews 点击按钮触发消息处理
         */
        BroadcastManager.create(baseContext).registerReceiver("com.media.next", object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == "com.media.next") {
                    BKLog.e("com.media.next")
                    binder?.next()
                }
            }
        })

        /**
         * 权限
         * <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
         * android 高版本必须要添加一个notificationChannel渠道
         */
        val CHANNEL_ONE_ID = "common.xm.com.xmcommon"
        val CHANNEL_ONE_NAME = "Channel One"
        val notificationChannel = NotificationChannel(CHANNEL_ONE_ID,
                CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.setShowBadge(true)
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(notificationChannel)

        val activityIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, 0)

        val remoteViews = RemoteViews(packageName, R.layout.media_notification)

        val i = Intent("com.media.next")
        val pendingIntent1 = PendingIntent.getBroadcast(baseContext, 0, i, PendingIntent.FLAG_UPDATE_CURRENT)
        remoteViews.setOnClickPendingIntent(R.id.iv_next, pendingIntent1)

        val notification = Notification.Builder(application)
                .setContent(remoteViews)
                .setTicker("正在播放")
                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                .setSmallIcon(R.mipmap.ic_launcher)
                .setChannelId(CHANNEL_ONE_ID)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .build()

//        startForeground(1, notification)

        val mNotifyMgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotifyMgr.notify(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? {
        if (binder == null)
            binder = XmMediaPlayerBinder(this)
        return binder
    }

    class XmMediaPlayerBinder(private val xmMediaPlayerService: XmMediaPlayerService) : Binder() {
        var xmVideoView: XmVideoView? = null

        fun getService(): XmMediaPlayerService {
            return xmMediaPlayerService
        }

        fun next() {
            xmVideoView?.next()
        }

        fun pre() {
            xmVideoView?.pre()
        }

        fun pause() {

        }

        fun start() {

        }
    }
}
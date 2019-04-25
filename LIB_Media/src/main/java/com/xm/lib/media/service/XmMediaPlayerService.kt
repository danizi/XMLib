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
    private val TAG = "XmMediaPlayerService"
    var binder: XmMediaPlayerBinder? = null

    @SuppressLint("WrongConstant")
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate() {
        super.onCreate()

        /**
         * 自定义remoteViews 点击按钮触发消息处理
         */
        val remoteViews = RemoteViews(packageName, R.layout.media_notification)
        BroadcastManager.create(baseContext).registerReceiver("com.media.next", object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    "com.media.next" -> {
                        BKLog.d(TAG, "下一首")
                        binder?.next()
                    }
                    "com.media.pre" -> {
                        BKLog.d(TAG, "上一首")
                        binder?.pre()
                    }
                    "com.media.pause" -> {
                        BKLog.d(TAG, "暂停")
                        remoteViews.setImageViewResource(R.id.iv_action, R.mipmap.media_control_play)
                        binder?.pause()
                    }
                    "com.media.start" -> {
                        BKLog.d(TAG, "播放")
                        remoteViews.setImageViewResource(R.id.iv_action, R.mipmap.media_control_pause)
                        binder?.start()
                    }
                }
                if (intent?.action == "com.media.next") {

                    binder?.next()
                }
            }
        })

        /**
         * 权限
         * <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
         * android 高版本必须要添加一个notificationChannel渠道
         */
        //Android6.0以上需要添加渠道
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

        //点击事件

        val nextAction = Intent("com.media.next")
        val preAction = Intent("com.media.pre")
        val pauseAction = Intent("com.media.pause")
        val startAction = Intent("com.media.start")
        val nextPendingIntent = PendingIntent.getBroadcast(baseContext, 0, nextAction, PendingIntent.FLAG_UPDATE_CURRENT)
        val prePendingIntent = PendingIntent.getBroadcast(baseContext, 0, preAction, PendingIntent.FLAG_UPDATE_CURRENT)
        val pausePendingIntent = PendingIntent.getBroadcast(baseContext, 0, pauseAction, PendingIntent.FLAG_UPDATE_CURRENT)
        val startPendingIntent = PendingIntent.getBroadcast(baseContext, 0, startAction, PendingIntent.FLAG_UPDATE_CURRENT)
        remoteViews.setOnClickPendingIntent(R.id.iv_next, nextPendingIntent)
        remoteViews.setOnClickPendingIntent(R.id.iv_pre, prePendingIntent)
        remoteViews.setOnClickPendingIntent(R.id.iv_action, pausePendingIntent)
        //remoteViews.setOnClickPendingIntent(R.id.iv_action, startPendingIntent)

        //显示通知
        val activityIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, 0)
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
            xmVideoView?.pause()
        }

        fun start() {
            xmVideoView?.start()
        }
    }
}
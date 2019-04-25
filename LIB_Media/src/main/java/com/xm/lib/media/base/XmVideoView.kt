package com.xm.lib.media.base

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.media.SubtitleData
import android.os.IBinder
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import com.xm.lib.common.log.BKLog
import com.xm.lib.media.attachment.BaseAttachmentView
import com.xm.lib.media.base.IXmMediaPlayer.Companion.TAG
import com.xm.lib.media.broadcast.BroadcastManager
import com.xm.lib.media.broadcast.receiver.*
import com.xm.lib.media.event.GestureObservable
import com.xm.lib.media.event.PhoneStateObservable
import com.xm.lib.media.event.PlayerObservable
import com.xm.lib.media.service.XmMediaPlayerService
import com.xm.lib.media.utils.GestureHelper
import java.io.IOException
import java.util.concurrent.ConcurrentLinkedQueue

class XmVideoView : FrameLayout {

    var mediaPlayer: XmMediaPlayer? = null //播放器
    private var attachmentViews: ConcurrentLinkedQueue<BaseAttachmentView>? = ConcurrentLinkedQueue() //附着页面集合
    private var urls: ConcurrentLinkedQueue<String>? = ConcurrentLinkedQueue() //保存播放记录
    private var surfaceView: SurfaceView? = null //画布
    private var sh: SurfaceHolder? = null //画布Holder
    private var autoPlay = false //是否自动播放
    private var playerObservable: PlayerObservable? = null //播放器相关监听
    private var phoneStateObservable: PhoneStateObservable? = null //广播相关监听
    private var gestureObservable: GestureObservable? = null //手势相关监听
    private var broadcastManager: BroadcastManager? = null // 广播管理类
    private var gestureHelper: GestureHelper? = null //手势帮助类
    private var pos = 0L //记录播放的位置

    var binder: XmMediaPlayerService.XmMediaPlayerBinder? = null//服务对外提供接口
    var xmMediaPlayerService: XmMediaPlayerService? = null //播放服务

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context) : super(context)

    init {
        initMediaPlayer()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (sh != null) { //只要播放过则就手势处理
            return gestureHelper?.onTouchEvent(event)!!
        }
        return super.onTouchEvent(event)
    }

    private fun initMediaPlayer() {

        if (playerObservable == null) {
            playerObservable = PlayerObservable()
        }
        if (phoneStateObservable == null) {
            phoneStateObservable = PhoneStateObservable()
        }
        if (gestureObservable == null) {
            gestureObservable = GestureObservable()
        }
        if (mediaPlayer == null) {
            mediaPlayer = XmMediaPlayer()
            initMediaPlayerListener()
        }

        //手势监听
        gestureHelper = GestureHelper(context)
        gestureHelper?.setOnGestureListener(object : GestureHelper.OnGestureListener {
            override fun onDown() {
                gestureObservable?.notifyObserversDown()
            }

            override fun onDownUp() {
                gestureObservable?.notifyObserversDownUp()
            }

            override fun onClick() {
                gestureObservable?.notifyObserversClick()
            }

            override fun onHorizontal(present: Int) {
                gestureObservable?.notifyObserversHorizontal(present)
            }

            override fun onVertical(type: String, present: Int) {
                gestureObservable?.notifyObserversVertical(type, present)
            }

            override fun onDoubleClick() {
                gestureObservable?.notifyObserversDoubleClick()
            }

            override fun onScaleEnd(scaleFactor: Float) {
                gestureObservable?.notifyObserversScaleEnd(scaleFactor)
            }
        })

        //各种系统广播监听
        if (broadcastManager == null) {
            broadcastManager = BroadcastManager.create(context)
        }
        //电量状态
        val batteryLevelReceiver = BatteryLevelReceiver(object : BatteryLevelReceiver.OnBatteryLevelListener {
            override fun onBatteryLevel(type: String, batteryPct: Float) {
                BKLog.d(TAG, "onBatteryLevel type:$type batteryPct:$batteryPct")
                phoneStateObservable?.notifyObserversBatteryLevel(type, batteryPct)
            }
        })
        //耳机状态
        val headsetReceiver = HeadsetReceiver(object : HeadsetReceiver.OnHeadsetListener {
            override fun onHeadset(type: String, state: Int?) {
                BKLog.d(TAG, "onHeadset type:$type state:$state")
                phoneStateObservable?.notifyObserversHeadset(type, state)
            }
        })
        //网络状态
        val networkConnectChangedReceiver = NetworkConnectChangedReceiver(object : NetworkConnectChangedReceiver.OnNetworkConnectChangedListener {
            override fun onChange(isConnect: Boolean, type: Int) {
                BKLog.d(TAG, "onChange isConnect:$isConnect type:$type")
                phoneStateObservable?.notifyObserversNetworkConnectChange(isConnect, type)
            }
        })
        //手机来电去电状态
        val phoneStateReceiver = PhoneStateReceiver(object : PhoneStateReceiver.OnPhoneStateListener {
            override fun onPhoneState() {
                BKLog.d(TAG, "onPhoneState")
                phoneStateObservable?.notifyObserversPhoneState()
            }

            override fun onStateRinging() {
                BKLog.d(TAG, "onStateRinging")
                phoneStateObservable?.notifyObserversStateRinging()
            }
        })
        //是否充电状态
        val powerConnectionReceiver = PowerConnectionReceiver(object : PowerConnectionReceiver.OnPowerConnectionListener {
            override fun onPowerConnection(charger: Boolean, type: String) {
                phoneStateObservable?.notifyObserversPowerConnection(charger, type)
            }
        })
        broadcastManager?.registerReceiver(batteryLevelReceiver.createIntentFilter(), batteryLevelReceiver)
        broadcastManager?.registerReceiver(headsetReceiver.createIntentFilter(), headsetReceiver)
        broadcastManager?.registerReceiver(networkConnectChangedReceiver.createIntentFilter(), networkConnectChangedReceiver)
        broadcastManager?.registerReceiver(phoneStateReceiver.createIntentFilter(), phoneStateReceiver)
        broadcastManager?.registerReceiver(powerConnectionReceiver.createIntentFilter(), powerConnectionReceiver)

        viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                BKLog.d(TAG, "XmVideoView 渲染完成")
                gestureHelper?.bind(this@XmVideoView)
                for (attachment in attachmentViews?.iterator()!!) {
                    if (attachment.parent != null) {//android.view.ViewGroup$LayoutParams cannot be cast to android.view.ViewGroup$MarginLayoutParams
                        (attachment.parent as ViewGroup).removeView(attachment)
                    }
                    attachment.bind(this@XmVideoView)
                }
                viewTreeObserver.removeGlobalOnLayoutListener(this)
            }
        })
    }

    private fun initMediaPlayerListener() {
        mediaPlayer?.setOnVideoSizeChangedListener(object : OnVideoSizeChangedListener {
            override fun onVideoSizeChanged(mp: IXmMediaPlayer, width: Int, height: Int, sar_num: Int, sar_den: Int) {
                playerObservable?.notifyObserversVideoSizeChanged(mp, width, height, sar_num, sar_den)
            }
        })

        mediaPlayer?.setOnErrorListener(object : OnErrorListener {
            override fun onError(mp: IXmMediaPlayer, what: Int, extra: Int): Boolean {
                playerObservable?.notifyObserversError(mp, what, extra)
                return false
            }
        })

        mediaPlayer?.setOnInfoListener(object : OnInfoListener {
            override fun onInfo(mp: IXmMediaPlayer, what: Int, extra: Int): Boolean {
                playerObservable?.notifyObserversInfo(mp, what, extra)
                return false
            }
        })

        mediaPlayer?.setOnPreparedListener(object : OnPreparedListener {
            override fun onPrepared(mp: IXmMediaPlayer) {
                if (autoPlay) {
                    mediaPlayer?.seekTo(pos.toInt())
                    mediaPlayer?.start()
                }
                playerObservable?.notifyObserversPrepared(mp)
            }
        })

        mediaPlayer?.setOnBufferingUpdateListener(object : OnBufferingUpdateListener {
            override fun onBufferingUpdate(mp: IXmMediaPlayer, percent: Int) {
                playerObservable?.notifyObserversBufferingUpdate(mp, percent)
            }
        })

        mediaPlayer?.setOnSeekCompleteListener(object : OnSeekCompleteListener {
            override fun onSeekComplete(mp: IXmMediaPlayer) {
                playerObservable?.notifyObserversSeekComplete(mp)
            }
        })

        mediaPlayer?.setOnCompletionListener(object : OnCompletionListener {
            override fun onCompletion(mp: IXmMediaPlayer) {
                playerObservable?.notifyObserversCompletion(mp)
            }
        })

        mediaPlayer?.setOnSubtitleDataListener(object : OnSubtitleDataListener {
            override fun onSubtitleData(mp: IXmMediaPlayer, data: SubtitleData) {
                playerObservable?.notifyObserversSubtitleData(mp, data)
            }
        })

        mediaPlayer?.setOnDrmInfoListener(object : OnDrmInfoListener {
            override fun onDrmInfo(mp: IXmMediaPlayer, drmInfo: MediaPlayer.DrmInfo) {
                playerObservable?.notifyObserversDrmInfo(mp, drmInfo)
            }
        })

        mediaPlayer?.setOnMediaCodecSelectListener(object : OnMediaCodecSelectListener {
            override fun onMediaCodecSelect(mp: IXmMediaPlayer, mimeType: String, profile: Int, level: Int): String {
                playerObservable?.notifyObserversMediaCodecSelect(mp, mimeType, profile, level)
                return ""
            }
        })

        mediaPlayer?.setOnControlMessageListener(object : OnControlMessageListener {
            override fun onControlResolveSegmentUrl(mp: IXmMediaPlayer, segment: Int): String {
                playerObservable?.notifyObserversControlResolveSegmentUrl(mp, segment)
                return ""
            }
        })
    }

    fun addPlayerObserver(attachment: BaseAttachmentView?) {
        playerObservable?.addObserver(attachment?.observer)
    }

    fun addGestureObserver(attachment: BaseAttachmentView?) {
        gestureObservable?.addObserver(attachment?.gestureObserver)
    }

    fun addPhoneStateObserver(attachment: BaseAttachmentView?) {
        phoneStateObservable?.addObserver(attachment?.phoneObserver)
    }

    fun bindAttachmentView(attachment: BaseAttachmentView?) {
        /*添加在播放器附着的页面*/
        if (attachment != null) {
            attachmentViews?.add(attachment)
        } else {
            BKLog.e(TAG, "attachment is null")
        }
    }

    fun unBindAttachmentView(attachment: BaseAttachmentView?) {
        if (attachment != null) {
            //this.removeView(attachment)
            //attachmentViews?.remove(attachment)
            playerObservable?.deleteObserver(attachment.observer)
            gestureObservable?.deleteObserver(attachment.gestureObserver)
            phoneStateObservable?.deleteObserver(attachment.phoneObserver)
            attachment.unBind()
        } else {
            BKLog.e(TAG, "attachment is null")
        }
    }

    fun start(url: String, autoPlay: Boolean = false) {
        /*异步准备播放*/
        this.autoPlay = autoPlay
        if (surfaceView == null || sh == null) {
            //添加画布
            surfaceView = SurfaceView(context)
            surfaceView?.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            sh = surfaceView?.holder
            sh?.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                    BKLog.d(TAG, "surfaceChanged width:$width height:$height")
                }

                override fun surfaceDestroyed(holder: SurfaceHolder?) {
                    BKLog.d(TAG, "surfaceDestroyed")
                }

                override fun surfaceCreated(holder: SurfaceHolder?) {
                    if (!TextUtils.isEmpty(url)) {
                        if (mediaPlayer == null) {
                            initMediaPlayer()
                        } else {
                            mediaPlayer?.reset()
                        }
                        mediaPlayer?.setDisplay(holder)
                        try {
                            mediaPlayer?.setDataSource(url)
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        mediaPlayer?.prepareAsync()
                    } else {
                        BKLog.e(TAG, "url is null")
                    }
                    BKLog.d(IXmMediaPlayer.TAG, "surfaceCreated")
                }
            })
            // surfaceView?.visibility= View.GONE  设置画布隐藏就无法播放了
            addView(surfaceView)
        } else {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.setDisplay(sh)
            mediaPlayer?.setDataSource(url)
            mediaPlayer?.prepareAsync()
        }
//        if (TextUtils.isEmpty(url)) {
//            throw NullPointerException("url is null")
//        }
//
//        if (mediaPlayer == null) {
//            initMediaPlayer()
//            initMediaPlayerListener()
//        }
//
//        if (surfaceView == null) {
//            surfaceView = SurfaceView(context)
//            surfaceView?.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//            sh = surfaceView?.holder
//            sh?.addCallback(object : SurfaceHolder.Callback {
//                override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
//                    BKLog.d(TAG, "surfaceChanged width:$width height:$height")
//                }
//
//                override fun surfaceDestroyed(holder: SurfaceHolder?) {
//                    BKLog.d(TAG, "surfaceDestroyed")
//                }
//
//                override fun surfaceCreated(holder: SurfaceHolder?) {
//                    try {
//                        mediaPlayer?.setDisplay(sh)
//                        mediaPlayer?.setDataSource(url)
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                    mediaPlayer?.prepareAsync()
//                    BKLog.d(IXmMediaPlayer.TAG, "surfaceCreated")
//                }
//            })
//            // surfaceView?.visibility= View.GONE  设置画布隐藏就无法播放了
//            addView(surfaceView)
//        }
//
//        mediaPlayer?.stop()
//        mediaPlayer?.reset()
//        mediaPlayer?.setDataSource(url)
//        mediaPlayer?.prepareAsync()
    }

    fun addPlaylists(url: String) {
        urls?.add(url)
    }

    fun next() {
        start("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4")
    }

    fun pre() {
        start("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4")
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun start() {
        mediaPlayer?.start()
    }

    fun onPause() {
        pos = mediaPlayer?.getCurrentPosition()!!
        val conn = object : ServiceConnection {

            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                Log.d("", "onServiceConnected")
                binder = service as XmMediaPlayerService.XmMediaPlayerBinder
                binder?.xmVideoView = this@XmVideoView
                xmMediaPlayerService = binder?.getService()
            }

            override fun onServiceDisconnected(name: ComponentName) {
                Log.d("", "onServiceDisconnected")
            }

            override fun onBindingDied(name: ComponentName) {
                Log.d("", "onBindingDied")
            }

            override fun onNullBinding(name: ComponentName) {
                Log.d("", "onNullBinding")
            }
        }
        context?.bindService(Intent(context, XmMediaPlayerService::class.java), conn, Context.BIND_AUTO_CREATE)
    }

    fun onResume() {}

    fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
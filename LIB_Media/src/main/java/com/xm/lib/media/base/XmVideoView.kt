package com.xm.lib.media.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.media.MediaPlayer
import android.media.SubtitleData
import android.os.IBinder
import android.support.constraint.ConstraintLayout
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ScreenUtil
import com.xm.lib.common.util.TimerHelper
import com.xm.lib.media.R
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
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ConcurrentMap

/**
 * 播放器View
 */
class XmVideoView : FrameLayout {
    /**
     * 播放器狀態
     */
    //var mediaState: MediaState? = null
    /**
     * 播放器
     */
    var mediaPlayer: XmMediaPlayer? = null
    /**
     * 播放器附着页面
     */
    private var attachmentViews: ConcurrentLinkedQueue<BaseAttachmentView>? = ConcurrentLinkedQueue() //附着页面集合
    var attachmentViewMaps: ConcurrentMap<String, BaseAttachmentView>? = ConcurrentHashMap<String, BaseAttachmentView>() //附着页面集合
    /**
     * 保存播放记录
     */
    @Deprecated("")
    private var urls: ConcurrentLinkedQueue<String>? = ConcurrentLinkedQueue()
    /**
     * 画布
     */
    private var surfaceView: SurfaceView? = null
    /**
     * 画布Holder
     */
    private var sh: SurfaceHolder? = null
    /**
     * 是否自动播放标志
     */
    private var autoPlay = false
    /**
     * 播放器相关监听
     */
    private var playerObservable: PlayerObservable? = null
    /**
     * 广播相关监听
     */
    private var phoneStateObservable: PhoneStateObservable? = null
    /**
     * 手势相关监听
     */
    private var gestureObservable: GestureObservable? = null
    /**
     * 手势帮助类
     */
    private var gestureHelper: GestureHelper? = null
    /**
     * 广播管理类
     */
    private var broadcastManager: BroadcastManager? = null
    /**
     * 记录播放的位置
     */
    private var pos = 0L
    /**
     * 服务对外提供接口
     */
    var binder: XmMediaPlayerService.XmMediaPlayerBinder? = null
    /**
     * 播放服务
     */
    var xmMediaPlayerService: XmMediaPlayerService? = null

    /**
     * 是否完成
     */
    var isComplete = false

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context) : super(context)

    init {
        initMediaPlayer()
    }

    /**
     * 播放器初始化
     */
    @SuppressLint("ResourceAsColor")
    private fun initMediaPlayer() {

        //设置背景颜色
        setBackgroundColor(Color.parseColor("#000000"))

        //播放器观察者实例化
        if (playerObservable == null) {
            playerObservable = PlayerObservable()
        }

        //手机状态观察者实例化
        if (phoneStateObservable == null) {
            phoneStateObservable = PhoneStateObservable()
        }

        //手势观察者实例化
        if (gestureObservable == null) {
            gestureObservable = GestureObservable()
        }

        //播放器实例化
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


        //判断视图是否渲染完成
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

    /**
     * 播放器监听
     */
    private fun initMediaPlayerListener() {
        mediaPlayer?.setOnVideoSizeChangedListener(object : OnVideoSizeChangedListener {
            override fun onVideoSizeChanged(mp: IXmMediaPlayer, width: Int, height: Int, sar_num: Int, sar_den: Int) {
                playerObservable?.notifyObserversVideoSizeChanged(mp, width, height, sar_num, sar_den)
            }
        })

        mediaPlayer?.setOnErrorListener(object : OnErrorListener {
            override fun onError(mp: IXmMediaPlayer, what: Int, extra: Int): Boolean {
                //mediaState = MediaState.MEDIA_STATE_ERROR
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
                //mediaState = MediaState.MEDIA_STATE_PREPARED
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
                //mediaState = MediaState.MEDIA_STATE_COMPLETION
                playerObservable?.notifyObserversSeekComplete(mp)
            }
        })

        mediaPlayer?.setOnCompletionListener(object : OnCompletionListener {
            override fun onCompletion(mp: IXmMediaPlayer) {
                playerObservable?.notifyObserversCompletion(mp)
                isComplete = true
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

    /**
     * 播放器事件处理
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (sh != null) { //只要播放过则就手势处理
            return gestureHelper?.onTouchEvent(event)!!
        }
        return super.onTouchEvent(event)
    }

    /**
     * 添加播放器观察者
     * @param attachment 附着View
     */
    fun addPlayerObserver(attachment: BaseAttachmentView?) {
        playerObservable?.addObserver(attachment?.observer)
    }

    /**
     * 添加手势观察者
     * @param attachment 附着View
     */
    fun addGestureObserver(attachment: BaseAttachmentView?) {
        gestureObservable?.addObserver(attachment?.gestureObserver)
    }

    /**
     * 添加手机状态观察者
     * @param attachment 附着View
     */
    fun addPhoneStateObserver(attachment: BaseAttachmentView?) {
        phoneStateObservable?.addObserver(attachment?.phoneObserver)
    }

    /**
     * 绑定附着view
     * @param attachment 附着View
     */
    fun bindAttachmentView(attachment: BaseAttachmentView?, attachmentName: String? = "") {
        /*添加在播放器附着的页面*/
        if (attachment != null) {
            attachmentViews?.add(attachment)
            attachmentViewMaps?.put(attachmentName, attachment)
        } else {
            BKLog.e(TAG, "attachment is null")
        }
    }

    /**
     * 解绑附着View,删除观察者，删除附着View
     */
    fun unBindAttachmentView(attachment: BaseAttachmentView?, attachmentName: String? = "") {
        if (attachment != null) {
            playerObservable?.deleteObserver(attachment.observer)
            gestureObservable?.deleteObserver(attachment.gestureObserver)
            phoneStateObservable?.deleteObserver(attachment.phoneObserver)
            attachment.unBind()
            attachmentViews?.remove(attachment)
            attachmentViewMaps?.remove(attachmentName)
        } else {
            BKLog.e(TAG, "attachment is null")
        }
    }

    /**
     * 播放
     * @param url 播放地址
     * @param autoPlay 是否自动播放
     * @param pos 指定位置播放，默认为0(单位毫秒)
     */
    fun start(url: String?, autoPlay: Boolean = false, pos: Int? = 0) {
        /*异步准备播放*/
        isComplete = false
        this.pos = pos?.toLong()!!
        this.autoPlay = autoPlay
        if (surfaceView == null || sh == null) {
            //添加画布
            surfaceView = SurfaceView(context)
            surfaceView?.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            sh = surfaceView?.holder
            sh?.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                    TimerHelper().start(object :TimerHelper.OnDelayTimerListener{
                        override fun onDelayTimerFinish() {
                            if (ScreenUtil.isLandscape(context)) {
                                //横屏
                                val w = ScreenUtil.getNormalWH(context as Activity)[0]
                                val h = ScreenUtil.getNormalWH(context as Activity)[1]
                                val surfaceViewW = (ratio * height).toInt()
                                val margin = (w - surfaceViewW) / 2
                                val lp = FrameLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.MATCH_PARENT,
                                        RelativeLayout.LayoutParams.MATCH_PARENT)
                                lp.setMargins(margin, 0, margin, 0)
                                surfaceView?.layoutParams = lp
                                val lp2 = ConstraintLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.MATCH_PARENT,
                                        RelativeLayout.LayoutParams.MATCH_PARENT)
                                this@XmVideoView.layoutParams = lp2
                                BKLog.d(TAG, "横屏 surfaceChanged width:$width height:$height")
                                BKLog.d(TAG, "横屏 surfaceChanged w:$w h:$h")
                            } else if (ScreenUtil.isPortrait(context)) {
                                //竖屏
                                val w = ScreenUtil.getNormalWH(context as Activity)[0]
                                val h = ScreenUtil.getNormalWH(context as Activity)[1]
                                val lp = FrameLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.MATCH_PARENT,
                                        RelativeLayout.LayoutParams.MATCH_PARENT)
                                lp.setMargins(0, 0, 0, 0)
                                surfaceView?.layoutParams?.width = w
                                surfaceView?.layoutParams?.height = height
                                surfaceView?.layoutParams = lp
                                val lp2 = ConstraintLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.MATCH_PARENT,
                                        height)
                                this@XmVideoView.layoutParams = lp2
                                BKLog.d(TAG, "竖屏 surfaceChanged width:$width height:$height")
                                BKLog.d(TAG, "竖屏 surfaceChanged w:$w h:$h")
                            }
                        }
                    },500)
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
    }

    private var ratio = 1f
    /**
     * 设置画布大小
     * @param ratio 分辨率 宽：高
     */
    fun setSufaceViewSize(ratio: Float? = 1F) {
        //val ratio = (mediaPlayer?.getVideoWidth()!! / mediaPlayer?.getVideoHeight()!!).toFloat()
        val ratio = (16f / 9f)
        this.ratio = ratio
        val h = ScreenUtil.getNormalWH(context as Activity)[1]
        surfaceView?.layoutParams?.width = (ratio * h).toInt()
    }

    /**
     * 设置播放列表信息
     */
    @Deprecated("在控制器中处理", ReplaceWith("start(\"http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4\")"))
    fun addPlaylists(url: String) {
        urls?.add(url)
    }

    /**
     * 播放下一集视频
     */
    @Deprecated("在控制器中处理", ReplaceWith("start(\"http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4\")"))
    fun next() {
        start("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4")
    }

    /**
     * 播放上一集视频
     */
    @Deprecated("在控制器中处理", ReplaceWith("start(\"http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4\")"))
    fun pre() {
        start("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4")
    }

    /**
     * 暂停视频
     */
    fun pause() {
        mediaPlayer?.pause()
    }

    /**
     * 播放视频
     */
    fun start() {
        mediaPlayer?.start()
    }

    /**
     * 窗口不可见处理
     */
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
        //context?.bindService(Intent(context, XmMediaPlayerService::class.java), conn, Context.BIND_AUTO_CREATE)
    }

    /**
     * 窗口可见处理
     */
    fun onResume() {

    }

    /**
     * 窗口销毁处理
     */
    fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
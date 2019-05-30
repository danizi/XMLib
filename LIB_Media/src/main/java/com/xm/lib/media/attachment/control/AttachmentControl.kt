package com.xm.lib.media.attachment.control

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Toast
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ScreenUtil
import com.xm.lib.media.R
import com.xm.lib.media.attachment.BaseAttachmentView
import com.xm.lib.media.attachment.control.viewholder.LandscapeViewHolder
import com.xm.lib.media.attachment.control.viewholder.PortraitViewHolder
import com.xm.lib.media.base.IXmMediaPlayer
import com.xm.lib.media.base.XmVideoView
import com.xm.lib.media.event.GestureObserver
import com.xm.lib.media.event.PhoneStateObserver
import com.xm.lib.media.event.PlayerObserver


class AttachmentControl(context: Context?) : BaseAttachmentView(context) {

    var controlViewHolder: ControlViewHolder? = null
    private val period: Int = 1000
    private val delay: Int = 5000

    companion object {
        const val TAG = "AttachmentControl"
        const val PORTRAIT = "portrait"
        const val LANDSCAPE = "landscape"
    }

    init {
        observer = object : PlayerObserver {
            override fun onPrepared(mp: IXmMediaPlayer) {
                super.onPrepared(mp)
                xmVideoView?.bringChildToFront(this@AttachmentControl)
            }


            override fun onSeekComplete(mp: IXmMediaPlayer) {
                super.onSeekComplete(mp)
                controlViewHolder?.hideLoading()
            }

            override fun onInfo(mp: IXmMediaPlayer, what: Int, extra: Int) {
                super.onInfo(mp, what, extra)
                if (what == IXmMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    controlViewHolder?.setActionResID(R.mipmap.media_control_pause)
                    //controlViewHolder?.progressTimerStart(period.toLong())
                }
                when (what) {
                    IXmMediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                        controlViewHolder?.showLoading()
                    }
                    IXmMediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                        controlViewHolder?.hideLoading()
                    }
                }
            }

            override fun onBufferingUpdate(mp: IXmMediaPlayer, percent: Int) {
                super.onBufferingUpdate(mp, percent)
                controlViewHolder?.secondaryProgress(percent)
            }
        }
        gestureObserver = object : GestureObserver {
            private var horizontalSlidePos: Long = -1  //记录手指滑动时，第一次播放进度。主要在回调中使用

            override fun onClick() {
                super.onClick()
                controlViewHolder?.isClick = true
                controlViewHolder?.showOrHideControlView()
            }

            override fun onDownUp() {
                super.onDownUp()
                horizontalSlidePos = -1
                //手指滑动设置进度释放处理进度
                if (controlViewHolder?.isHorizontalSlide!!) {
                    controlViewHolder?.isHorizontalSlide = false
                    controlViewHolder?.hideControlView()
                    controlViewHolder?.horizontalSlideStopSeekTo()
                    controlViewHolder?.progressTimerStart(period.toLong())
                    controlViewHolder?.horizontalSlideStopSeekTo()
                }

                if (controlViewHolder?.isClick!!) {
                    controlViewHolder?.isClick = false
                    controlViewHolder?.startDelayTimerHideControlView(delay)
                }
            }

            override fun onScaleEnd(scaleFactor: Float) {
                super.onScaleEnd(scaleFactor)
                if (context == null) {
                    BKLog.e(TAG, "context is null")
                    return
                }

                if (ScreenUtil.isLandscape(context)) {

                }
            }

            override fun onVertical(type: String, present: Int) {
                super.onVertical(type, present)
                if (controlViewHolder?.isControlViewShow!!) {
                    if (present > 0) {
                        controlViewHolder?.hidePlayListAni()
                    } else {
                        controlViewHolder?.showPlayListAni()
                    }
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onHorizontal(present: Int) {
                super.onHorizontal(present)
                //记录手指滑动时播放器的位置 ps:只记录一次
                if (horizontalSlidePos == -1L) {
                    horizontalSlidePos = xmVideoView?.mediaPlayer?.getCurrentPosition()!!
                }
                //手指处于水平滑动中
                controlViewHolder?.isHorizontalSlide = true

                //停止所有的计时器
                controlViewHolder?.stopDelayTimerHideControlView()
                controlViewHolder?.progressTimerStop()

                //显示控制器界面
                controlViewHolder?.updateProgress(horizontalSlidePos.toInt() + present * 1000L) // ps: 更新进度条 播放进度文本
                controlViewHolder?.showProgress()
                controlViewHolder?.showControlView()
            }
        }
        phoneObserver = object : PhoneStateObserver {}
    }

    override fun layoutId(): Int {
        return R.layout.attachment_control
    }

    override fun bind(xmVideoView: XmVideoView) {
        super.bind(xmVideoView)
        portraitXmVideoViewRect?.left = xmVideoView.left
        portraitXmVideoViewRect?.top = xmVideoView.top
        portraitXmVideoViewRect?.right = xmVideoView.right
        portraitXmVideoViewRect?.bottom = xmVideoView.bottom
        addPortraitView()
    }

    private var portraitXmVideoViewRect: Rect? = Rect()
    //private var portraitXmVideoView: XmVideoView? = null
    private fun addPortraitView(visibility: Int = View.GONE) {
        val portraitView = getView(R.layout.attachment_control_portrait)
        portraitView.visibility = visibility
        addView(portraitView, LayoutParams(MATCH_PARENT, MATCH_PARENT))
        controlViewHolder = PortraitViewHolder.create(portraitView)
        controlViewHolder?.playResID = R.mipmap.media_control_play
        controlViewHolder?.pauseResID = R.mipmap.media_control_pause
        controlViewHolder?.bind(this)
        controlViewHolder?.listener = object : ControlViewHolder.OnScreenStateListener {
            override fun onState(type: String) {
                this@AttachmentControl.removeAllViews()//删除所有子View ps:子View包含横屏or竖屏View
                when (type) {
                    PORTRAIT -> {
                        addPortraitView(View.VISIBLE)
                    }
                    LANDSCAPE -> {
                        addLandscapeView(View.VISIBLE)
                    }
                }
            }
        }
        controlViewHolder?.progressTimerStart(period.toLong())
    }

    private fun addLandscapeView(visibility: Int = View.GONE) {
        val landscapeView = getView(R.layout.attachment_control_landscape)
        landscapeView.visibility = visibility
        addView(landscapeView, LayoutParams(MATCH_PARENT, MATCH_PARENT))
        controlViewHolder = LandscapeViewHolder.create(landscapeView)
        controlViewHolder?.playResID = R.mipmap.media_control_play
        controlViewHolder?.pauseResID = R.mipmap.media_control_pause
        controlViewHolder?.bind(this)
        controlViewHolder?.portraitXmVideoViewRect = portraitXmVideoViewRect
        controlViewHolder?.listener = object : ControlViewHolder.OnScreenStateListener {
            override fun onState(type: String) {
                this@AttachmentControl.removeAllViews()//删除所有子View ps:子View包含横屏or竖屏View
                when (type) {
                    PORTRAIT -> {
                        addPortraitView(View.VISIBLE)
                    }
                    LANDSCAPE -> {
                        addLandscapeView(View.VISIBLE)
                    }
                }
            }
        }
        controlViewHolder?.progressTimerStart(period.toLong())
        (controlViewHolder as LandscapeViewHolder)
    }
}
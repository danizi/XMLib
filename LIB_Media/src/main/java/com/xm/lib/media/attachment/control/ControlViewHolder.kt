package com.xm.lib.media.attachment.control

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Rect
import android.view.View
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.TimerHelper
import com.xm.lib.media.base.XmMediaPlayer
import com.xm.lib.media.base.XmVideoView

/**
 * 播放器控制页面抽象基类ViewHolder
 */
abstract class ControlViewHolder {
    protected val TAG = "ControlViewHolder"
    private var progressTimer: TimerHelper? = TimerHelper()
    private var controlViewHideTimer: TimerHelper? = TimerHelper()
    var attachmentControl: AttachmentControl? = null //播放器控制页面
    var isHorizontalSlide = false //是否处于水平滑动
    var isClick = false //是否处理点击状态
    var progress = 0    //保存用户拖动进度 单位:%
    var rootView: View? = null

    var playResID = 0  //播放图片资源
    var pauseResID = 0 //暂停图片资源

    var portraitXmVideoViewRect: Rect? = null //保存竖屏的控件

    protected var activity: Activity? = null
    protected var mediaPlayer: XmMediaPlayer? = null
    protected var xmVideoView: XmVideoView? = null
    protected var screenW = 0
    protected var screenH = 0
    var listener: OnScreenStateListener? = null

    /**
     * 点击屏幕控制界面 显示和隐藏状态来回切换
     */
    abstract fun showOrHideControlView()

    /**
     * 显示播放器控制界 PS : 只显示顶部和底部，按需显示”
     */
    open fun showControlView() {
        rootView?.visibility = View.VISIBLE
        showTop()
        showBottom()
    }

    abstract fun showProgress()
    abstract fun showLoading()
    abstract fun showTop()
    abstract fun showBottom()

    /**
     * 隐藏播放器控制界面，PS : 横竖切屏所要隐藏的界面块不同，需要特定操作请覆盖写该方法
     */
    open fun hideControlView() {
        rootView?.visibility = View.GONE
        hideTop()
        hideBottom()
        hideLoading()
        hideProgress()
    }

    abstract fun hideProgress()
    abstract fun hideLoading()
    abstract fun hideTop()
    abstract fun hideBottom()

    /**
     * 绑定播放器
     */
    open fun bind(attachmentControl: AttachmentControl?) {
        this.attachmentControl = attachmentControl
    }

    /**
     * 更新播放器进度UI界面 PS:更新进度条、控制页面“居中进度文本”、底部进度条“居右进度文本”
     * @param isSetProgress 如果手指未触碰seekBar控件则使用默认值，否则设置false
     */
    abstract fun updateProgress(pos: Long, isSetProgress: Boolean = true)

    /**
     * tingzhi
     */
    abstract fun horizontalSlideStopSeekTo()

    /**
     * 设置播放动作图片
     */
    abstract fun setActionResID(id: Int)

    /**
     * 设置缓冲进度 单位:%
     */
    abstract fun secondaryProgress(present: Int)

    /**
     * 设置播放进度 单位:%
     */
    abstract fun progress(present: Int)

    /**
     * 进度更新定时器
     */
    open fun progressTimerStart(period: Long) {
        /*定时更新进度*/
        progressTimer?.start(object : TimerHelper.OnPeriodListener {
            @SuppressLint("SetTextI18n")
            override fun onPeriod() {
                val mediaPlayer = attachmentControl?.xmVideoView?.mediaPlayer
                if (mediaPlayer == null) {
                    BKLog.e(TAG, "progressTimerStart() mediaPlayer is null ")
                    progressTimer?.stop()
                    return
                }

                val pos = mediaPlayer.getCurrentPosition()
                val duration = mediaPlayer.getDuration()
                if (pos > duration) {
                    progressTimer?.stop()
                    return
                }
                updateProgress(pos)
            }
        }, period)
    }

    fun progressTimerStop() {
        /*关闭定时更新进度*/
        progressTimer?.stop()
    }

    /**
     * 延时隐藏播放器界面定时器
     */
    fun startDelayTimerHideControlView(delay: Int) {
        /*延时隐藏控制界面*/
        controlViewHideTimer?.start(object : TimerHelper.OnDelayTimerListener {
            override fun onDelayTimerFinish() {
                hideControlView()
            }
        }, delay.toLong())

    }

    fun stopDelayTimerHideControlView() {
        /*停止延时隐藏控制界面*/
        controlViewHideTimer?.stop()
    }

    interface OnScreenStateListener {
        fun onState(type: String)
    }
}
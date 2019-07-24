package com.xm.lib.media.attachment.control.viewholder

import android.annotation.SuppressLint
import android.app.Activity
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ScreenUtil
import com.xm.lib.common.util.TimeUtil
import com.xm.lib.media.R
import com.xm.lib.media.attachment.control.AttachmentControl
import com.xm.lib.media.attachment.control.ControlViewHolder
import com.xm.lib.media.view.XmPopWindow

/**
 * 竖屏界面
 */
class PortraitViewHolder : ControlViewHolder {

    companion object {
        fun create(rootView: View?): PortraitViewHolder {
            val clPortraitTop = rootView?.findViewById<View>(R.id.cl_portrait_top) as ConstraintLayout
            val ivBack = rootView.findViewById<View>(R.id.iv_back) as ImageView
            val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
            val ivListener = rootView.findViewById<View>(R.id.iv_listener) as ImageView
            val ivMiracast = rootView.findViewById<View>(R.id.iv_miracast) as ImageView
            val ivShare = rootView.findViewById<View>(R.id.iv_share) as ImageView
            val ivMore = rootView.findViewById<View>(R.id.iv_more) as ImageView
            val clPortraitBottom = rootView.findViewById<View>(R.id.cl_portrait_bottom) as ConstraintLayout
            val ivAction = rootView.findViewById<View>(R.id.iv_action) as ImageView
            val seekBar = rootView.findViewById<View>(R.id.seekBar) as SeekBar
            val tvTime = rootView.findViewById<View>(R.id.tv_time) as TextView
            val ivScreenFull = rootView.findViewById<View>(R.id.iv_screen_full) as ImageView
            val clSeek = rootView.findViewById<View>(R.id.cl_seek) as ConstraintLayout
            val tvTime2 = rootView.findViewById<View>(R.id.tv_time2) as TextView
            val pbLoading = rootView.findViewById<View>(R.id.pb) as ProgressBar
            return PortraitViewHolder(rootView, clPortraitTop, ivBack, tvTitle, ivListener, ivMiracast, ivShare, ivMore, clPortraitBottom, ivAction, seekBar, tvTime, ivScreenFull, clSeek, tvTime2, pbLoading)
        }
    }

    private constructor(rootView: View, clPortraitTop: ConstraintLayout, ivBack: ImageView, tvTitle: TextView, ivListener: ImageView, ivMiracast: ImageView, ivShare: ImageView, ivMore: ImageView, clPortraitBottom: ConstraintLayout, ivAction: ImageView, seekBar: SeekBar, tvTime: TextView, ivScreenFull: ImageView, clSeek: ConstraintLayout, tvTime2: TextView, pbLoading: ProgressBar) {
        this.attachmentControl = attachmentControl
        this.rootView = rootView
        this.clPortraitTop = clPortraitTop
        this.ivBack = ivBack
        this.tvTitle = tvTitle
        this.ivListener = ivListener
        this.ivMiracast = ivMiracast
        this.ivShare = ivShare
        this.ivMore = ivMore
        this.clPortraitBottom = clPortraitBottom
        this.ivAction = ivAction
        this.seekBar = seekBar
        this.tvTime = tvTime
        this.ivScreenFull = ivScreenFull
        this.clSeek = clSeek
        this.tvTime2 = tvTime2
        this.pbLoading = pbLoading
    }

    private var clPortraitTop: ConstraintLayout? = null
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null
    private var ivListener: ImageView? = null
    private var ivMiracast: ImageView? = null
    private var ivShare: ImageView? = null
    private var ivMore: ImageView? = null
    private var clPortraitBottom: ConstraintLayout? = null
    private var ivAction: ImageView? = null
    private var seekBar: SeekBar? = null
    private var tvTime: TextView? = null
    private var ivScreenFull: ImageView? = null
    private var clSeek: ConstraintLayout? = null
    private var tvTime2: TextView? = null
    private var pbLoading: ProgressBar? = null


    override fun horizontalSlideStopSeekTo() {
        val seekPos = mediaPlayer?.getDuration()!! * (progress.toFloat() / 100f)
        mediaPlayer?.seekTo(seekPos.toInt())
    }

    override fun bind(attachmentControl: AttachmentControl?) {
        super.bind(attachmentControl)
        mediaPlayer = attachmentControl?.xmVideoView?.mediaPlayer
        xmVideoView = attachmentControl?.xmVideoView
        size()
        initEvent()
    }

    private fun size() {
        activity = attachmentControl?.context as Activity
        screenW = ScreenUtil.getNormalWH(activity)[0]
        screenH = ScreenUtil.getNormalWH(activity)[1]
        if (screenH > screenW) {
            val temp = screenW
            screenW = screenH
            screenH = temp
        }
    }

    private fun initEvent() {
        //顶部
        ivBack?.setOnClickListener {
            activity?.finish()
        }
        ivListener?.setOnClickListener {
            Toast.makeText(activity, "耳机模式", Toast.LENGTH_SHORT).show()
        }
        ivMiracast?.setOnClickListener {
            Toast.makeText(activity, "投屏", Toast.LENGTH_SHORT).show()
        }
        ivShare?.setOnClickListener {
            val xmPopWindow = XmPopWindow(activity)
            val shareView = LayoutInflater.from(activity).inflate(R.layout.media_share, null, false)
            val share: ImageView = shareView.findViewById(R.id.iv_share_wx)
            val friend: ImageView = shareView.findViewById(R.id.iv_share_friend)
            share.setOnClickListener {
                Toast.makeText(activity, "分享到微信", Toast.LENGTH_SHORT).show()
            }
            friend.setOnClickListener {
                Toast.makeText(activity, "分享到朋友圈", Toast.LENGTH_SHORT).show()
            }
            xmPopWindow.ini(shareView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            xmPopWindow.showAtLocation(XmPopWindow.Location.BOTTOM, R.style.AnimationBottomFade, activity?.window?.decorView!!, 0, 0)
        }

        //底部
        ivAction?.setOnClickListener {
            try {
                if (mediaPlayer?.isPlaying() == true) {
                    ivAction?.setImageResource(playResID)
                    mediaPlayer?.pause()
                } else {
                    ivAction?.setImageResource(pauseResID)
                    mediaPlayer?.start()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        ivScreenFull?.setOnClickListener {
            //横屏 高度 < 宽度
            ScreenUtil.setLandscape(activity)  //设置横屏
            ScreenUtil.hideStatusBar(activity) //隐藏系统状态栏

            //xmVideoView?.layout(0, 0, screenW, screenH)    //设置宽高
            xmVideoView?.layoutParams?.height = screenH
            xmVideoView?.layoutParams?.width = screenW

            hideControlView()                  //隐藏控制界面  PS : 或者删除
            listener?.onState(AttachmentControl.LANDSCAPE)
        }

        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            /**
             * 设置progress 属性也会触发，所有设置进度完成的时候应该在DOWN_UP事件回调中设置播放器播放进度
             */
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                this@PortraitViewHolder.progress = progress
                if (isHorizontalSlide) {
                    //用户在屏幕水平滑动，但未触碰到seekbar时
                    BKLog.i(AttachmentControl.TAG, "“未”触碰到Seekbar，滑动中... progress:$progress")
                } else {
                    //用户触碰了seekbar或者定时器一直设置progress属性值时
                    val pos = ((progress.toFloat() / 100f) * mediaPlayer?.getDuration()!!).toLong()
                    updateProgress(pos, false)
                    BKLog.i(AttachmentControl.TAG, "触碰到Seekbar，滑动中... progress:$progress")
                }
            }

            /**
             * 只有手指触控了滑块触发
             */
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                showControlView()
                showProgress()
                progressTimerStop()
                progress = 0
                BKLog.d(AttachmentControl.TAG, "开始触发滑动 progress:$progress")
            }

            /**
             * 只有手指触控了滑块释放后触发
             */
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                hideControlView()
                horizontalSlideStopSeekTo()
                progressTimerStart(period = 1000)
                BKLog.d(AttachmentControl.TAG, "结束触发滑动 progress:$progress")
            }
        })
    }

    override fun showOrHideControlView() {
        if (clPortraitBottom?.visibility == View.VISIBLE) {
            hideControlView()
        } else {
            showControlView()
        }
    }

    override fun showLoading() {
        pbLoading?.visibility = View.VISIBLE
    }

    override fun showTop() {
        clPortraitTop?.visibility = View.VISIBLE
    }

    override fun showBottom() {
        clPortraitBottom?.visibility = View.VISIBLE
    }

    override fun showProgress() {
        clSeek?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        pbLoading?.visibility = View.GONE
    }

    override fun hideTop() {
        clPortraitTop?.visibility = View.GONE
    }

    override fun hideBottom() {
        clPortraitBottom?.visibility = View.GONE
    }

    override fun hideProgress() {
        clSeek?.visibility = View.GONE
    }

    override fun setActionResID(id: Int) {
        try {
            ivAction?.setImageResource(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun hideControlView() {
        hideBottom()
        hideLoading()
        hideProgress()
    }

    override fun secondaryProgress(present: Int) {
        seekBar?.secondaryProgress = present
    }

    override fun progress(present: Int) {
        seekBar?.progress = present
    }

    @SuppressLint("SetTextI18n")
    override fun updateProgress(pos: Long, isSetProgress: Boolean) {
        if (mediaPlayer == null) {
            BKLog.e(TAG, "updateProgress() mediaPlayer is null")
            return
        }
        val duration = mediaPlayer?.getDuration()!!
        if (isSetProgress) {
            seekBar?.progress = (pos * 100f / duration.toFloat()).toInt()
        }
        if (pos in 0..duration) {
            tvTime?.text = TimeUtil.hhmmss(pos) + "/" + TimeUtil.hhmmss(duration)
            tvTime2?.text = TimeUtil.hhmmss(pos) + "/" + TimeUtil.hhmmss(duration)
        }
    }

}

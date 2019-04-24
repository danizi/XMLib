package com.xm.lib.media.attachment.control.viewholder

import android.R.attr.x
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
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
 * 横屏界面
 */
class LandscapeViewHolder : ControlViewHolder {

    companion object {
        fun create(rootView: View?): LandscapeViewHolder {
            val clLandscapeTop = rootView?.findViewById<View>(R.id.cl_landscape_top) as ConstraintLayout
            val ivBack = rootView.findViewById<View>(R.id.iv_back) as ImageView
            val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
            val ivShare = rootView.findViewById<View>(R.id.iv_share) as ImageView
            val ivMore = rootView.findViewById<View>(R.id.iv_more) as ImageView
            val clLandscapeBottom = rootView.findViewById<View>(R.id.cl_landscape_bottom) as ConstraintLayout
            val seekBar = rootView.findViewById<View>(R.id.seekBar) as SeekBar
            val ivAction = rootView.findViewById<View>(R.id.iv_action) as ImageView
            val tvTime = rootView.findViewById<View>(R.id.tv_time) as TextView
            val tvRatio = rootView.findViewById<View>(R.id.tv_ratio) as TextView
            val clSeek = rootView.findViewById<View>(R.id.cl_seek) as ConstraintLayout
            val tvTime2 = rootView.findViewById<View>(R.id.tv_time2) as TextView
            val pbLoading = rootView.findViewById<View>(R.id.pb) as ProgressBar
            val rv = rootView.findViewById<View>(R.id.rv) as RecyclerView

            return LandscapeViewHolder(clLandscapeTop, ivBack, tvTitle, ivShare, ivMore, clLandscapeBottom, seekBar, ivAction, tvTime, tvRatio, clSeek, tvTime2, pbLoading, rv)
        }
    }

    private var clLandscapeTop: ConstraintLayout? = null
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null
    private var ivShare: ImageView? = null
    private var ivMore: ImageView? = null
    private var clLandscapeBottom: ConstraintLayout? = null
    private var seekBar: SeekBar? = null
    private var ivAction: ImageView? = null
    private var tvTime: TextView? = null
    private var tvRatio: TextView? = null
    private var clSeek: ConstraintLayout? = null
    private var tvTime2: TextView? = null
    private var pbLoading: ProgressBar? = null
    private var rv: RecyclerView? = null

    private constructor(clLandscapeTop: ConstraintLayout, ivBack: ImageView, tvTitle: TextView, ivShare: ImageView, ivMore: ImageView, clLandscapeBottom: ConstraintLayout, seekBar: SeekBar, ivAction: ImageView, tvTime: TextView, tvRatio: TextView, clSeek: ConstraintLayout, tvTime2: TextView, pbLoading: ProgressBar, rv: RecyclerView) {
        this.clLandscapeTop = clLandscapeTop
        this.ivBack = ivBack
        this.tvTitle = tvTitle
        this.ivShare = ivShare
        this.ivMore = ivMore
        this.clLandscapeBottom = clLandscapeBottom
        this.seekBar = seekBar
        this.ivAction = ivAction
        this.tvTime = tvTime
        this.tvRatio = tvRatio
        this.clSeek = clSeek
        this.tvTime2 = tvTime2
        this.pbLoading = pbLoading
        this.rv = rv
    }

    override fun bind(attachmentControl: AttachmentControl?) {
        super.bind(attachmentControl)
        mediaPlayer = attachmentControl?.xmVideoView?.mediaPlayer
        xmVideoView = attachmentControl?.xmVideoView
        activity = attachmentControl?.context as Activity
        screenW = ScreenUtil.getNormalWH(activity)[0]
        screenH = ScreenUtil.getNormalWH(activity)[1]
        if (screenH < screenW) {
            val temp = screenH
            screenH = screenW
            screenW = temp
        }
        initEvent()
    }

    private var mLastY: Int = 0
    private var mLastX: Int = 0
    private var deltaX = 0
    private var deltaY = 0
    @SuppressLint("ClickableViewAccessibility", "ObjectAnimatorBinding")
    private fun initEvent() {
        //顶部
        ivBack?.setOnClickListener {
            BKLog.d(TAG, "Landscape -> Portrait")
            // 横屏高度 > 宽度
            //设置竖屏
            ScreenUtil.setPortrait(activity)
            //显示系统状态栏
            ScreenUtil.setDecorVisible(activity)
            //设置宽高
            if (portraitXmVideoViewRect != null) {
                //xmVideoView?.layout(portraitXmVideoViewRect?.left!!, portraitXmVideoViewRect?.top!!, portraitXmVideoViewRect?.right!!, portraitXmVideoViewRect?.bottom!!)
                xmVideoView?.layoutParams?.width = portraitXmVideoViewRect?.right!!
                xmVideoView?.layoutParams?.height = portraitXmVideoViewRect?.bottom!!

            } else {
                BKLog.e(TAG, "请给portraitXmVideoView属性赋值")
            }
            hideControlView()
            listener?.onState(AttachmentControl.PORTRAIT)
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
        ivMore?.setOnClickListener {
            val xmPopWindow = XmPopWindow(activity)
            val view = LayoutInflater.from(activity).inflate(R.layout.attachment_control_landscape_setting_pop, null, false)
            view.findViewById<TextView>(R.id.tv_speed_20).setOnClickListener {
                xmVideoView?.mediaPlayer?.setSpeed(2.0f)
            }
            xmPopWindow.ini(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
            xmPopWindow.showAtLocation(XmPopWindow.Location.RIGHT, R.style.AnimationRightFade, activity?.window?.decorView!!, 0, 0)
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
        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            /**
             * 设置progress 属性也会触发，所有设置进度完成的时候应该在DOWN_UP事件回调中设置播放器播放进度
             */
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                this@LandscapeViewHolder.progress = progress
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
        tvRatio?.setOnClickListener { it ->
            val xmPopWindow = XmPopWindow(activity)
            val ratioView = LayoutInflater.from(activity).inflate(R.layout.attachment_control_landscape_ratio, null, false)
            val tvRatio360p: TextView = ratioView.findViewById(R.id.tv_ratio_360p)
            val tvRatio480p: TextView = ratioView.findViewById(R.id.tv_ratio_480p)
            val tvRatio720p: TextView = ratioView.findViewById(R.id.tv_ratio_720p)
            val tvRatio1080p: TextView = ratioView.findViewById(R.id.tv_ratio_1080p)
            tvRatio360p.setOnClickListener {
                Toast.makeText(activity, "360p", Toast.LENGTH_SHORT).show()
            }
            tvRatio480p.setOnClickListener {
                Toast.makeText(activity, "480p", Toast.LENGTH_SHORT).show()
            }
            tvRatio720p.setOnClickListener {
                Toast.makeText(activity, "720p", Toast.LENGTH_SHORT).show()
            }
            tvRatio1080p.setOnClickListener {
                Toast.makeText(activity, "1080p", Toast.LENGTH_SHORT).show()
            }
            xmPopWindow.ini(ratioView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
            xmPopWindow.showAtLocation(XmPopWindow.Location.RIGHT, R.style.AnimationRightFade, activity?.window?.decorView!!, 0, 0)
        }
        rv?.setOnTouchListener { v, event ->
            //val x = event.rawX.toInt()
            val y = event.rawY.toInt()

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                }
                MotionEvent.ACTION_UP -> {
                    if (deltaY > 0) {
                        ObjectAnimator.ofFloat(rv, "translationY", rv?.translationY!!, 0f).setDuration(500).start()
                    } else {
                        ObjectAnimator.ofFloat(rv, "translationY", rv?.translationY!!.toFloat(), (-rv?.height!!).toFloat()).setDuration(500).start()
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    deltaX = x - mLastX//计算x坐标上的差值
                    deltaY = y - mLastY//计算y坐标上的差值
                    val tranX = rv?.translationX!! + deltaX//要平移的x值
                    val tranY = rv?.translationY!! + deltaY//要平移的y值
                    rv?.translationX = tranX//设置值
                    rv?.translationY = tranY
                }
            }
            mLastX = x;//记录上次的坐标
            mLastY = y;
            true
        }
    }

    override fun showOrHideControlView() {
        if (clLandscapeBottom?.visibility == View.VISIBLE) {
            hideControlView()
        } else {
            showControlView()
        }
    }

    override fun showProgress() {
        pbLoading?.visibility = View.VISIBLE
    }

    override fun showLoading() {
        pbLoading?.visibility = View.VISIBLE
    }

    override fun showTop() {
        clLandscapeTop?.visibility = View.VISIBLE
    }

    override fun showBottom() {
        clLandscapeBottom?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        pbLoading?.visibility = View.GONE
    }

    override fun hideTop() {
        clLandscapeTop?.visibility = View.GONE
    }

    override fun hideBottom() {
        clLandscapeBottom?.visibility = View.GONE
    }

    override fun hideProgress() {
        pbLoading?.visibility = View.GONE
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

    override fun horizontalSlideStopSeekTo() {
        val seekPos = mediaPlayer?.getDuration()!! * (progress.toFloat() / 100f)
        mediaPlayer?.seekTo(seekPos.toInt())
    }

    override fun setActionResID(id: Int) {
        try {
            ivAction?.setImageResource(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun secondaryProgress(present: Int) {
        seekBar?.secondaryProgress = present
    }

    override fun progress(present: Int) {
        seekBar?.progress = present
    }
}

/**
 * 播放列表ViewHolder
 */
class PlayListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind() {

    }
}

/**
 * 播放列表Adapter
 */
class PlayListAdapter : RecyclerView.Adapter<PlayListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        return PlayListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

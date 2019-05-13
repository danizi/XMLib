package com.xm.lib.media.attachment.control.viewholder

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ScreenUtil
import com.xm.lib.common.util.TimeUtil
import com.xm.lib.media.test.MediaListEnt
import com.xm.lib.media.R
import com.xm.lib.media.attachment.control.AttachmentControl
import com.xm.lib.media.attachment.control.ControlViewHolder
import com.xm.lib.media.base.XmVideoView
import com.xm.lib.media.view.XmPopWindow
import okhttp3.*
import java.io.IOException


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
            PopSettingViewHolder.create(xmVideoView, view)
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
//        rv?.setOnTouchListener { v, event ->
//            //val x = event.rawX.toInt()
//            val y = event.rawY.toInt()
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
//                }
//                MotionEvent.ACTION_UP -> {
//                    if (deltaY > 0) {
//                        ObjectAnimator.ofFloat(rv, "translationY", rv?.translationY!!, 0f).setDuration(500).start()
//                    } else {
//                        ObjectAnimator.ofFloat(rv, "translationY", rv?.translationY!!.toFloat(), (-rv?.height!!).toFloat()).setDuration(500).start()
//                    }
//                }
//                MotionEvent.ACTION_MOVE -> {
//                    if (mLastX != 0 && mLastY != 0) {
//                        deltaX = x - mLastX//计算x坐标上的差值
//                        deltaY = y - mLastY//计算y坐标上的差值
//                        BKLog.d("rv?.translationY:${rv?.translationY} deltaY:$deltaY")
//                        val tranX = rv?.translationX!! + deltaX//要平移的x值
//                        val tranY = rv?.translationY!! + deltaY//要平移的y值
//                        rv?.translationX = tranX//设置值
//                        rv?.translationY = tranY
//                    }
//                }
//            }
//            mLastX = x;//记录上次的坐标
//            mLastY = y;
//            false
//        }

        val l = LinearLayoutManager(activity)
        l.orientation = LinearLayoutManager.HORIZONTAL
        rv?.layoutManager = l

        //请求列表数据
        val okHttpClient = OkHttpClient.Builder().build()
        val request = Request.Builder()
                .url("https://api.tradestudy.cn/v3/course?courseId=e90b1cbc845411e5a95900163e000c35")
                .addHeader("x-tradestudy-access-token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IlpsczdzM0p6SG8zVHl6TkN3UU9iekUzakJNalB1L1loektHemNRYXlzOENHdkxBS1R5REFXbGt1K1FpdFE5WTJqTzAvNnJnQkgwVXA1cjJDYUxTakNBPT0iLCJwaG9uZSI6IjE1MDc0NzcwNzA4IiwiaWQiOiI2NTc4M2IxNWQ0NzcxMWU4OGI0NDAyNDJhYzEzMDAwMyIsInRva2VuIjoiZjc0OTEyYjIzYWFkNDIzMzliNjg1NDdmNzIyY2Y2NDEifQ.I2VniieCs33Q-78jkzfdI4O_aqosAiFOijpbCujtR5g")
                .addHeader("x-tradestudy-client-version", "3.4.3")
                .addHeader("x-tradestudy-client-device", "android_phone")
                .addHeader("x-tradestudy-access-key-id", "c")
                .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("", "")
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val mediaListEnt = Gson().fromJson(body, MediaListEnt::class.java)
                val sections = ArrayList<MediaListEnt.ChaptersBean.SectionsBean>()
                for (ent in mediaListEnt.chapters) {
                    sections.addAll(ent.sections)
                }
                activity?.runOnUiThread {
                    rv?.adapter = PlayListAdapter(sections, xmVideoView)
                }
                Log.d("", "body:$body-$mediaListEnt")
            }
        })
    }

    @SuppressLint("ObjectAnimatorBinding")
    override fun showPlayList() {
        super.showPlayList()
        rv?.visibility = View.VISIBLE

    }

    @SuppressLint("ObjectAnimatorBinding")
    override fun hidePlayListAni() {
        super.hidePlayListAni()
        ObjectAnimator.ofFloat(rv, "translationY", rv?.translationY!!, 0f).setDuration(500).start()
    }

    @SuppressLint("ObjectAnimatorBinding")
    override fun showPlayListAni() {
        super.showPlayListAni()
        ObjectAnimator.ofFloat(rv, "translationY", rv?.translationY!!.toFloat(), (-rv?.height!!).toFloat()).setDuration(500).start()
    }

    @SuppressLint("ObjectAnimatorBinding")
    override fun hidePlayList() {
        super.hidePlayList()
        rv?.visibility = View.GONE
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

    private class PopSettingViewHolder private constructor(val xmVideoView: XmVideoView?, val context: Context, val tvEdit: TextView, val clAction: ConstraintLayout, val imageView: ImageView, val clSpeed: ConstraintLayout, val tvSpeedTitle: TextView, val tvSpeed05: TextView, val tvSpeed075: TextView, val tvSpeed10: TextView, val tvSpeed125: TextView, val tvSpeed15: TextView, val tvSpeed20: TextView, val clTimerTop: ConstraintLayout, val tvTimerTitle: TextView, val tvTimerNoOpen: TextView, val tvTimerComplete: TextView, val tvTimerCustom: TextView, val clPlayType: ConstraintLayout, val tvPlayTypeTitle: TextView, val tvPlayTypeAuto: TextView, val tvPlayTypeListLoop: TextView, val tvPlayTypeLoop: TextView, val tvPlayTypeCustom: TextView, val clCanvas: ConstraintLayout, val tvCanvasTitle: TextView, val tvCanvasFit: TextView, val tvCanvasFill: TextView, val tvCanvas169: TextView, val tvCanvas43: TextView) {
        companion object {

            fun create(xmVideoView: XmVideoView?, rootView: View?): PopSettingViewHolder {
                val tvEdit = rootView?.findViewById<View>(R.id.tv_edit) as TextView
                val clAction = rootView.findViewById<View>(R.id.cl_action) as ConstraintLayout
                val imageView = rootView.findViewById<View>(R.id.imageView) as ImageView
                val clSpeed = rootView.findViewById<View>(R.id.cl_speed) as ConstraintLayout
                val tvSpeedTitle = rootView.findViewById<View>(R.id.tv_speed_title) as TextView
                val tvSpeed05 = rootView.findViewById<View>(R.id.tv_speed_05) as TextView
                val tvSpeed075 = rootView.findViewById<View>(R.id.tv_speed_075) as TextView
                val tvSpeed10 = rootView.findViewById<View>(R.id.tv_speed_10) as TextView
                val tvSpeed125 = rootView.findViewById<View>(R.id.tv_speed_125) as TextView
                val tvSpeed15 = rootView.findViewById<View>(R.id.tv_speed_15) as TextView
                val tvSpeed20 = rootView.findViewById<View>(R.id.tv_speed_20) as TextView
                val clTimerTop = rootView.findViewById<View>(R.id.cl_timer_top) as ConstraintLayout
                val tvTimerTitle = rootView.findViewById<View>(R.id.tv_timer_title) as TextView
                val tvTimerNoOpen = rootView.findViewById<View>(R.id.tv_timer_no_open) as TextView
                val tvTimerComplete = rootView.findViewById<View>(R.id.tv_timer_complete) as TextView
                val tvTimerCustom = rootView.findViewById<View>(R.id.tv_timer_custom) as TextView
                val clPlayType = rootView.findViewById<View>(R.id.cl_play_type) as ConstraintLayout
                val tvPlayTypeTitle = rootView.findViewById<View>(R.id.tv_play_type_title) as TextView
                val tvPlayTypeAuto = rootView.findViewById<View>(R.id.tv_play_type_auto) as TextView
                val tvPlayTypeListLoop = rootView.findViewById<View>(R.id.tv_play_type_list_loop) as TextView
                val tvPlayTypeLoop = rootView.findViewById<View>(R.id.tv_play_type_loop) as TextView
                val tvPlayTypeCustom = rootView.findViewById<View>(R.id.tv_play_type_custom) as TextView
                val clCanvas = rootView.findViewById<View>(R.id.cl_canvas) as ConstraintLayout
                val tvCanvasTitle = rootView.findViewById<View>(R.id.tv_canvas_title) as TextView
                val tvCanvasFit = rootView.findViewById<View>(R.id.tv_canvas_fit) as TextView
                val tvCanvasFill = rootView.findViewById<View>(R.id.tv_canvas_fill) as TextView
                val tvCanvas169 = rootView.findViewById<View>(R.id.tv_canvas_16_9) as TextView
                val tvCanvas43 = rootView.findViewById<View>(R.id.tv_canvas_4_3) as TextView
                return PopSettingViewHolder(xmVideoView, rootView.context, tvEdit, clAction, imageView, clSpeed, tvSpeedTitle, tvSpeed05, tvSpeed075, tvSpeed10, tvSpeed125, tvSpeed15, tvSpeed20, clTimerTop, tvTimerTitle, tvTimerNoOpen, tvTimerComplete, tvTimerCustom, clPlayType, tvPlayTypeTitle, tvPlayTypeAuto, tvPlayTypeListLoop, tvPlayTypeLoop, tvPlayTypeCustom, clCanvas, tvCanvasTitle, tvCanvasFit, tvCanvasFill, tvCanvas169, tvCanvas43)
            }
        }

        init {
            tvSpeed05.setOnClickListener {
                xmVideoView?.mediaPlayer?.setSpeed(0.5f)
                Toast.makeText(context, "0.5倍速度", Toast.LENGTH_SHORT).show()
            }
            tvSpeed075.setOnClickListener {
                xmVideoView?.mediaPlayer?.setSpeed(0.75f)
                Toast.makeText(context, "0.75倍速度", Toast.LENGTH_SHORT).show()
            }
            tvSpeed10.setOnClickListener {
                xmVideoView?.mediaPlayer?.setSpeed(1.0f)
                Toast.makeText(context, "1.0倍速度", Toast.LENGTH_SHORT).show()
            }
            tvSpeed125.setOnClickListener {
                xmVideoView?.mediaPlayer?.setSpeed(1.25f)
                Toast.makeText(context, "1.25倍速度", Toast.LENGTH_SHORT).show()
            }
            tvSpeed15.setOnClickListener {
                xmVideoView?.mediaPlayer?.setSpeed(1.5f)
                Toast.makeText(context, "1.5倍速度", Toast.LENGTH_SHORT).show()
            }
            tvSpeed20.setOnClickListener {
                xmVideoView?.mediaPlayer?.setSpeed(2.0f)
                Toast.makeText(context, "2.0倍速度", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 播放列表ViewHolder
     */
    private class PlayListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var cover: ImageView? = null
        private var title: TextView? = null
        fun bind(any: Any, mediaPlayer: XmVideoView?) {
            val sectionsBean = any as MediaListEnt.ChaptersBean.SectionsBean
            cover = itemView.findViewById(R.id.iv_cover)
            title = itemView.findViewById(R.id.tv_title)

            Glide.with(itemView.context).load(sectionsBean.avatar).error(R.mipmap.ic_launcher).listener(object : RequestListener<String?, GlideDrawable?> {
                override fun onException(e: java.lang.Exception?, model: String?, target: Target<GlideDrawable?>?, isFirstResource: Boolean): Boolean {
                    e?.printStackTrace()
                    return false
                }

                override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable?>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                    return false
                }
            }).into(cover)
            title?.text = sectionsBean.name
            itemView.setOnClickListener {
                mediaPlayer?.start(sectionsBean.hls1, true)
            }
        }
    }

    /**
     * 播放列表Adapter
     */
    private class PlayListAdapter(val data: ArrayList<*>, val xmVideoView: XmVideoView?) : RecyclerView.Adapter<PlayListViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
            return PlayListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
        }

        override fun getItemCount(): Int {
            return if (data.isEmpty()) {
                0
            } else {
                data.size
            }
        }

        override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
            holder.bind(data[position]!!, xmVideoView)
        }
    }
}

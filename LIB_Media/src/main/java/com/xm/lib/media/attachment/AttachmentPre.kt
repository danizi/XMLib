package com.xm.lib.media.attachment

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.xm.lib.media.R
import com.xm.lib.media.base.IXmMediaPlayer
import com.xm.lib.media.event.GestureObserver
import com.xm.lib.media.event.PhoneStateObserver
import com.xm.lib.media.event.PlayerObserver


class AttachmentPre(context: Context?, private var preUrl: String = "") : BaseAttachmentView(context!!) {
    private var ivStart: ImageView? = null
    private var pbLoading: ProgressBar? = null
    var url: String = ""
    private var ivPre: ImageView? = null

    init {
        observer = object : PlayerObserver {
            override fun onPrepared(mp: IXmMediaPlayer) {
                super.onPrepared(mp)
                xmVideoView?.mediaPlayer?.start()
                xmVideoView?.unBindAttachmentView(this@AttachmentPre)
            }
        }
        gestureObserver = object : GestureObserver {

        }
        phoneObserver = object : PhoneStateObserver {}
        Glide.with(context).load(preUrl).error(R.drawable.ic_launcher_background).into(ivPre)//加载图片
    }

    override fun layoutId(): Int {
        return R.layout.attachment_pre
    }

    override fun findViews(view: View?) {
        ivPre = view?.findViewById(R.id.iv_pre)
        ivStart = view?.findViewById(R.id.iv_start)
        pbLoading = view?.findViewById(R.id.pb_loading)
    }

    override fun initEvent() {
        ivStart?.setOnClickListener {
            if (ivStart?.visibility == View.VISIBLE) {
                ivStart?.visibility = View.GONE
                pbLoading?.visibility = View.VISIBLE
                xmVideoView?.start(url, true) //播放视频
                xmVideoView?.bringChildToFront(this@AttachmentPre)
            }
        }
    }

    override fun initDisplay() {
        ivPre?.visibility = View.VISIBLE
        ivStart?.visibility = View.VISIBLE
        pbLoading?.visibility = View.GONE
    }
}
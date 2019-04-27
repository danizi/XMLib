package com.xm.lib.media.test.holder

import android.content.Context
import android.os.Environment
import android.view.View
import android.widget.Button
import com.xm.lib.media.R
import com.xm.lib.media.attachment.AttachmentComplete
import com.xm.lib.media.attachment.AttachmentGesture
import com.xm.lib.media.attachment.AttachmentPre
import com.xm.lib.media.attachment.control.AttachmentControl
import com.xm.lib.media.base.XmVideoView
import com.xm.lib.media.cache.M3u8Helper
import com.xm.lib.media.test.MainActivity
import java.io.File

class MediaViewHolder private constructor(val context: Context?, val xmVideoView: XmVideoView, val btnMedia2: Button, val btnSet: Button) {

    init {
        initData()
        initEvent()
    }

    private fun initEvent() {
        //解析m3u8地址
        btnSet.setOnClickListener { v ->
            val m3u8 = "http://hls.videocc.net/26de49f8c2/2/26de49f8c253b3715148ea0ebbb2ad95_1.m3u8"
            M3u8Helper.parseDownUrl(m3u8)
        }
    }

    private fun initData() {
        //    8090, /storage/emulated/0/XmDown
        //绑定数据
        val preUrl = "https://img-blog.csdn.net/20160413112832792?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQv/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center"
        val attachmentPre = AttachmentPre(context, preUrl)
        attachmentPre.url = Environment.getExternalStorageDirectory().toString() + File.separator + "26de49f8c273bbc8f6812d1422a11b39_2.m3u8"  //本地缓存m3u8视频
        attachmentPre.url = "http://hls.videocc.net/26de49f8c2/2/26de49f8c253b3715148ea0ebbb2ad95_1.m3u8"
        val attachmentControl = AttachmentControl(context)
        val attachmentGesture = AttachmentGesture(context)
        val attachmentComplete = AttachmentComplete(context)

        xmVideoView.bindAttachmentView(attachmentPre)      //预览附着页面
        xmVideoView.bindAttachmentView(attachmentControl)  //控制器附着页面
        xmVideoView.bindAttachmentView(attachmentGesture)  //手势附着页面(调节亮度和音量)
        xmVideoView.bindAttachmentView(attachmentComplete) //播放完成附着页面

        xmVideoView.addPlayerObserver(attachmentPre)
        xmVideoView.addGestureObserver(attachmentPre)
        xmVideoView.addPhoneStateObserver(attachmentPre)

        xmVideoView.addPlayerObserver(attachmentControl)
        xmVideoView.addGestureObserver(attachmentControl)
        xmVideoView.addPhoneStateObserver(attachmentControl)

        xmVideoView.addPlayerObserver(attachmentGesture)
        xmVideoView.addGestureObserver(attachmentGesture)
        xmVideoView.addPhoneStateObserver(attachmentGesture)

        xmVideoView.addPlayerObserver(attachmentComplete)
        xmVideoView.addGestureObserver(attachmentComplete)
        xmVideoView.addPhoneStateObserver(attachmentComplete)
    }

    companion object {

        fun create(context: Context?, rootView: View): MediaViewHolder {
            val video = rootView.findViewById<XmVideoView>(R.id.video)
            val btnMedia2 = rootView.findViewById<Button>(R.id.btn_media2)
            val btnSet = rootView.findViewById<Button>(R.id.btn_set)
            return MediaViewHolder(context, video, btnMedia2, btnSet)
        }
    }
}
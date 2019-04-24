package com.xm.lib.media.attachment

import android.content.Context
import android.view.View
import com.xm.lib.media.R
import com.xm.lib.media.base.IXmMediaPlayer
import com.xm.lib.media.base.XmVideoView
import com.xm.lib.media.event.GestureObserver
import com.xm.lib.media.event.PhoneStateObserver
import com.xm.lib.media.event.PlayerObserver

class AttachmentComplete(context: Context?) : BaseAttachmentView(context) {


    init {
        observer = object : PlayerObserver {
            override fun onCompletion(mp: IXmMediaPlayer) {
                super.onCompletion(mp)
                xmVideoView?.bringChildToFront(this@AttachmentComplete)
                this@AttachmentComplete.visibility = View.VISIBLE
            }

            override fun onPrepared(mp: IXmMediaPlayer) {
                super.onPrepared(mp)
                this@AttachmentComplete.visibility = View.GONE
            }
        }
        gestureObserver = object : GestureObserver {

        }
        phoneObserver = object : PhoneStateObserver {}
    }

    override fun bind(xmVideoView: XmVideoView) {
        super.bind(xmVideoView)
        this.visibility = View.GONE
    }

    override fun layoutId(): Int {
        return R.layout.attachment_complete
    }

}
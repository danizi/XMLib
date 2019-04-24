package com.xm.lib.media.attachment

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.xm.lib.media.base.XmVideoView
import com.xm.lib.media.event.GestureObserver
import com.xm.lib.media.event.PhoneStateObserver
import com.xm.lib.media.event.PlayerObserver


abstract class BaseAttachmentView : FrameLayout {

    var observer: PlayerObserver? = null  //观察者
    var gestureObserver: GestureObserver? = null  //观察者
    var phoneObserver: PhoneStateObserver? = null  //观察者
    var xmVideoView: XmVideoView? = null  //播放实例
    var view: View? = null //当前页面View

    constructor(context: Context?) : super(context!!)

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)

    init {
        try {
            view = getView(layoutId())
            addView(view)
            findViews(view)
            initDisplay()
            initEvent()
            initData()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getView(layoutID: Int): View {
        return LayoutInflater.from(context).inflate(layoutID, null, false)
    }

    abstract fun layoutId(): Int

    open fun findViews(view: View?) {}

    open fun initDisplay() {}

    open fun initEvent() {}

    open fun initData() {}

    /**
     * 在调用XmMediaView.bindAttachment()会调用此方法
     */
    open fun bind(xmVideoView: XmVideoView) {
        xmVideoView.addView(this, LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        this.xmVideoView = xmVideoView
    }

    /**
     * 在调用XmMediaView.unBindAttachment()会调用此方法
     */
    open fun unBind() {
        xmVideoView?.removeView(this)
        xmVideoView = null
    }
}
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

/**
 * 附着View基类
 */
abstract class BaseAttachmentView : FrameLayout, IBaseAttachmentView {
    /**
     * 播放器相关回调观察者
     */
    var observer: PlayerObserver? = null
    /**
     * 手势相关回调观察者
     */
    var gestureObserver: GestureObserver? = null
    /**
     * 手机状态相关观察者
     */
    var phoneObserver: PhoneStateObserver? = null
    /**
     * 播放器View
     */
    var xmVideoView: XmVideoView? = null
    /**
     * 当前页面View
     */
    var view: View? = null

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

    override fun getView(layoutID: Int): View {
        return LayoutInflater.from(context).inflate(layoutID, null, false)
    }

    override fun bind(xmVideoView: XmVideoView) {
        xmVideoView.addView(this, LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        this.xmVideoView = xmVideoView
    }

    override fun unBind() {
        xmVideoView?.removeView(this)
        xmVideoView = null
    }
}

/**
 * 播放列表Item点击监听
 */
interface OnPlayListItemClickListener {
    fun item(vid: String?, progress: Int?, view: View?, postion: Int)
}

/**
 * 附着基类方法
 */
interface IBaseAttachmentView {
    /**
     * 获取View
     */
    fun getView(layoutID: Int): View

    /**
     * 布局id
     */
    fun layoutId(): Int

    /**
     * 查看控件
     */
    fun findViews(view: View?) {}

    /**
     * 初始化显示状态
     */
    fun initDisplay() {}

    /**
     * 初始化监听
     */
    fun initEvent() {}

    /**
     * 初始化数据
     */
    fun initData() {}

    /**
     * 在调用XmMediaView.bindAttachment()会调用此方法
     */
    fun bind(xmVideoView: XmVideoView) {}

    /**
     * 在调用XmMediaView.unBindAttachment()会调用此方法
     */
    fun unBind() {}
}
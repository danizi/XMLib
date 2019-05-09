package com.xm.lib.share

import android.app.Activity
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX

/**
 * 文本
 * 图片
 * 音频
 * 视频
 * 网页
 * 数据
 */
abstract class AbsShare(val activity:Activity) {

    abstract fun init(appid: String)

    abstract fun shareImage()
    abstract fun shareText()
    abstract fun shareMusic()
    abstract fun shareVideo()
    abstract fun shareAppData()
    abstract fun shareWebPage()

    protected val mTargetScene = SendMessageToWX.Req.WXSceneSession

    protected fun buildTransaction(type: String?): String {
        return if (type == null) System.currentTimeMillis().toString() else type + System.currentTimeMillis()
    }
}
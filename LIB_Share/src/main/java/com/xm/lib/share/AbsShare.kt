package com.xm.lib.share

import android.app.Activity
import android.graphics.Bitmap
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX

/**
 * 分享抽象基类
 */
abstract class AbsShare(val activity: Activity) {

    abstract fun init(shareConfig: ShareConfig)

    /**
     * 分享图片
     * @param bmp 分享的图片Bitmap对象
     */
    abstract fun shareImage(bmp: Bitmap,scene:Int?=SendMessageToWX.Req.WXSceneSession)

    /**
     * 分享文本
     */
    abstract fun shareText(text: String,scene:Int?=SendMessageToWX.Req.WXSceneSession)

    /**
     * 分享音乐
     * @param thumb 音乐分享封面
     * @param lowBandUrl 音乐分享地址
     * @param title 音乐分享标题
     * @param description 音乐分享描述
     */
    abstract fun shareMusic(thumb: Int, lowBandUrl: String, title: String, description: String,scene:Int?=SendMessageToWX.Req.WXSceneSession)

    /**
     * 分享视频
     * @param thumb 视频分享封面
     * @param videoUrl 视频分享视频
     * @param title 视频分享标题
     * @param description 视频分享描述
     */
    abstract fun shareVideo(thumb: Int, videoUrl: String, title: String, description: String,scene:Int?=SendMessageToWX.Req.WXSceneSession)

    /**
     * 分享app数据
     * @param path 文件地址
     * @param title 分享标题
     * @param description 分享描述
     */
    abstract fun shareAppData(path: String, title: String, description: String,scene:Int?=SendMessageToWX.Req.WXSceneSession)

    /**
     * 分享网页
     * @param thumb 分享应用logo
     * @param webpageUrl 分享网页地址
     * @param title 分享标题
     * @param description 分享内容
     */
    abstract fun shareWebPage(thumb: Int, webpageUrl: String, title: String, description: String,scene:Int?=SendMessageToWX.Req.WXSceneSession)

    protected val mTargetScene = SendMessageToWX.Req.WXSceneSession

    protected fun buildTransaction(type: String?): String {
        return if (type == null) System.currentTimeMillis().toString() else type + System.currentTimeMillis()
    }
}
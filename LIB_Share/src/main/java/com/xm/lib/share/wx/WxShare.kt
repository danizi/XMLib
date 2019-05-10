package com.xm.lib.share.wx

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.tencent.mm.opensdk.modelmsg.*
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.xm.lib.share.AbsShare
import com.xm.lib.share.R
import com.xm.lib.share.ShareConfig

class WxShare(act: Activity) : AbsShare(act) {

    companion object {
        private const val THUMB_SIZE=100
    }

    private var api: IWXAPI? = null

    override fun init(shareConfig: ShareConfig) {
        api = WXAPIFactory.createWXAPI(activity, shareConfig.appid)
        api?.registerApp(shareConfig.appid)  // 将该app注册到微信
    }

    override fun shareImage(bmp:Bitmap, scene: Int?) {
        //val bmp = BitmapFactory.decodeResource(activity.resources, R.drawable.ic_launcher_background)
        val imgObj = WXImageObject(bmp)

        val msg = WXMediaMessage()
        msg.mediaObject = imgObj

        val thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true)
        bmp.recycle()
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true)

        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("img")
        req.message = msg
        req.scene = mTargetScene
        api?.sendReq(req)
    }

    override fun shareText(text: String, scene: Int?) {
        val textObj = WXTextObject()
        textObj.text = text

        val msg = WXMediaMessage()
        msg.mediaObject = textObj
        // msg.title = "Will be ignored";
        msg.description = text
        msg.mediaTagName = "我是mediaTagName啊"

        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("text")
        req.message = msg
        req.scene = mTargetScene

        api?.sendReq(req)
    }

    override fun shareMusic(thumb: Int, lowBandUrl: String, title: String, description: String, scene: Int?) {
        val music = WXMusicObject()
        music.musicLowBandUrl = lowBandUrl

        val msg = WXMediaMessage()
        msg.mediaObject = music
        msg.title = title
        msg.description = description

        val bmp = BitmapFactory.decodeResource(activity.resources, thumb)
        val thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true)
        bmp.recycle()
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true)

        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("music")
        req.message = msg
        req.scene = mTargetScene
        api?.sendReq(req)
    }

    override fun shareVideo(thumb: Int, videoUrl: String, title: String, description: String, scene: Int?) {
        val video = WXVideoObject()
        video.videoUrl = videoUrl

        val msg = WXMediaMessage(video)

        msg.mediaTagName = "mediaTagName"
        msg.messageAction = "MESSAGE_ACTION_SNS_VIDEO#gameseq=1491995805&GameSvrEntity=87929&RelaySvrEntity=2668626528&playersnum=10"

        msg.title = title
        msg.description = description
        val bmp = BitmapFactory.decodeResource(activity.resources, thumb)
        val thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true)
        bmp.recycle()
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true)

        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("video")
        req.message = msg
        req.scene = mTargetScene
        api?.sendReq(req)
    }

    override fun shareAppData(path:String,title: String, description: String, scene: Int?) {
        val gameVideoFileObject = WXGameVideoFileObject()
        //val path = "/sdcard/test_video.mp4"
        gameVideoFileObject.filePath = path

        val msg = WXMediaMessage()
        msg.setThumbImage(Util.extractThumbNail(path, 150, 150, true))
        msg.title = title
        msg.description = description
        msg.mediaObject = gameVideoFileObject

        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("appdata")
        req.message = msg
        req.scene = mTargetScene
        api?.sendReq(req)
    }

    override fun shareWebPage(thumb:Int,webpageUrl:String,title:String,description:String,scene: Int?) {
        val webpage = WXWebpageObject()
        webpage.webpageUrl = webpageUrl
        val msg = WXMediaMessage(webpage)
        msg.title = title
        msg.description = description
        val bmp = BitmapFactory.decodeResource(activity.resources, thumb)
        val thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true)
        bmp.recycle()
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true)

        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("webpage")
        req.message = msg
        req.scene = mTargetScene
        api?.sendReq(req)
    }
}
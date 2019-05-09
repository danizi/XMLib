package com.xm.lib.share.wx

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXImageObject
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.xm.lib.share.AbsShare
import com.xm.lib.share.R

class WxShare(act: Activity) : AbsShare(act) {

    private var api: IWXAPI? = null

    override fun init(appid: String) {
        api = WXAPIFactory.createWXAPI(activity, appid)
        api?.registerApp(appid)// 将该app注册到微信
    }

    override fun shareImage() {
        val bmp = BitmapFactory.decodeResource(getResources(), R.drawable.send_img)
        val imgObj = WXImageObject(bmp)

        val msg = WXMediaMessage()
        msg.mediaObject = imgObj

        val thumbBmp = Bitmap.createScaledBitmap(bmp, /*THUMB_SIZE*/100, /*THUMB_SIZE*/100, true)
        bmp.recycle()
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true)

        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("img")
        req.message = msg
        req.scene = mTargetScene
        api?.sendReq(req)
    }

    override fun shareText() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun shareMusic() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun shareVideo() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun shareAppData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun shareWebPage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
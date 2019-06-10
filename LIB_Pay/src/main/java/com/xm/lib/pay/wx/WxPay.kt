package com.xm.lib.pay.wx

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.google.gson.Gson
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.xm.lib.pay.AbsPay
import com.xm.lib.pay.Channel
import com.xm.lib.pay.OnPayListener
import com.xm.lib.pay.PayConfig
import com.xm.lib.pay.wx.uikit.PayParameters

class WxPay(activity: Activity) : AbsPay(activity) {


    companion object {
        const val ACTION_PAY_SUCCESS = "ACTION_PAY_SUCCESS"
        const val ACTION_PAY_FAILURE = "ACTION_PAY_FAILURE"
        const val ACTION_PAY_CANCEL = "ACTION_PAY_CANCEL"
    }

    init {
        val filter = IntentFilter()
        filter.addAction(ACTION_PAY_SUCCESS)
        filter.addAction(ACTION_PAY_FAILURE)
        filter.addAction(ACTION_PAY_CANCEL)
        activity.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    ACTION_PAY_SUCCESS -> {
                        listener?.onSuccess()
                    }
                    ACTION_PAY_FAILURE -> {
                        listener?.onFailure()
                    }
                    ACTION_PAY_CANCEL->{
                        listener?.onCancel()
                    }
                }
            }

        }, filter)
    }

    private var listener: OnPayListener? = null
    private var api: IWXAPI? = null

    override fun init(payConfig: PayConfig) {
        api = WXAPIFactory.createWXAPI(activity, payConfig.appid)
        api?.registerApp(payConfig.appid) // 将该app注册到微信
    }

    override fun pay(channel: Channel, paramsJson: String, listener: OnPayListener) {
        this.listener = listener
        val request = PayReq()
        val payParameters = Gson().fromJson(paramsJson, PayParameters::class.java)
        request.appId = payParameters?.appid
        request.partnerId = payParameters?.partnerid
        request.prepayId = payParameters?.prepayid
        request.packageValue = payParameters?.packageName
        request.nonceStr = payParameters?.noncestr
        request.timeStamp = payParameters?.timestamp
        request.sign = payParameters?.sign
        api?.sendReq(request)
    }
}
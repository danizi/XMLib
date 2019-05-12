package com.xm.lib.pay.wx.uikit.wxapi

import android.content.Intent
import android.os.Bundle
import android.provider.UserDictionary.Words.APP_ID
import android.support.v7.app.AppCompatActivity
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.xm.lib.common.log.BKLog
import com.xm.lib.pay.wx.WxPay


class WXPayEntryActivity : AppCompatActivity(), IWXAPIEventHandler {
    companion object {
        private const val TAG = "WXPayEntryActivity"
    }

    private var api: IWXAPI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //注册API
        api = WXAPIFactory.createWXAPI(this, APP_ID, false)
        api?.handleIntent(intent, this)
    }

    override fun onReq(baseReq: BaseReq) {

    }

    override fun onResp(baseResp: BaseResp) {
        if (baseResp.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (baseResp.errCode == 0) {
                BKLog.d(TAG, "支付成功")
                sendBroadcast(Intent(WxPay.ACTION_PAY_SUCCESS))
            } else {
                BKLog.d(TAG, "支付失败")
                sendBroadcast(Intent(WxPay.ACTION_PAY_FAILURE))
            }
            finish()
        }
    }
}

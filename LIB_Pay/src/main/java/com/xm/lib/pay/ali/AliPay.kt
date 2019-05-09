package com.xm.lib.pay.ali

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.widget.Toast
import com.alipay.sdk.app.PayTask
import com.xm.lib.pay.AbsPay
import com.xm.lib.pay.Channel
import com.xm.lib.pay.OnPayListener
import com.xm.lib.pay.ali.util.AuthResult
import com.xm.lib.pay.ali.util.PayResult


/**
 * 阿里支付
 */
class AliPay(activity: Activity) : AbsPay(activity) {

    override fun init(APP_ID: String) {

    }

    companion object {
        //支付结果标志位
        const val SDK_PAY_FLAG = 1
        //授权结果标志位
        const val SDK_AUTH_FLAG = 2
    }

    private var listener: OnPayListener? = null

    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {

        private fun onPayResult(map: Map<String, String>) {
            val payResult = PayResult(map)
            /**
             * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            val resultInfo = payResult.result// 同步返回需要验证的信息
            val resultStatus = payResult.resultStatus
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                Toast.makeText(activity, "支付成功", Toast.LENGTH_SHORT).show()
                listener?.onSuccess()
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                Toast.makeText(activity, "支付失败", Toast.LENGTH_SHORT).show()
                listener?.onCancel()
            }
        }

        private fun onAuthResult(map: Map<String, String>) {
            val authResult = AuthResult(map, true)
            val resultStatus = authResult.getResultStatus()

            // 判断resultStatus 为“9000”且result_code
            // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
            if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                // 获取alipay_open_id，调支付时作为参数extern_token 的value
                // 传入，则支付账户为该授权账户
                Toast.makeText(activity,
                        "授权成功\n" + String.format("authCode:%s", authResult.authCode), Toast.LENGTH_SHORT)
                        .show()
            } else {
                // 其他状态值则为授权失败
                Toast.makeText(activity,
                        "授权失败" + String.format("authCode:%s", authResult.authCode), Toast.LENGTH_SHORT).show()

            }
        }

        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SDK_PAY_FLAG -> {
                    onPayResult(msg.obj as Map<String, String>)
                }
                SDK_AUTH_FLAG -> {
                    onAuthResult(msg.obj as Map<String, String>)
                }
            }
        }
    }

    override fun pay(channel: Channel, paramsJson: String, listener: OnPayListener) {
        this.listener = listener
        //从服务器上面获取
        val payRunnable = Runnable {
            val alipay = PayTask(activity)
            val result = alipay.payV2(paramsJson, true)
            val msg = Message()
            msg.what = SDK_PAY_FLAG
            msg.obj = result
            mHandler.sendMessage(msg)
        }
        // 必须异步调用
        val payThread = Thread(payRunnable)
        payThread.start()
    }

}
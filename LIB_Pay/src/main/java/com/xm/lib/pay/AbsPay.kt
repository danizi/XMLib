package com.xm.lib.pay

import android.app.Activity

/**
 * 支付抽象类
 */
abstract class AbsPay(act: Activity) : IPay {
    protected var activity: Activity = act
}

/**
 * 支付接口
 */
interface IPay {
    /**
     * @param channel 渠道号
     * @param paramsJson 支付参数json串
     *                    微信 {"package":"Sign=WXPay","appid":"wxd37fb8ce51a02360","sign":"A1C433367FC6FA1B279DF25579235CC1","partnerid":"1507283191","prepayid":"wx091153404274087ebf63570c2172257868","noncestr":"61acc180b95a42eb8d7822032fc354a9","timestamp":"1557374020"}
     *                    阿里 alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2018010901719722&biz_content=%7B%22body%22%3A%22%E5%B8%AE%E8%AF%BE%E5%A4%A7%E5%AD%A6vip%E6%9C%8D%E5%8A%A11%E5%B9%B4%22%2C%22out_trade_no%22%3A%221905091156300918859%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E5%B8%AE%E8%AF%BE%E5%A4%A7%E5%AD%A6vip%E6%9C%8D%E5%8A%A11%E5%B9%B4%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.1%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=https%3A%2F%2Fapi.tradestudy.cn%2Fv3%2Fnotify%2Fpay%2Falipay&sign=QBFeCXgw7Z1nSC9%2FZrqfrAQx5dm6J%2Bo7gGulrpNwOSe%2B7O2cjzb%2FH2AaLzckO3ZUckbrLMWyEIxc4RMvPjeUygIeknRa%2BshPjckckh%2FFD7UoypvTVS5X9U%2FfJEy1FP%2FK1vV9v89iJ6xaPhYuwLSImJRhlNYv6nEODO6Ypa1W2oDr9M2UV%2FmenvhHyEdqovRmUAVmJzYeSAmx7V%2FDy91ac%2FYT6NisExhsw6cowJgnnYizZsvkJERW1F5%2FvsUa6M23L8Az0RIuqRNgQBEU6EQb%2Fvv174Y%2FK0StCBzbWDXBNpsb1cJNGyI8YQEIJHSX6GZ4TRf83Tf9ZTrIa64EKatgIA%3D%3D&sign_type=RSA2&timestamp=2019-05-09+11%3A56%3A30&version=1.0

     * @param listener 支付监听
     */
    fun pay(channel: Channel, paramsJson: String, listener: OnPayListener)

    /**
     * 微信支付初始化
     * @param payConfig 初始化参数
     */
    fun init(payConfig: PayConfig)

    /**
     * 回收资源
     */
    fun clear()
}

/**
 * 支付的监听
 */
interface OnPayListener {
    fun onSuccess()
    fun onFailure()
    fun onCancel()
}

/**
 * 支付的渠道号
 */
enum class Channel {
    //小米渠道
    XIAOMI,
    //华为渠道
    HUAWEI,
    //一般的渠道
    GENERAL
}

/**
 * 支付的类型
 */
enum class Type {
    //阿里
    ALI,
    //微信
    WX
}
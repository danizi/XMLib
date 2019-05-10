package com.xm.lib.pay

import android.app.Activity

/**
 * 支付抽象类
 */
abstract class AbsPay(act: Activity) {
    protected var activity: Activity = act

    abstract fun pay(channel: Channel, paramsJson: String, listener: OnPayListener)

    /**
     * 微信支付初始化
     */
    abstract fun init(payConfig: PayConfig)
}

/**
 * 监听
 */
interface OnPayListener {
    fun onSuccess()
    fun onFailure()
    fun onCancel()
}

/**
 * 渠道号
 */
enum class Channel {
    XIAOMI,
    HUAWEI,
    GENERAL
}

/**
 * 支付类型
 */
enum class Type {
    ALI,
    WX
}
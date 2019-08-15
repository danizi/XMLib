package com.xm.lib.downloader.v2.imp

import com.xm.lib.downloader.v2.XmDownRequest

/**
 * 下载者
 */
interface Call {
    /**
     * 请求
     */
    fun request(): XmDownRequest

    /**
     * 入队列
     */
    fun enqueue(callback: XmDownInterface.Callback?)

    /**
     * 移除队列
     */
    fun cancel()

    /**
     * 是否取消
     */
    fun isCanceled():Boolean

    /**
     * 是否执行
     */
    fun isExecuted():Boolean

    /**
     * 创建工厂接口
     */
    interface Factory {
        fun newCall(request: XmDownRequest): Call
    }
}
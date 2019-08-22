package com.xm.lib.downloader.v2.imp

import com.xm.lib.downloader.v2.XmDownRequest
import com.xm.lib.downloader.v2.abs.AbsRequest

/**
 * 下载者
 */
interface Call {
    /**
     * 请求
     */
    fun request(): AbsRequest

    /**
     * 入队列
     */
    fun enqueue(callback: XmDownInterface.Callback?)

    /**
     * 移除队列，并删除数据库的记录
     */
    fun cancel()

    /**
     * 移除队列，但“不”删除数据库的记录
     */
    fun pause()

    /**
     * 是否取消
     */
    fun isCanceled(): Boolean

    /**
     * 是否执行
     */
    fun isExecuted(): Boolean

    /**
     * 创建工厂接口
     */
    interface Factory {
        fun newCall(request: AbsRequest): Call
    }
}
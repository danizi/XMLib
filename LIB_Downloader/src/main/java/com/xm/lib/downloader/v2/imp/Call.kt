package com.xm.lib.downloader.v2.imp

import com.xm.lib.downloader.v2.XmRealCall
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
     * 入队列,如果该任务数据库不存在，则添加到数据库中，否则读取数据库数据
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
     * 是否暂停
     */
    fun isPaused(): Boolean

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
        /**
         * 创建call
         */
        fun newCall(request: AbsRequest): Call

        /**
         * 删除call
         */
        fun removeCall(call: XmRealCall)
    }
}
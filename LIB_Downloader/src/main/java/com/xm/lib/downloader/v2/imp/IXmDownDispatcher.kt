package com.xm.lib.downloader.v2.imp

import com.xm.lib.downloader.v2.XmRealCall

/**
 * 分发器
 */
interface IXmDownDispatcher {

    /**
     * 加入队列
     */
    fun enqueue(downRunnable: XmRealCall.DownRunnable)

    /**
     * 任务完成
     */
    fun finished(downRunnable: XmRealCall.DownRunnable)

    /**
     * 取消任务
     */
    fun cancel(downRunnable: XmRealCall.DownRunnable)

    /**
     * 取消所有任务
     */
    fun cancelAll()
}
package com.xm.lib.downloader.v2.imp

import com.xm.lib.downloader.v2.XmRealCall
import com.xm.lib.downloader.v2.abs.AbsRunnable

/**
 * 分发器
 */
interface IXmDownDispatcher {

    /**
     * 加入队列
     * @param downRunnable 下载接口
     */
    fun enqueue(downRunnable: AbsRunnable)

    /**
     * 任务完成
     * @param downRunnable 下载接口
     */
    fun finished(downRunnable: AbsRunnable)

    /**
     * 取消任务
     * @param downRunnable 下载接口
     */
    fun cancel(downRunnable: AbsRunnable)

    /**
     * 取消所有任务
     */
    fun cancelAll()

}
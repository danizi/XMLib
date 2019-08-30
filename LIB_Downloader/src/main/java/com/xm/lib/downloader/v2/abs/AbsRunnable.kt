package com.xm.lib.downloader.v2.abs

abstract class AbsRunnable : Runnable {

    override fun run() {
        execute()
    }

    /**
     * 执行
     */
    abstract fun execute()

    /**
     * 停止线程
     */
    abstract fun cancel()

    /**
     * 获取下载地址信息
     */
    abstract fun getRequestUrl(): String
}
package com.xm.lib.downloader.config

import java.util.concurrent.ExecutorService

class DownConfig {
    companion object {
        const val DEFAULT = 0L
        const val EMPTY_STRING = ""
    }

    var path = EMPTY_STRING
    var dir = EMPTY_STRING

    var downTaskerPool: ExecutorService? = null //下载者线程池
    var isMultiRunnable = false  //启用多线程下载
    var isSingleRunnable = false //启动单线程下载
    var threadNum = DEFAULT   //分段下载线程数量
    var bufferSize = 1024 * 1 //缓存字节大小

    var downDispatcherPool: ExecutorService? = null //下载分发器线程池
    var runqueues = DEFAULT //分发器限定的线程数量
}
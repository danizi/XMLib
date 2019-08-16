package com.xm.lib.downloader.v2.imp

import com.xm.lib.downloader.v2.state.XmDownError

/**
 * 接口集合
 */
interface XmDownInterface {
    /**
     * 下载任务
     */
    interface Callback {

        /**
         * 下载开始
         */
        fun onDownloadStart()

        /**
         * 下载进度
         */
        fun onDownloadProgress(progress: Long, total: Long)

        /**
         * 下载成功
         */
        fun onDownloadSuccess()

        /**
         * 下载错误
         */
        fun onDownloadFailed(error: XmDownError)
    }
}
package com.xm.lib.downloader.v2.imp

import com.xm.lib.downloader.v2.XmDownRequest
import com.xm.lib.downloader.v2.abs.AbsRequest
import com.xm.lib.downloader.v2.state.XmDownError

/**
 * 接口集合
 */
interface XmDownInterface {

    /**
     * 下载监听
     */
    interface Callback {

        /**
         * 下载开始
         */
        fun onDownloadStart(request: AbsRequest, path: String)

        /**
         * 下载取消
         */
        fun onDownloadCancel(request: AbsRequest)

        /**
         * 下载暂停
         */
        fun onDownloadPause(request: AbsRequest)

        /**
         * 下载进度
         */
        fun onDownloadProgress(request: AbsRequest, progress: Long, total: Long)

        /**
         * 下载成功
         */
        fun onDownloadComplete(request: AbsRequest)

        /**
         * 下载错误
         */
        fun onDownloadFailed(request: AbsRequest, error: XmDownError)
    }


}
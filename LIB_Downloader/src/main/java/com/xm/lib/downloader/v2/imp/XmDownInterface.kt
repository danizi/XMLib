package com.xm.lib.downloader.v2.imp

import com.xm.lib.downloader.v2.XmDownRequest
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
        fun onDownloadStart(request: XmDownRequest)

        /**
         * 下载取消
         */
        fun onDownloadCancel(request: XmDownRequest)

        /**
         * 下载进度
         */
        fun onDownloadProgress(request: XmDownRequest,progress: Long, total: Long)

        /**
         * 下载成功
         */
        fun onDownloadComplete(request: XmDownRequest)

        /**
         * 下载错误
         */
        fun onDownloadFailed(request: XmDownRequest, error: XmDownError)
    }
}
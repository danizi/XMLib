package com.xm.lib.downloader.v2

import com.xm.lib.common.log.BKLog
import com.xm.lib.downloader.v2.abs.AbsRequest
import com.xm.lib.downloader.v2.db.XmDownDaoBean
import com.xm.lib.downloader.v2.imp.Call
import com.xm.lib.downloader.v2.imp.XmDownInterface
import com.xm.lib.downloader.v2.runnable.DownRunnable
import com.xm.lib.downloader.v2.state.XmDownError
import com.xm.lib.downloader.v2.state.XmDownState
import java.io.File

/**
 * 下载请求呼叫
 */
class XmRealCall(private var client: XmDownClient, private val request: AbsRequest) : Call {

    private var callback: XmDownInterface.Callback? = null
    private var isExecuted = false
    private var isCanceled = false
    private var isPaused = false
    lateinit var downRunnable: DownRunnable

    /**
     * 回调处理
     */
    private var callbackHandler = object : XmDownInterface.IXmRealCallListener {

        override fun checkComplete(): Boolean {
            val downDaoBeans = client.dao?.select(request.url!!)
            if (downDaoBeans?.size!! > 2) {
                BKLog.d(DownRunnable.TAG, "${request.url}运行中数据库中超过两个任务...")
                throw Exception("${request.url}数据库中超过两个任务")
            }

            if (downDaoBeans.size == 1) {
                if (downDaoBeans[0].state == XmDownState.COMPLETE) {
                    callback?.onDownloadComplete(request)
                    client.dispatcher?.finished(downRunnable)
                    client.dao?.updateComplete(request.url)
                    return true
                }
            }
            return false
        }

        override fun checkSpace(): Boolean {
            return false
        }

        override fun onDownloadStart(request: AbsRequest, path: String) {
            //callback?.onDownloadStart(request, (client.dir + File.separator + request.fileName))
            //client.dao?.insert(getXmDownDaoBean(request, (client.dir + File.separator + request.fileName)))
            callback?.onDownloadStart(request, path)
            client.dao?.insert(getXmDownDaoBean(request, path))
        }

        /**
         * 将请求信息包装成数据库bean，这样便于数据的操作
         */
        private fun getXmDownDaoBean(request: AbsRequest, path: String): XmDownDaoBean {
            var xmDownDaoBean = XmDownDaoBean()
            //先读取缓存中的数据
            if (client.dao?.select(request.url!!)?.size == 1) {
                xmDownDaoBean = client.dao?.select(request.url!!)!![0]
                xmDownDaoBean.path = path
            } else {
                xmDownDaoBean.progress = 0
                xmDownDaoBean.total = 0
                xmDownDaoBean.url = request.url!!
                xmDownDaoBean.fileName = request.fileName!!
                xmDownDaoBean.path = path
                xmDownDaoBean.state = XmDownState.START
                xmDownDaoBean.isEdit = false
                xmDownDaoBean.isSelect = false
            }
            return xmDownDaoBean
        }

        override fun onDownloadCancel(request: AbsRequest) {
            callback?.onDownloadCancel(request)
            client.dao?.delete(request.url)
        }

        override fun onDownloadPause(request: AbsRequest) {
            callback?.onDownloadPause(request)
            client.dispatcher?.finished(downRunnable)
            client.removeCall(this@XmRealCall)
        }

        override fun onDownloadProgress(request: AbsRequest, progress: Long, total: Long) {
            BKLog.d(DownRunnable.TAG, "${request.url}运行中... progress:$progress")
            callback?.onDownloadProgress(request, progress, total)
            client.dao?.updateProgress(request.url, progress, total)
        }

        override fun onDownloadComplete(request: AbsRequest) {
            callback?.onDownloadComplete(request)
            client.dao?.updateComplete(request.url)
            client.dispatcher?.finished(downRunnable)
            client.removeCall(this@XmRealCall)
        }

        override fun onDownloadFailed(request: AbsRequest, error: XmDownError) {
            callback?.onDownloadFailed(request, error)
            client.dao?.updateFailed(request.url, error)
            client.dispatcher?.finished(downRunnable)
            client.removeCall(this@XmRealCall)
        }
    }

    companion object {
        fun newXmRealDown(client: XmDownClient, request: AbsRequest): XmRealCall {
            return XmRealCall(client, request)
        }
    }

    override fun request(): AbsRequest {
        return request
    }

    override fun enqueue(callback: XmDownInterface.Callback?) {
        this.downRunnable = DownRunnable(callbackHandler, this, client, request, callback)
        this.client.dispatcher?.enqueue(downRunnable)
        this.isExecuted = true
        this.callback = callback

        callbackHandler.onDownloadStart(request, (client.dir + File.separator + request.fileName))

    }

    override fun cancel() {
        this.downRunnable.cancel()
        this.isCanceled = true

        callbackHandler.onDownloadCancel(request)
    }

    override fun pause() {
        this.downRunnable.cancel()
        this.isPaused = true

        callbackHandler.onDownloadPause(request)
    }

    override fun isPaused(): Boolean {
        return isPaused
    }

    override fun isCanceled(): Boolean {
        return isCanceled
    }

    override fun isExecuted(): Boolean {
        return isExecuted
    }
}
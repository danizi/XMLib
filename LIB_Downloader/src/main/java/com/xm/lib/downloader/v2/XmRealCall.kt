package com.xm.lib.downloader.v2

import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.file.FileUtil
import com.xm.lib.downloader.v2.abs.AbsRequest
import com.xm.lib.downloader.v2.db.XmDownDaoBean
import com.xm.lib.downloader.v2.imp.Call
import com.xm.lib.downloader.v2.imp.XmDownInterface
import com.xm.lib.downloader.v2.state.XmDownError
import com.xm.lib.downloader.v2.state.XmDownState
import java.io.BufferedInputStream
import java.io.File
import java.io.InputStream
import java.io.RandomAccessFile
import java.net.HttpURLConnection
import java.net.URL

/**
 * 下载请求呼叫
 */
class XmRealCall(private var client: XmDownClient, private val request: AbsRequest) : Call {

    private var callback: XmDownInterface.Callback? = null
    private var isExecuted = false
    private var isCanceled = false
    private var isPaused = false
    private lateinit var downRunnable: DownRunnable

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
            callback?.onDownloadStart(request, (client.dir + File.separator + request.fileName))
            client.dao?.insert(getXmDownDaoBean(request, (client.dir + File.separator + request.fileName)))
        }

        override fun onDownloadCancel(request: AbsRequest) {
            callback?.onDownloadCancel(request)
            client.dao?.delete(request.url)
        }

        override fun onDownloadPause(request: AbsRequest) {
            callback?.onDownloadPause(request)
            client.dispatcher?.finished(downRunnable)
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
        }

        override fun onDownloadFailed(request: AbsRequest, error: XmDownError) {
            callback?.onDownloadFailed(request, error)
            client.dao?.updateFailed(request.url, error)
            client.dispatcher?.finished(downRunnable)
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

    /**
     * 下载网络数据接口
     */
    class DownRunnable(private val callbackHandler: XmDownInterface.IXmRealCallListener?, private val call: XmRealCall, private val client: XmDownClient, private val request: AbsRequest, private val callback: XmDownInterface.Callback?) : Runnable {

        companion object {
            const val TAG = "DownRunnable"
        }

        private var total = 0L
        private var progress = 0L
        private var unDownLoadedData = 0L
        private var cancel = false
        private var readTimeout = 15000
        private var connectTimeout = 15000

        fun getRequestUrl(): String {
            return request.url!!
        }

        /**
         * 遇到问题:
         * 1 conn.responseCode 等于-1还是能访问网络数据
         * 2 RandomAccessFile为null会一直卡住的
         */
        override fun run() {
            //downStateType = DownStateType.START
            //判断缓存是否完成
            if (callbackHandler?.checkComplete()!!) return

            //创建任务缓存文件
            val downFile = File(client.dir + File.separator + request.fileName)
            var downRaf: RandomAccessFile? = null
            var downFileExists = false
            if (!downFile.exists()) {
                if (!FileUtil.createNewFile(downFile)) {
                    //创建文件“错误”回调
                    callbackHandler.onDownloadFailed(request, XmDownError.CREATE_FILE_ERROR)
                    return
                }
            }
            downFileExists = true
            downRaf = RandomAccessFile(downFile, "rw")
            progress = downRaf.length()
            downRaf.seek(progress)
            BKLog.d(TAG, "${downFile.absolutePath} 已存在，直接读取缓存文件当前的长度$progress")

            //请求网络数据
            val conn = URL(request.url).openConnection() as HttpURLConnection
            var inputStream: InputStream? = null
            try {
                conn.requestMethod = "GET"
                conn.readTimeout = readTimeout
                conn.connectTimeout = connectTimeout
                conn.doInput = true
                /**
                 *  "bytes=$rangeStartIndex-"
                 *  "bytes=$rangeStartIndex-$rangeEndIndex"
                 */
                conn.setRequestProperty("Range", "bytes=$progress-")
                conn.setRequestProperty("Accept-Encoding", "identity")
                conn.connect()
                inputStream = conn.inputStream

                //contentLength获取必须在conn.inputStream之后
                total = if (progress == 0L) {
                    if (conn.contentLength == -1) {
                        throw IllegalArgumentException("conn.contentLength==-1")
                    }
                    conn.contentLength.toLong()
                } else {
                    progress + conn.contentLength.toLong()
                }

                if (!downFileExists) {
                    BKLog.d(TAG, "*********************************************************")
                    BKLog.d(TAG, "文件总大小 : ${FileUtil.getSizeUnit(total)}")
                    BKLog.d(TAG, "                                                         ")
                } else {
                    unDownLoadedData = conn.contentLength.toLong()
                    BKLog.d(TAG, "*********************************************************")
                    BKLog.d(TAG, "文件总大小 : ${FileUtil.getSizeUnit(total)}")
                    BKLog.d(TAG, "剩余未下载文件大小 : ${FileUtil.getSizeUnit(unDownLoadedData)}")
                    BKLog.d(TAG, "                                                         ")
                }
                writeIntoLocal(inputStream, downRaf)
            } catch (e: Exception) {
                e.printStackTrace()
                //请求网络数据“错误”回调
                callbackHandler.onDownloadFailed(request, XmDownError.UNKNOWN)
            } finally {
                inputStream?.close()
                client.dispatcher?.finished(call.downRunnable)
            }
        }

        private fun writeIntoLocal(inputStream: InputStream?, raf: RandomAccessFile?) {
            if (inputStream == null) {
                throw NullPointerException("inputStream is null ,writeIntoLocal failure")
            }
            val bis: BufferedInputStream? = BufferedInputStream(inputStream)
            try {
                val buff = ByteArray(1024 * 4)
                var length: Int
                while (true) {
                    length = bis?.read(buff)!!
                    if (length == -1 || cancel) {
                        break
                    }
                    raf?.write(buff, 0, length)
                    progress += length
                    //数据写入本地“进度”回调
                    callbackHandler?.onDownloadProgress(request, progress, total)
                }

                //数据写入本地“完成”回调
                if (!cancel) {
                    callbackHandler?.onDownloadComplete(request)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                //数据写入本地“错误”回调
                callbackHandler?.onDownloadFailed(request, XmDownError.UNKNOWN)
            } finally {
                bis?.close()
                raf?.close()
            }
        }

        fun cancel() {
            cancel = true
            client.dispatcher?.cancel(this)
        }
    }
}
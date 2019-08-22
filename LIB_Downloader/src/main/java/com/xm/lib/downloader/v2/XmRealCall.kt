package com.xm.lib.downloader.v2

import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.file.FileUtil
import com.xm.lib.downloader.v2.abs.AbsRequest
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
    private lateinit var downRunnable: DownRunnable

    companion object {
        fun newXmRealDown(client: XmDownClient, request: AbsRequest): XmRealCall {
            return XmRealCall(client, request)
        }
    }

    override fun request(): AbsRequest {
        return request
    }

    override fun enqueue(callback: XmDownInterface.Callback?) {
        this.downRunnable = DownRunnable(this, client, request, callback)
        this.client.dispatcher?.enqueue(downRunnable)
        this.isExecuted = true
        this.callback = callback
        callback?.onDownloadStart(request, (client.dir + File.separator + request.fileName))
    }

    override fun cancel() {
        this.downRunnable.cancel()
        this.callback?.onDownloadCancel(request)
    }

    override fun pause() {
        this.downRunnable.cancel()
        this.callback?.onDownloadPause(request)
    }

    override fun isCanceled(): Boolean {
        if (isExecuted) {
            return true
        }
        return false
    }

    override fun isExecuted(): Boolean {
        return isExecuted
    }

    /**
     * 下载网络数据接口
     */
    class DownRunnable(private val call: XmRealCall, private val client: XmDownClient, private val request: AbsRequest, private val callback: XmDownInterface.Callback?) : Runnable {
        //private var downStateType: DownStateType? = null
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
            val downFile = File(client.dir + File.separator + request.fileName)
            var downRaf: RandomAccessFile? = null
            var downFileExists = false
            val downDaoBeans = client.dao?.select(request.url!!)
            if (downDaoBeans?.size!! > 2) {
                BKLog.d(TAG, "${request.url}运行中数据库中超过两个任务...")
                throw Exception("${request.url}数据库中超过两个任务")
            }

            if (downDaoBeans.size == 1) {
                if (downDaoBeans[0].state == XmDownState.COMPLETE) {
                    callback?.onDownloadComplete(request)
                    //client.dispatcher?.finished(call.downRunnable)
                    client.dispatcher?.finished(this)
                    return
                }
            }

            //创建任务缓存文件
            if (!downFile.exists()) {
                if (!FileUtil.createNewFile(downFile)) {
                    call.callback?.onDownloadFailed(request, XmDownError.CREATE_FILE_ERROR)
                    //downStateType = DownStateType.ERROR
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
//                /**
//                 *  -1 等于-1也成功了，不知道为什么
//                 *  1xx: Informational
//                 *  2xx: Success
//                 *  3xx: Redirection
//                 *  4xx: Client XmDownError
//                 *  5xx: Server XmDownError
//                 */
//                when (conn.responseCode) {
//                    -1 or 2 or 3  -> {
//                        writeIntoLocal(inputStream, downRaf)
//                    }
//                    4 -> {
//                        callback?.onDownloadFailed(request, XmDownError.CLIENT)
//                    }
//                    5 -> {
//                        callback?.onDownloadFailed(request, XmDownError.SERVER)
//                    }
//                }
                writeIntoLocal(inputStream, downRaf)
            } catch (e: Exception) {
                e.printStackTrace()
                callback?.onDownloadFailed(request, XmDownError.UNKNOWN)
                //client.dispatcher?.finished(call.downRunnable)
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
                    BKLog.d(TAG, "${request.url}运行中... progress:$progress")
                    call.callback?.onDownloadProgress(request, progress, total)
                    //downStateType = DownStateType.RUNNING
                }

                if (!cancel) {
                    call.callback?.onDownloadComplete(request)
                    client.dispatcher?.finished(this)
                    //downStateType = DownStateType.COMPLETE
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.callback?.onDownloadFailed(request, XmDownError.UNKNOWN)
                client.dispatcher?.finished(this)
            } finally {
                bis?.close()
                raf?.close()
            }
        }

        fun cancel() {
            cancel = true
            client.dispatcher?.cancel(this)
            //downStateType = DownStateType.PAUSE
        }
    }
}
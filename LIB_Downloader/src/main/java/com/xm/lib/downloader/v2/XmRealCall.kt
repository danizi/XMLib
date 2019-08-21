package com.xm.lib.downloader.v2

import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.file.FileUtil
import com.xm.lib.downloader.enum_.DownStateType
import com.xm.lib.downloader.v2.imp.Call
import com.xm.lib.downloader.v2.imp.XmDownInterface
import com.xm.lib.downloader.v2.state.XmDownError
import com.xm.lib.downloader.v2.state.XmDownState
import okhttp3.OkHttpClient
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class XmRealCall(private var client: XmDownClient, val request: XmDownRequest) : Call {

    private var callback: XmDownInterface.Callback? = null
    private var isExecuted = false
    private lateinit var downRunnable: DownRunnable

    companion object {
        fun newXmRealDown(client: XmDownClient, request: XmDownRequest): XmRealCall {
            return XmRealCall(client, request)
        }
    }

    override fun request(): XmDownRequest {
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

    override fun isCanceled(): Boolean {
        if (isExecuted) {
            return true
        }
        return false
    }

    override fun isExecuted(): Boolean {
        return isExecuted
    }

    class DownRunnable(
            private val call: XmRealCall,
            private val client: XmDownClient,
            val request: XmDownRequest,
            private val callback: XmDownInterface.Callback?) : Runnable {

        private var downStateType: DownStateType? = null

        companion object {
            const val TAG = "DownRunnable"
        }

        private var total = 0L
        private var unDownLoadedData = 0L
        private var cancel = false
        private var readTimeout = 15000
        private var connectTimeout = 15000
        var progress = 0L

        override fun run() {

            downStateType = DownStateType.START

            //判断缓存是否完成
            val downFile = File(client.dir + File.separator + request.fileName)
            var downRaf: RandomAccessFile? = null
            var downFileExists = false
            val downDaoBeans = client.dao?.select(request.url!!)
            if (downDaoBeans?.size!! > 0) {
                if (downDaoBeans[0].state == XmDownState.COMPLETE) {
                    callback?.onDownloadComplete(request)
                    client.dispatcher?.finished(this)
                    return
                }

                if (!downFile.exists()) {
                    throw IllegalArgumentException()
                }

                downFileExists = true
                BKLog.d(TAG, "${downFile.absolutePath} 已存在，直接读取缓存文件当前的长度")
                progress = downRaf?.length()!!
            } else {
                //新任务，创建文件
                if (!FileUtil.createNewFile(downFile)) {
                    call.callback?.onDownloadFailed(request, XmDownError.CREATE_FILE_ERROR)
                    downStateType = DownStateType.ERROR
                    return
                }
            }
            downRaf = RandomAccessFile(downFile, "rw")

            //请求网络数据
            val conn = URL(request.url).openConnection() as HttpURLConnection
            var inputStream: InputStream? = null
            try {
                conn.requestMethod = "GET"
                conn.readTimeout = readTimeout
                conn.connectTimeout = connectTimeout
                conn.doInput = true
                conn.setRequestProperty("Accept-Encoding", "identity")

                /**
                 *  "bytes=$rangeStartIndex-"
                 *  "bytes=$rangeStartIndex-$rangeEndIndex"
                 */
                conn.setRequestProperty("Range", "bytes=$progress-")
                conn.connect()

                inputStream = conn.inputStream

                //contentLength获取必须在conn.inputStream之后
                if (progress == 0L) {
                    if (conn.contentLength == -1) {
                        throw IllegalArgumentException("conn.contentLength==-1")
                    }
                    total = conn.contentLength.toLong()
                    client.dao?.updateTotal(request.url, total)
                } else {
                    total = progress + conn.contentLength.toLong()
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


                /**
                 *  1xx: Informational
                 *  2xx: Success
                 *  3xx: Redirection
                 *  4xx: Client XmDownError
                 *  5xx: Server XmDownError
                 */
                when (conn.responseCode) {
                    -1/* or 2 or 3 */ -> {
                        writeIntoLocal(inputStream, downRaf)
                    }
                    4 -> {
                        callback?.onDownloadFailed(request, XmDownError.CLIENT)
                    }
                    5 -> {
                        callback?.onDownloadFailed(request, XmDownError.SERVER)
                    }
                }
                writeIntoLocal(inputStream, downRaf)
            } catch (e: Exception) {
                e.printStackTrace()
                callback?.onDownloadFailed(request, XmDownError.UNKNOWN)
            } finally {
                inputStream?.close()
                client.dispatcher?.finished(this)
            }
        }


        private fun writeIntoLocal(inputStream: InputStream?, raf: RandomAccessFile?) {
            if (inputStream == null) {
                throw NullPointerException("inputStream is null ,writeIntoLocal failure")
            }
            val bis: BufferedInputStream? = BufferedInputStream(inputStream)
            try {
                val buff = ByteArray(1024 * 4)
                var length = 0
                while (true) {
                    length = bis?.read(buff)!!
                    if (length == -1 || cancel) {
                        break
                    }
                    raf?.write(buff, 0, length)
                    progress += length
                    call.callback?.onDownloadProgress(request, progress, total)
                    downStateType = DownStateType.RUNNING
                }

                call.callback?.onDownloadComplete(request)
                //client.builder.dispatcher?.finished(call.downRunnable)
                client.dispatcher?.finished(this)
                downStateType = DownStateType.COMPLETE

            } catch (e: Exception) {
                e.printStackTrace()
                call.callback?.onDownloadFailed(request, XmDownError.UNKNOWN)

            } finally {
                bis?.close()
                raf?.close()
            }
        }

        fun cancel() {
            cancel = true
            //client.builder.dispatcher?.cancel(call.downRunnable)
            client.dispatcher?.cancel(this)
            downStateType = DownStateType.PAUSE
        }
    }
}
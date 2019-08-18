package com.xm.lib.downloader.v2

import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.file.FileUtil
import com.xm.lib.downloader.enum_.DownStateType
import com.xm.lib.downloader.v2.imp.Call
import com.xm.lib.downloader.v2.imp.XmDownInterface
import com.xm.lib.downloader.v2.state.XmDownError
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
        this.client.builder.dispatcher?.enqueue(downRunnable)
        this.isExecuted = true
        this.callback = callback
        callback?.onDownloadStart()
    }

    override fun cancel() {
        this.downRunnable.cancel()
        this.callback?.onDownloadCancle()
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

        override fun run() {

            //创建文件
            val downFile = File(client.builder.dir + File.separator + request.b.fileName)
            val downFileExists = downFile.exists()
            if (downFileExists) {
                BKLog.d(TAG, "${downFile.absolutePath} 已存在，直接读取缓存文件当前的长度")
            } else {
                if (!FileUtil.createNewFile(downFile)) {
                    call.callback?.onDownloadFailed(XmDownError.CREATE_FILE_ERROR)
                    downStateType = DownStateType.ERROR
                    return
                }
            }
            val downRaf = RandomAccessFile(downFile, "rw")
            progress = downRaf.length()

            val conn = URL(request.b.url).openConnection() as HttpURLConnection
            var inputStream: InputStream? = null
            try {
                conn.requestMethod = "GET"
                conn.readTimeout = 15000
                conn.connectTimeout = 15000
                conn.doInput = true
                conn.setRequestProperty("Accept-Encoding", "identity")
                conn.connect()
                /**
                 *  "bytes=$rangeStartIndex-"
                 *  "bytes=$rangeStartIndex-$rangeEndIndex"
                 */
                //conn.setRequestProperty("Range", "")
                inputStream = conn.inputStream
                if (!downFileExists) {
                    total = conn.contentLength.toLong()

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

                downStateType = DownStateType.START

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
                        callback?.onDownloadFailed(XmDownError.CLIENT)
                    }
                    5 -> {
                        callback?.onDownloadFailed(XmDownError.SERVER)
                    }
                }
                writeIntoLocal(inputStream, downRaf)
            } catch (e: Exception) {
                e.printStackTrace()
                callback?.onDownloadFailed(XmDownError.UNKNOWN)
            } finally {
                inputStream?.close()
            }
        }

        var progress = 0L
        private fun writeIntoLocal(inputStream: InputStream?, raf: RandomAccessFile?) {
            if (inputStream == null) {
                throw NullPointerException("inputStream is null ,writeIntoLocal failure")
            }
            val bis: BufferedInputStream? = BufferedInputStream(inputStream)
            try {
                val buff = ByteArray(1024)
                var length = 0
                while (true) {
                    length = bis?.read(buff)!!
                    if (length == -1 || cancel) {
                        break
                    }
                    raf?.write(buff, 0, length)
                    progress += length
                    call.callback?.onDownloadProgress(progress, 100)
                    downStateType = DownStateType.RUNNING
                }

                call.callback?.onDownloadSuccess()
                client.builder.dispatcher?.finished(call.downRunnable)
                downStateType = DownStateType.COMPLETE

            } catch (e: Exception) {
                e.printStackTrace()
                call.callback?.onDownloadFailed(XmDownError.UNKNOWN)

            } finally {
                bis?.close()
                raf?.close()
            }
        }

        fun cancel() {
            cancel = true
            client.builder.dispatcher?.cancel(call.downRunnable)
            downStateType = DownStateType.PAUSE
        }
    }
}
package com.xm.lib.downloader.v2

import com.xm.lib.downloader.v2.imp.Call
import com.xm.lib.downloader.v2.imp.XmDownInterface
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class XmRealCall(var client: XmDownClient, val request: XmDownRequest) : Call {

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
        downRunnable = DownRunnable(this, client, request, callback)
        client.builder.dispatcher?.enqueue(downRunnable)
        isExecuted = true
    }

    override fun cancel() {
        downRunnable.cancel()
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
            private val request: XmDownRequest,
            private val callback: XmDownInterface.Callback?) : Runnable {

        private var cancel = false

        override fun run() {
            val conn = URL(request.b.url).openConnection() as HttpURLConnection
            var inputStream: InputStream? = null
            try {
                conn.requestMethod = "GET"
                conn.readTimeout = 15000
                conn.connectTimeout = 15000
                conn.doInput = true
                conn.setRequestProperty("Accept-Encoding", "identity")
                /**
                 *  "bytes=$rangeStartIndex-"
                 *  "bytes=$rangeStartIndex-$rangeEndIndex"
                 */
                conn.setRequestProperty("Range", "")
                inputStream = conn.inputStream

                conn.contentLength

                /**
                 *  1xx: Informational
                 *  2xx: Success
                 *  3xx: Redirection
                 *  4xx: Client Error
                 *  5xx: Server Error
                 */
                if (conn.responseCode % 100 == 2 || conn.responseCode % 100 == 3) {

                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                inputStream?.close()
            }
        }

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
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                bis?.close()
                raf?.close()
            }
        }

        fun cancel() {
            cancel = true
            client.builder.dispatcher?.cancel(call.downRunnable)
        }
    }

}
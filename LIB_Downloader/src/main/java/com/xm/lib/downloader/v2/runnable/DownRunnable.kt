package com.xm.lib.downloader.v2.runnable

import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.file.FileUtil
import com.xm.lib.downloader.v2.XmDownClient
import com.xm.lib.downloader.v2.XmRealCall
import com.xm.lib.downloader.v2.abs.AbsRequest
import com.xm.lib.downloader.v2.abs.AbsRunnable
import com.xm.lib.downloader.v2.imp.XmDownInterface
import com.xm.lib.downloader.v2.state.XmDownError
import java.io.BufferedInputStream
import java.io.File
import java.io.InputStream
import java.io.RandomAccessFile
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.atomic.AtomicBoolean

class DownRunnable(private val callbackHandler: XmDownInterface.IXmRealCallListener?, private val call: XmRealCall, private val client: XmDownClient, private val request: AbsRequest, private val callback: XmDownInterface.Callback?) : AbsRunnable() {

    companion object {
        const val TAG = "DownRunnable"
    }

    private var total = 0L
    private var progress = 0L
    private var unDownLoadedData = 0L
    private var cancel = AtomicBoolean(false)
    private var readTimeout = 15000
    private var connectTimeout = 15000

    override fun getRequestUrl(): String {
        return request.url!!
    }

    /**
     * 遇到问题:
     * 1 conn.responseCode 等于-1还是能访问网络数据
     * 2 RandomAccessFile为null会一直卡住的
     */
    override fun execute() {
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
            //client.dispatcher?.finished(call.downRunnable)
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
                if (length == -1 || cancel.get()) {
                    break
                }
                raf?.write(buff, 0, length)
                progress += length
                //数据写入本地“进度”回调
                callbackHandler?.onDownloadProgress(request, progress, total)
            }

            //数据写入本地“完成”回调
            if (!cancel.get()) {
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

    override fun cancel() {
        cancel.set(true)
        //client.dispatcher?.cancel(this)
    }
}

package com.xm.lib.downloader.v2m3u8

import android.content.Context
import android.os.Environment
import com.xm.lib.downloader.v2.XmDownClient
import com.xm.lib.downloader.v2.XmDownRequest
import com.xm.lib.downloader.v2.abs.AbsRequest
import com.xm.lib.downloader.v2.imp.XmDownInterface
import com.xm.lib.downloader.v2.state.XmDownError
import java.io.File

class M3u8DownLoader {
    private var context: Context? = null
    private var client: XmDownClient? = null

    init {
        val dir = Environment.getExternalStorageState() + File.separator + "XmDown" + File.separator + "M3u8" + File.separator
        client = XmDownClient.Builder()
                .context(context!!)
                .dir(dir)
                .build()
    }

    fun down() {

        //首先获取m3u8

        val request = XmDownRequest.Builder()
                .url("")
                .fileName("")
                .build()
        val call = client?.newCall(request)

        call?.enqueue(object :XmDownInterface.Callback{
            override fun onDownloadStart(request: AbsRequest, path: String) {

            }

            override fun onDownloadCancel(request: AbsRequest) {

            }

            override fun onDownloadPause(request: AbsRequest) {

            }

            override fun onDownloadProgress(request: AbsRequest, progress: Long, total: Long) {

            }

            override fun onDownloadComplete(request: AbsRequest) {

            }

            override fun onDownloadFailed(request: AbsRequest, error: XmDownError) {

            }
        })
    }
}
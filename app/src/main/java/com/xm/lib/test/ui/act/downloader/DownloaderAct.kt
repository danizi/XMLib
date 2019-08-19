package com.xm.lib.test.ui.act.downloader

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import com.xm.lib.common.log.BKLog
import com.xm.lib.downloader.utils.CommonUtil
import com.xm.lib.downloader.v2.XmDownClient
import com.xm.lib.downloader.v2.XmDownRequest
import com.xm.lib.downloader.v2.db.XmDownDaoBean
import com.xm.lib.downloader.v2.imp.XmDownInterface
import com.xm.lib.downloader.v2.state.XmDownError
import com.xm.lib.downloader.v2.state.XmDownState
import com.xm.lib.test.R
import java.io.File

/**
 * 下载模块相关测试
 */
class DownloaderAct : AppCompatActivity() {

    companion object {
        const val TAG = "DownloaderAct"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_downloader)
        //startActivity(Intent(this, DownloaderActivity::class.java))

        val downClient = XmDownClient.Builder()
                .context(this)
                .breakpoint(true)
                .dir(Environment.getExternalStorageDirectory().absolutePath + File.separator + "XmDown")
                .runMaxQueuesNum(1)
                .build()

        val downUrls = arrayOf(
                "http://e.hiphotos.baidu.com/image/pic/item/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg",
                "https://apk.apk.xgdown.com/down/1hd.apk",
                "https://dl.hz.37.com.cn/upload/1_1002822_10664/shikongzhiyiH5_10664.apk",
                "https://cavedl.leiting.com/full/caveonline_M141859.apk"
        )

        for (url in downUrls) {

            val call = downClient.newCall(XmDownRequest.Builder()
                    .url(url)
                    .fileName(CommonUtil.getFileName(url))
                    .build())

            call.enqueue(object : XmDownInterface.Callback {
                override fun onDownloadStart(request: XmDownRequest) {
                    BKLog.d(TAG, "onDownloadStart")
                    downClient.builder.dao?.insert(XmDownDaoBean.newXmDownDaoBean(XmDownState.START, request))
                }

                override fun onDownloadCancel(request: XmDownRequest) {
                    BKLog.d(TAG, "onDownloadCancel")
                    downClient.builder.dao?.delete(request.url!!)
                }

                override fun onDownloadProgress(request: XmDownRequest, progress: Long, total: Long) {
                    BKLog.d(TAG, "onDownloadProgress progress : $progress total : $total")
                    downClient.builder.dao?.updateProgress(request.url, progress.toInt())
                }

                override fun onDownloadComplete(request: XmDownRequest) {
                    BKLog.d(TAG, "onDownloadSuccess")
                    downClient.builder.dao?.updateComplete(request.url)
                }

                override fun onDownloadFailed(request: XmDownRequest, error: XmDownError) {
                    BKLog.d(TAG, "onDownloadFailed $error")
                    downClient.builder.dao?.updateFailed(request.url,error)
                }
            })
        }
    }
}

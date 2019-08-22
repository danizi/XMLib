package com.xm.lib.test.contract

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import android.os.Handler
import android.os.Message
import com.xm.lib.common.log.BKLog
import com.xm.lib.downloader.utils.CommonUtil
import com.xm.lib.downloader.v2.XmDownClient
import com.xm.lib.downloader.v2.XmDownRequest
import com.xm.lib.downloader.v2.abs.AbsRequest
import com.xm.lib.downloader.v2.db.XmDownDaoBean
import com.xm.lib.downloader.v2.imp.Call
import com.xm.lib.downloader.v2.imp.XmDownInterface
import com.xm.lib.downloader.v2.state.XmDownError
import com.xm.lib.downloader.v2.state.XmDownState
import com.xm.lib.test.ui.act.downloader.DownloaderAct
import java.io.File

class DownloaderActContract {

    companion object {
        const val TAG = "DownloaderAct"
    }

    interface V {
        fun showToast(s: String)

        fun onDownloadStart(obj: XmDownDaoBean)

        fun onDownloadCancel(obj: XmDownDaoBean)

        fun onDownloadPause(obj: XmDownDaoBean)

        fun onDownloadProgress(obj: XmDownDaoBean)

        fun onDownloadComplete(obj: XmDownDaoBean)

        fun onDownloadFailed(obj: XmDownDaoBean)

    }

    class M

    class P(private val context: Context, private val v: V) {
        val m = M()
        private var calls = ArrayList<Call>()
        private var downClient: XmDownClient? = null
        private var downIndex = 0
        private val downUrls = arrayOf(
                "https://dl.hz.37.com.cn/upload/1_1002822_10664/shikongzhiyiH5_10664.apk",
                "http://e.hiphotos.baidu.com/image/pic/item/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg",
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2867164189,4027291360&fm=11&gp=0.jpg",
                "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1778670421,3855406236&fm=26&gp=0.jpg",
                "https://cavedl.leiting.com/full/caveonline_M141859.apk",
                "https://apk.apk.xgdown.com/down/1hd.apk"
        )
        private val rvUpdateHandler = @SuppressLint("HandlerLeak")
        object : Handler() {

            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                when (msg?.what) {

                    parseInt(XmDownState.START) -> {
                        val obj = getXmDownDaoBean(msg.obj)
                        downClient?.dao?.insert(obj)  //插入缓存数据库，注意：任务不存在才插入
                        v.onDownloadStart(obj)  //更新任务状态UI界面
                        BKLog.d(DownloaderAct.TAG, "${obj.fileName}任务开始...")
                    }

                    parseInt(XmDownState.CANCLE) -> {
                        val obj = getXmDownDaoBean(msg.obj)
                        downClient?.dao?.delete(obj.url) //从数据库中删除任务
                        v.onDownloadCancel(obj)
                        BKLog.d(DownloaderAct.TAG, "${obj.fileName}任务取消...")
                    }

                    parseInt(XmDownState.PAUSE) -> {
                        val obj = getXmDownDaoBean(msg.obj)
                        v.onDownloadPause(obj)
                        BKLog.d(DownloaderAct.TAG, "${obj.fileName}任务暂停...")
                    }

                    parseInt(XmDownState.RUNNING) -> {
                        //todo 频繁发送msg消息会导致UI卡顿，所以就直接刷新了,详情请看sendUpdateMessage(...)方法
                    }

                    parseInt(XmDownState.COMPLETE) -> {
                        val obj = getXmDownDaoBean(msg.obj)
                        downClient?.dao?.updateComplete(obj.url) //数据库中更新任务下载完成状态
                        v.onDownloadComplete(obj)
                        BKLog.d(DownloaderAct.TAG, "${obj.fileName}任务完成...")
                    }

                    parseInt(XmDownState.ERROR) -> {
                        val obj = getXmDownDaoBean(msg.obj)
                        downClient?.dao?.updateFailed(obj.url, obj.error)  //数据库中更新任务下载错误状态
                        v.onDownloadFailed(obj)
                        BKLog.d(DownloaderAct.TAG, "${obj.fileName}任务错误... error:${obj.error}")
                    }
                }
            }

            private fun getXmDownDaoBean(obj: Any): XmDownDaoBean {
                return obj as XmDownDaoBean
            }
        }

        init {
            downClient = XmDownClient.Builder()
                    .context(context)
                    .breakpoint(true)
                    .dir(Environment.getExternalStorageDirectory().absolutePath + File.separator + "XmDown")
                    .build()

            //从数据库中读取缓存记录
            val cache = downClient?.dao?.selectAll()!!
            downIndex = cache.size
            if (cache.isNotEmpty()) {
                for (ent in cache) {
                    enqueue(ent.url)
                }
            }
        }

        fun clickInsert() {
            if (downIndex < downUrls.size) {
                val url = downUrls[downIndex]
                enqueue(url)
            } else {
                v.showToast("下载任务已添加完成")
            }
            downIndex++
        }

        fun clickPauseAll() {
            downClient?.dao?.updateAllState(XmDownState.NOT_STARTED)
            downClient?.dao?.deleteAll()
            if (com.xm.lib.common.util.file.FileUtil.delAll(File(downClient?.dir))) {
                v.showToast("删除成功")
            }
        }

        fun clickEdit() {
        }

        fun clickJump() {
        }

        /**
         * 任务添加队列当中
         */
        private fun enqueue(url: String): Call {
            val request = XmDownRequest.Builder().url(url)
                    .fileName(CommonUtil.getFileName(url))
                    .build()

            val call = downClient?.newCall(request)

            calls.add(call!!)

            call.enqueue(object : XmDownInterface.Callback {

                override fun onDownloadStart(request: AbsRequest, path: String) {
                    sendUpdateMessage(getXmDownDaoBean(request, path), XmDownState.START)
                }

                override fun onDownloadCancel(request: AbsRequest) {
                    sendUpdateMessage(getXmDownDaoBean(request, ""), XmDownState.CANCLE)
                }

                override fun onDownloadPause(request: AbsRequest) {
                    sendUpdateMessage(getXmDownDaoBean(request, ""), XmDownState.PAUSE)
                }

                override fun onDownloadProgress(request: AbsRequest, progress: Long, total: Long) {
                    sendUpdateMessage(getXmDownDaoBean(request, progress, total), XmDownState.RUNNING)
                }

                override fun onDownloadComplete(request: AbsRequest) {
                    sendUpdateMessage(getXmDownDaoBean(request, ""), XmDownState.COMPLETE)
                }

                override fun onDownloadFailed(request: AbsRequest, error: XmDownError) {
                    sendUpdateMessage(getXmDownDaoBean(request, error), XmDownState.ERROR)
                }
            })
            return call
        }

        /**
         * 发送更新消息
         */
        private fun sendUpdateMessage(request: XmDownDaoBean, state: String, delayMillis: Long? = 0L) {
            if (state == XmDownState.RUNNING) {
                // todo 进度刷新
                val obj = request
                downClient?.dao?.updateProgress(obj.url, obj.progress, obj.total)
                v.onDownloadProgress(request)
                BKLog.d(DownloaderAct.TAG, "进度 : ${obj.progress} total : ${obj.total}")
            } else {
                val msg = rvUpdateHandler.obtainMessage()
                msg.what = parseInt(state)
                msg.obj = request
                rvUpdateHandler.sendMessage(msg/*, delayMillis!!*/)
            }
        }

        /**
         * String 转 Int
         */
        private fun parseInt(str: String): Int {
            var value = 0
            for (s in str) {
                value += s.toInt()
            }
            return value
        }

        /**
         * 将请求信息包装成数据库bean，这样便于数据的操作
         */
        private fun getXmDownDaoBean(request: AbsRequest, path: String): XmDownDaoBean {
            var xmDownDaoBean = XmDownDaoBean()
            //先读取缓存中的数据
            if (downClient?.dao?.select(request.url!!)?.size == 1) {
                xmDownDaoBean = downClient?.dao?.select(request.url!!)!![0]
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

        private fun getXmDownDaoBean(request: AbsRequest, progress: Long, total: Long): XmDownDaoBean {
            val xmDownDaoBean = getXmDownDaoBean(request, "")
            xmDownDaoBean.progress = progress
            xmDownDaoBean.total = total
            return xmDownDaoBean
        }

        private fun getXmDownDaoBean(request: AbsRequest, error: XmDownError): XmDownDaoBean {
            val xmDownDaoBean = getXmDownDaoBean(request, "")
            xmDownDaoBean.error = error
            return xmDownDaoBean
        }

        /**
         * 窗口退出暂停所有线程，这里通过一个flag来停止线程的，是在写入文件处停止的
         */
        fun onDestroy() {
            for (call in calls) {
                call.pause()
            }
        }
    }
}
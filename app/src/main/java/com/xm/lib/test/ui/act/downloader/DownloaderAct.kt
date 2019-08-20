package com.xm.lib.test.ui.act.downloader

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.xm.lib.common.base.rv.v2.BaseRvAdapterV2
import com.xm.lib.common.log.BKLog
import com.xm.lib.downloader.utils.CommonUtil
import com.xm.lib.downloader.v2.XmDownClient
import com.xm.lib.downloader.v2.XmDownRequest
import com.xm.lib.downloader.v2.db.XmDownDaoBean
import com.xm.lib.downloader.v2.imp.XmDownInterface
import com.xm.lib.downloader.v2.state.XmDownError
import com.xm.lib.downloader.v2.state.XmDownState
import com.xm.lib.test.R
import com.xm.lib.test.holder.DownVH2
import java.io.File


/**
 * 下载模块相关测试
 */
class DownloaderAct : AppCompatActivity() {

    companion object {
        const val TAG = "DownloaderAct"
    }

    private var rv: RecyclerView? = null
    private var btnInsert: Button? = null
    private var btnPauseAll: Button? = null
    private var btnEdit: Button? = null
    private var btnJump: Button? = null


    private var rvAdapter: BaseRvAdapterV2? = null
    private var dataResource = ArrayList<Any>()
    private var downClient: XmDownClient? = null
    private var downIndex = 0
    private val downUrls = arrayOf(
            "http://e.hiphotos.baidu.com/image/pic/item/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg",
            "https://apk.apk.xgdown.com/down/1hd.apk",
            "https://dl.hz.37.com.cn/upload/1_1002822_10664/shikongzhiyiH5_10664.apk",
            "https://cavedl.leiting.com/full/caveonline_M141859.apk"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_downloader)
        //startActivity(Intent(this, DownloaderActivity::class.java))
        findViews()
        initEvent()
        iniData()

    }

    private fun findViews() {
        rv = findViewById<View>(R.id.rv) as RecyclerView
        btnInsert = findViewById<View>(R.id.btn_insert) as Button
        btnPauseAll = findViewById<View>(R.id.btn_pause_all) as Button
        btnEdit = findViewById<View>(R.id.btn_edit) as Button
        btnJump = findViewById<View>(R.id.btn_jump) as Button
    }

    private fun initEvent() {
        btnInsert?.setOnClickListener {
            if (downIndex < downUrls.size) {
                val url = downUrls[downIndex]
                val call = downClient?.newCall(XmDownRequest.Builder()
                        .url(url)
                        .fileName(CommonUtil.getFileName(url))
                        .build())

                call?.enqueue(object : XmDownInterface.Callback {

                    override fun onDownloadStart(request: XmDownRequest, path: String) {
                        BKLog.d(TAG, "onDownloadStart")
                        downClient?.builder?.dao?.insert(XmDownDaoBean.newXmDownDaoBean(XmDownState.START, request))
                        for (i in rvAdapter?.getDataSource()!!) {
                            val d = data as XmDownDaoBean
                            if(d.url==request.url){
                                rvAdapter?.notifyItemChanged()
                                break
                            }
                        }

                    }

                    override fun onDownloadCancel(request: XmDownRequest) {
                        BKLog.d(TAG, "onDownloadCancel")
                        downClient?.builder?.dao?.delete(request.url!!)
                    }

                    override fun onDownloadProgress(request: XmDownRequest, progress: Long, total: Long) {
                        BKLog.d(TAG, "onDownloadProgress progress : $progress total : $total")
                        downClient?.builder?.dao?.updateProgress(request.url, progress)
                    }

                    override fun onDownloadComplete(request: XmDownRequest) {
                        BKLog.d(TAG, "onDownloadSuccess")
                        downClient?.builder?.dao?.updateComplete(request.url)
                    }

                    override fun onDownloadFailed(request: XmDownRequest, error: XmDownError) {
                        BKLog.d(TAG, "onDownloadFailed $error")
                        downClient?.builder?.dao?.updateFailed(request.url, error)
                    }

                })
            } else {
                Toast.makeText(this, "下载任务已添加完成", Toast.LENGTH_SHORT).show()
            }
            downIndex++
        }

        btnPauseAll?.setOnClickListener {

        }

        btnEdit?.setOnClickListener {

        }

        btnJump?.setOnClickListener {

        }
    }

    private fun iniData() {
        downIndex = 0
        downClient = XmDownClient.Builder()
                .context(this)
                .breakpoint(true)
                .dir(Environment.getExternalStorageDirectory().absolutePath + File.separator + "XmDown")
                .runMaxQueuesNum(1)
                .build()
        //从数据库中读取缓存记录
        dataResource.addAll(downClient?.dao?.selectAll()!!)

        rvAdapter = BaseRvAdapterV2.Builder()
                .addDataResouce(dataResource)
                .addHolderFactory(DownVH2.Factory())
                .build()
        rv?.adapter = rvAdapter
        rv?.layoutManager = LinearLayoutManager(this)
        rv?.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    override fun onDestroy() {
        super.onDestroy()
        downClient?.dao?.updateAllState(XmDownState.NOT_STARTED)
    }
}

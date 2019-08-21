package com.xm.lib.test.ui.act.downloader

import android.os.*
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
import com.xm.lib.downloader.utils.FileUtil
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
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2867164189,4027291360&fm=11&gp=0.jpg",
            "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1778670421,3855406236&fm=26&gp=0.jpg",
            "https://cavedl.leiting.com/full/caveonline_M141859.apk",
            "https://apk.apk.xgdown.com/down/1hd.apk",
            "https://dl.hz.37.com.cn/upload/1_1002822_10664/shikongzhiyiH5_10664.apk"
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

    private fun parseInt(str: String): Int {
        var value = 0
        for (s in str) {
            value += s.toInt()
        }
        return value
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
                        //插入缓存数据库，注意：任务不存在才插入
                        val xmDownDaoBean = XmDownDaoBean()
                        xmDownDaoBean.progress = 0
                        xmDownDaoBean.url = request.url!!
                        xmDownDaoBean.fileName = request.fileName!!
                        xmDownDaoBean.path = path
                        xmDownDaoBean.state = XmDownState.START
                        xmDownDaoBean.isEdit = false
                        xmDownDaoBean.isSelect = false
                        downClient?.dao?.insert(xmDownDaoBean)

                        //更新任务状态UI界面
                        val data = rvAdapter?.getDataSource()
                        data?.add(xmDownDaoBean)
                        rv?.post {
                            for (i in 0 until data?.size!!) {
                                val xmDownDaoBean = data[i] as XmDownDaoBean
                                if (xmDownDaoBean.url == request.url) {
                                    xmDownDaoBean.state = XmDownState.START
                                    rvAdapter?.notifyItemChanged(i)
                                    break
                                }
                            }
                        }
                    }

                    override fun onDownloadCancel(request: XmDownRequest) {
                        BKLog.d(TAG, "onDownloadCancel")
                        //从数据库中删除任务
                        downClient?.dao?.delete(request.url!!)

                        //更新任务状态UI界面
                        rv?.post {
                            val data = rvAdapter?.getDataSource()
                            for (i in 0 until data?.size!!) {
                                val xmDownDaoBean = data[i] as XmDownDaoBean
                                if (xmDownDaoBean.url == request.url) {
                                    xmDownDaoBean.state = XmDownState.CANCLE
                                    rvAdapter?.notifyItemChanged(i)
                                    break
                                }
                            }
                        }
                    }

                    override fun onDownloadProgress(request: XmDownRequest, progress: Long, total: Long) {
                        BKLog.d(TAG, "onDownloadProgress progress : $progress total : $total")
                        //从数据库中更新任务下载进度
                        downClient?.dao?.updateProgress(request.url, progress, total)
                        rv?.post {
                            //更新任务状态UI界面
                            val data = rvAdapter?.getDataSource()
                            for (i in 0 until data?.size!!) {
                                val xmDownDaoBean = data[i] as XmDownDaoBean
                                if (xmDownDaoBean.url == request.url) {
                                    xmDownDaoBean.progress = progress
                                    xmDownDaoBean.total = total
                                    rvAdapter?.notifyItemChanged(i)
                                    break
                                }
                            }
                        }
                    }

                    override fun onDownloadComplete(request: XmDownRequest) {
                        BKLog.d(TAG, "onDownloadSuccess")
                        //数据库中更新任务下载完成状态
                        downClient?.dao?.updateComplete(request.url)

                        //刷新UI界面
                        rv?.post {
                            val data = rvAdapter?.getDataSource()
                            for (i in 0 until data?.size!!) {
                                val xmDownDaoBean = data[i] as XmDownDaoBean
                                if (xmDownDaoBean.url == request.url) {
                                    xmDownDaoBean.state = XmDownState.COMPLETE
                                    xmDownDaoBean.progress = xmDownDaoBean.total
                                    rvAdapter?.notifyItemChanged(i)
                                    break
                                }
                            }
                        }
                    }

                    override fun onDownloadFailed(request: XmDownRequest, error: XmDownError) {
                        BKLog.d(TAG, "onDownloadFailed $error")
                        //数据库中更新任务下载错误状态
                        downClient?.dao?.updateFailed(request.url, error)

                        //刷新UI界面
                        rv?.post {
                            val data = rvAdapter?.getDataSource()
                            for (i in 0 until data?.size!!) {
                                val xmDownDaoBean = data[i] as XmDownDaoBean
                                if (xmDownDaoBean.url == request.url) {
                                    xmDownDaoBean.state = XmDownState.getError(error)
                                    rvAdapter?.notifyItemChanged(i)
                                    break
                                }
                            }
                        }
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
            downClient?.dao?.updateAllState(XmDownState.NOT_STARTED)
            downClient?.dao?.deleteAll()
            if (com.xm.lib.common.util.file.FileUtil.delAll(File(downClient?.dir))) {
                Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun iniData() {
        downClient = XmDownClient.Builder()
                .context(this)
                .breakpoint(true)
                .dir(Environment.getExternalStorageDirectory().absolutePath + File.separator + "XmDown")
                .runMaxQueuesNum(1)
                .build()

        //从数据库中读取缓存记录
        val cache = downClient?.dao?.selectAll()!!
        downIndex = cache.size + 1
        dataResource.addAll(cache)

        //初始化RecyclerView
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

    }
}

package com.xm.lib.test.ui.act.downloader

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.xm.lib.common.base.mvp.MvpActivity
import com.xm.lib.common.base.rv.v2.BaseRvAdapterV2
import com.xm.lib.downloader.v2.db.XmDownDaoBean
import com.xm.lib.downloader.v2.state.XmDownState
import com.xm.lib.test.R
import com.xm.lib.test.contract.DownloaderActContract
import com.xm.lib.test.holder.DownVH2

/**
 * 下载模块相关测试
 */

class DownloaderAct : MvpActivity<DownloaderActContract.P>(), DownloaderActContract.V {

    companion object {
        const val TAG = DownloaderActContract.TAG
    }

    private var rv: RecyclerView? = null
    private var btnInsert: Button? = null
    private var btnPauseAll: Button? = null
    private var btnResumeAll: Button? = null
    private var btnEdit: Button? = null
    private var btnDeleteAll: Button? = null
    private var btnDelete: Button? = null

    private var rvAdapter: BaseRvAdapterV2? = null

    override fun presenter(): DownloaderActContract.P {
        return DownloaderActContract.P(this, this)
    }

    override fun setContentViewBefore() {}

    override fun getLayoutId(): Int {
        return R.layout.activity_downloader
    }

    override fun initDisplay() {

    }

    override fun findViews() {
        rv = findViewById<View>(R.id.rv) as RecyclerView
        btnInsert = findViewById<View>(R.id.btn_insert) as Button
        btnPauseAll = findViewById<View>(R.id.btn_pause_all) as Button
        btnResumeAll = findViewById<View>(R.id.btn_resume_all) as Button
        btnEdit = findViewById<View>(R.id.btn_edit) as Button
        btnDeleteAll = findViewById<View>(R.id.btn_delete_all) as Button
        btnDelete = findViewById<View>(R.id.btn_delete) as Button
    }

    override fun iniData() {
        rvAdapter = BaseRvAdapterV2.Builder()
                .addDataResouce(ArrayList<Any>())
                .addHolderFactory(DownVH2.Factory(p?.getDownClient()))
                .build()
        rv?.adapter = rvAdapter
        rv?.layoutManager = LinearLayoutManager(this)
        rv?.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    override fun iniEvent() {
        btnInsert?.setOnClickListener {
            p?.clickInsert()
        }

        btnPauseAll?.setOnClickListener {
            p?.clickPauseAll()
        }

        btnResumeAll?.setOnClickListener {
            p?.clickResumeAll()
        }

        btnEdit?.setOnClickListener {
            p?.clickEdit()
        }

        btnDelete?.setOnClickListener {
            p?.clickDelete()
        }

        btnDeleteAll?.setOnClickListener {
            p?.clickDeleteAll()
        }
    }

    override fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    override fun onDownloadStart(obj: XmDownDaoBean) {
        val data = rvAdapter?.getDataSource()
        data?.add(obj)
        for (i in 0 until data?.size!!) {
            val xmDownDaoBean = data[i] as XmDownDaoBean
            if (xmDownDaoBean.url == obj.url) {
                xmDownDaoBean.state = XmDownState.START
                rvAdapter?.notifyItemChanged(i)
                break
            }
        }
    }

    override fun onDownloadCancel(obj: XmDownDaoBean) {
        val data = rvAdapter?.getDataSource()
        for (i in 0 until data?.size!!) {
            val xmDownDaoBean = data[i] as XmDownDaoBean
            if (xmDownDaoBean.url == obj.url) {
                xmDownDaoBean.state = XmDownState.CANCLE
                rvAdapter?.notifyItemChanged(i)
                break
            }
        }
    }

    override fun onDownloadPause(obj: XmDownDaoBean) {
        //更新任务状态UI界面
        val data = rvAdapter?.getDataSource()
        for (i in 0 until data?.size!!) {
            val xmDownDaoBean = data[i] as XmDownDaoBean
            if (xmDownDaoBean.url == obj.url) {
                xmDownDaoBean.state = XmDownState.PAUSE
                rvAdapter?.notifyItemChanged(i)
                break
            }
        }
    }

    override fun onDownloadProgress(obj: XmDownDaoBean) {
        val progress = obj.progress
        val total = obj.total
        val data = rvAdapter?.getDataSource()
        for (i in 0 until data?.size!!) {
            val xmDownDaoBean = data[i] as XmDownDaoBean
            if (xmDownDaoBean.url == obj.url) {
                if (obj.state == XmDownState.COMPLETE) {
                    xmDownDaoBean.progress = total
                    xmDownDaoBean.total = total
                } else {
                    xmDownDaoBean.progress = progress
                    xmDownDaoBean.total = total
                }
                xmDownDaoBean.state = XmDownState.RUNNING
                this@DownloaderAct.runOnUiThread {
                    rvAdapter?.notifyItemChanged(i)
                }
                break
            }
        }
    }

    override fun onDownloadComplete(obj: XmDownDaoBean) {
        val data = rvAdapter?.getDataSource()
        for (i in 0 until data?.size!!) {
            val xmDownDaoBean = data[i] as XmDownDaoBean
            if (xmDownDaoBean.url == obj.url) {
                xmDownDaoBean.state = XmDownState.COMPLETE
                xmDownDaoBean.progress = xmDownDaoBean.total
                rvAdapter?.notifyItemChanged(i)
                break
            }
        }
    }

    override fun onDownloadFailed(obj: XmDownDaoBean) {
        val data = rvAdapter?.getDataSource()
        for (i in 0 until data?.size!!) {
            val xmDownDaoBean = data[i] as XmDownDaoBean
            if (xmDownDaoBean.url == obj.url) {
                xmDownDaoBean.state = XmDownState.getError(xmDownDaoBean.error)
                rvAdapter?.notifyItemChanged(i)
                break
            }
        }
    }

    override fun edit(editMode: Boolean) {
        val dataSource = rvAdapter?.getDataSource()
        if (dataSource?.isEmpty()!!) {
            return
        }
        for (data in dataSource) {
            val d = data as XmDownDaoBean

            if (!editMode) {
                d.isSelect = false
            }

            d.isEdit = editMode
        }

        rvAdapter?.notifyDataSetChanged()
    }

    override fun deleteSelectItem() {
        val dataSource = rvAdapter?.getDataSource()
        if (dataSource?.isEmpty()!!) {
            return
        }
        /**
         * 迭代器处理 ConcurrentModificationException 错误
         */
        val it = dataSource.iterator()
        while (it.hasNext()) {
            val d = it.next() as XmDownDaoBean
            if (d.isSelect) {
                p?.deleteDao(d.url)
                dataSource.remove(d)
            }
        }
        rvAdapter?.notifyDataSetChanged()
    }

    override fun notification() {
        rvAdapter?.getDataSource()?.clear()
        rvAdapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        p?.onDestroy()
    }
}

package com.xm.lib.test.holder

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.xm.lib.common.base.rv.v2.BaseRvAdapterV2
import com.xm.lib.common.base.rv.v2.BaseViewHolderV2
import com.xm.lib.common.log.BKLog
import com.xm.lib.downloader.v2.XmDownClient
import com.xm.lib.downloader.v2.XmDownRequest
import com.xm.lib.downloader.v2.XmRealCall
import com.xm.lib.downloader.v2.abs.AbsRequest
import com.xm.lib.downloader.v2.db.XmDownDaoBean
import com.xm.lib.downloader.v2.imp.XmDownInterface
import com.xm.lib.downloader.v2.state.XmDownError
import com.xm.lib.downloader.v2.state.XmDownState
import com.xm.lib.test.R

class DownVH2(private val downClient: XmDownClient?, itemView: View) : BaseViewHolderV2(itemView) {

    private var ui: UI? = null
    private lateinit var ent: XmDownDaoBean
    private lateinit var adapter: BaseRvAdapterV2

    private var handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            adapter.notifyItemChanged(msg?.what!!)
        }
    }

    override fun onBind(adapter: BaseRvAdapterV2, data: Any, position: Int) {
        super.onBind(adapter, data, position)
        if (ui == null) {
            ui = UI.create(itemView)
            this.adapter = adapter
        }
        if (data is XmDownDaoBean) {
            ent = data as XmDownDaoBean
            //是否为编辑状态
            if (ent.isEdit) {
                ui?.ivSelect?.visibility = View.VISIBLE
                ui?.ivSelect?.isChecked = ent.isSelect
                ui?.ivSelect?.setOnCheckedChangeListener { buttonView, isChecked ->
                    //修改指定索引的选中标志
                    val xmDownDaoBean = adapter.getDataSource()[pos] as XmDownDaoBean
                    xmDownDaoBean.isSelect = isChecked
                }
            } else {
                ui?.ivSelect?.visibility = View.INVISIBLE
            }

            //下载任务封面
            ui?.ivIcon?.setBackgroundResource(R.mipmap.ic_launcher)

            //下载任务名称
            ui?.tvName?.text = ent.fileName

            //下载的状态
            ui?.tvState?.text = ent.state

            //下载进度 & 下載总大小
            if (ent.state == XmDownState.COMPLETE) {
                ui?.progressBar?.max = ent.total.toInt()
                ui?.progressBar?.progress = ent.total.toInt()
                ui?.tvProgress?.text = ent.total.toString()
                ui?.tvTotal?.text = ent.total.toString()
            } else {
                ui?.progressBar?.max = ent.total.toInt()
                ui?.progressBar?.progress = ent.progress.toInt()
                ui?.tvProgress?.text = ent.progress.toString()
                ui?.tvTotal?.text = ent.total.toString()
            }


        } else {
            BKLog.e("data not XmDownDaoBean")
        }
    }

    override fun onBind(data: Any) {
        //ctx = itemView.context
    }

    override fun onClick(v: View?) {
        val data = adapter.getDataSource()
        val xmDownDaoBean = data[pos] as XmDownDaoBean
        val pos = pos

        if (data.size > pos) {
            when (xmDownDaoBean.state) {
                XmDownState.RUNNING -> {
                    //队列中不可点击
                    for (call in downClient?.calls!!) {
                        if (ent.url == call.request().url) {
                            if (call.isExecuted() && (!call.isCanceled() && !call.isPaused())) {
                                call.pause()
                                xmDownDaoBean.state = XmDownState.PAUSE
                                handler.sendEmptyMessage(pos)
                                return
                            }
                        }
                    }
                }

                XmDownState.PAUSE, XmDownState.ERROR -> {
                    //恢复下载
                    downClient?.newCall(XmDownRequest.Builder()
                            .fileName(ent.fileName)
                            .url(ent.url)
                            .build())?.enqueue(object : XmDownInterface.Callback {
                        override fun onDownloadStart(request: AbsRequest, path: String) {
                            xmDownDaoBean.state = XmDownState.START
                            handler.sendEmptyMessage(pos)
                        }

                        override fun onDownloadCancel(request: AbsRequest) {
                            xmDownDaoBean.state = XmDownState.CANCLE
                            handler.sendEmptyMessage(pos)
                        }

                        override fun onDownloadPause(request: AbsRequest) {
                            xmDownDaoBean.state = XmDownState.PAUSE
                            handler.sendEmptyMessage(pos)
                        }

                        override fun onDownloadProgress(request: AbsRequest, progress: Long, total: Long) {
                            xmDownDaoBean.progress = progress
                            xmDownDaoBean.total = total
                            xmDownDaoBean.state = XmDownState.RUNNING
                            handler.sendEmptyMessage(pos)
                        }

                        override fun onDownloadComplete(request: AbsRequest) {
                            xmDownDaoBean.state = XmDownState.COMPLETE
                            xmDownDaoBean.progress = xmDownDaoBean.total
                            handler.sendEmptyMessage(pos)
                        }

                        override fun onDownloadFailed(request: AbsRequest, error: XmDownError) {
                            xmDownDaoBean.state = XmDownState.getError(error)
                            handler.sendEmptyMessage(pos)
                        }
                    })
                }
            }
        }
    }

    class Factory(private val downClient: XmDownClient?) : BaseViewHolderV2.Factory() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderV2 {
            return DownVH2(downClient, getView(parent.context, parent, R.layout.item_down_2))
        }

        override fun getItemViewType(): Pair<Class<*>, String> {
            return Pair(Any::class.java, "DownVH2")
        }
    }

    private class UI private constructor(val ivSelect: CheckBox, val ivIcon: ImageView, val progressBar: ProgressBar, val tvName: TextView, val tvState: TextView, val tvProgress: TextView, val tvTotal: TextView) {
        companion object {

            fun create(rootView: View): UI {
                val ivSelect = rootView.findViewById<View>(R.id.iv_select) as CheckBox
                val ivIcon = rootView.findViewById<View>(R.id.iv_icon) as ImageView
                val progressBar = rootView.findViewById<View>(R.id.progressBar) as ProgressBar
                val tvName = rootView.findViewById<View>(R.id.tv_name) as TextView
                val tvState = rootView.findViewById<View>(R.id.tv_state) as TextView
                val tvProgress = rootView.findViewById<View>(R.id.tv_progress) as TextView
                val tvTotal = rootView.findViewById<View>(R.id.tv_total) as TextView
                return UI(ivSelect, ivIcon, progressBar, tvName, tvState, tvProgress, tvTotal)
            }
        }
    }




}
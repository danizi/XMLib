package com.xm.lib.test.holder

import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.xm.lib.common.base.rv.v2.BaseViewHolderV2
import com.xm.lib.common.log.BKLog
import com.xm.lib.downloader.v2.db.XmDownDaoBean
import com.xm.lib.test.R

class DownVH2(itemView: View) : BaseViewHolderV2(itemView) {

    private var ui: UI? = null

    override fun onBind(data: Any) {
        ctx = itemView.context
        if (ui == null) {
            ui = UI.create(itemView)
        }
        if (data is XmDownDaoBean) {
            val ent = data as XmDownDaoBean
            //是否为编辑状态
            if (ent.isEdit) {
                ui?.ivSelect?.visibility = View.VISIBLE
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
            ui?.progressBar?.max = ent.total.toInt()
            ui?.progressBar?.progress = ent.progress.toInt()
            ui?.tvProgress?.text = ent.progress.toString()
            ui?.tvTotal?.text = ent.total.toString()

        } else {
            BKLog.e("data not XmDownDaoBean")
        }
    }

    override fun onClick(v: View?) {

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

    class Factory : BaseViewHolderV2.Factory() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderV2 {
            return DownVH2(getView(parent.context, parent, R.layout.item_down_2))
        }

        override fun getItemViewType(): Pair<Class<*>, String> {
            return Pair(Any::class.java, "DownVH2")
        }
    }
}
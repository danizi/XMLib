package com.xm.lib.test.holder

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.xm.lib.common.base.rv.v2.BaseViewHolderV2
import com.xm.lib.common.log.BKLog
import com.xm.lib.test.R
import com.xm.lib.test.utils.IntoTarget

class ItemVH(itemView: View) : BaseViewHolderV2(itemView) {

    private var tv: TextView? = null

    override fun onBind(data: Any) {}

    override fun onBind(data: Any, position: Int) {
        super.onBind(data, position)

        if (tv == null) {
            tv = itemView.findViewById<TextView>(R.id.tv)
        }

        if (data is String) {
            tv?.text = data.toString()
        }
    }

    override fun onClick(v: View?) {
        BKLog.d("触发点击事件 $pos")
        IntoTarget.start(data.toString(), ctx as Activity?)
    }


    internal  class Factory : BaseViewHolderV2.Factory() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderV2 {
            return ItemVH(getView(parent.context,parent, R.layout.item_tv))
        }

        override fun getItemViewType(): Pair<Class<*>, String> {
            return Pair(Any::class.java, "itemType")
        }
    }
}

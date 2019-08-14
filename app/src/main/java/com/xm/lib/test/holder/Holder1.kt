package com.xm.lib.test.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xm.lib.common.base.rv.v2.BaseViewHolderV2
import com.xm.lib.test.R
import com.xm.lib.test.bean.TypeBean1

class Holder1(itemView: View) : BaseViewHolderV2(itemView) {
    override fun onBind(data: Any) {

    }


    override fun onClick(v: View) {

    }

    internal class Factory1 : BaseViewHolderV2.Factory() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderV2 {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_holder_1, null)
            return Holder1(getView(parent.context,parent,R.layout.item_holder_1))
        }

        override fun getItemViewType(): Pair<Class<*>, String> {
            return Pair(TypeBean1::class.java, "HolderType1")
        }
    }
}
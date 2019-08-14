package com.xm.lib.test.holder

import android.view.View
import android.view.ViewGroup
import com.xm.lib.common.base.rv.v2.BaseViewHolderV2
import com.xm.lib.test.R
import com.xm.lib.test.bean.TypeBean3

class Holder3(itemView: View) : BaseViewHolderV2(itemView) {

    override fun onBind(data: Any) {}

    override fun onClick(v: View) {

    }

    internal class Factory3 : BaseViewHolderV2.Factory() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderV2 {
            return Holder3(getView(parent.context, parent, R.layout.item_holder_3))
        }

        override fun getItemViewType(): Pair<Class<*>, String> {
            return Pair(TypeBean3::class.java, "HolderType3")
        }
    }
}
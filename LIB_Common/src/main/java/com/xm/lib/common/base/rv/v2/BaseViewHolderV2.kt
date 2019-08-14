package com.xm.lib.common.base.rv.v2

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseViewHolderV2(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    protected var ctx: Context? = null
    protected var data: Any? = null
    protected var pos = 0

    abstract fun onBind(data: Any)

    open fun onBind(data: Any, position: Int) {
        this.ctx = itemView.context
        this.data = data
        this.pos = position
        itemView.setOnClickListener(this)
    }

    open fun onBind(adapter: BaseRvAdapterV2, data: Any, position: Int) {
        this.ctx = itemView.context
        this.data = data
        this.pos = position
        itemView.setOnClickListener(this)
    }

    abstract class Factory {

        protected fun getView(ctx: Context,parent:ViewGroup, layoutId: Int): View {
            return LayoutInflater.from(ctx).inflate(layoutId,  parent, false)
        }

        abstract fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderV2

        abstract fun getItemViewType(): Pair<Class<*>, String>

    }
}
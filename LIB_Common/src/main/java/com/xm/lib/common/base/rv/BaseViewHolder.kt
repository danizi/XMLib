package com.xm.lib.common.base.rv

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View


abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val views: SparseArray<View>? = null
    private var listener: OnBindDataListener? = null
    abstract fun bindData(d: Any, position: Int)

    fun <T : View> getView(viewId: Int): T? {
        /*获取view*/
        var view = views?.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            views?.put(viewId, view)
        }
        return view as T
    }
}

interface OnBindDataListener {
    fun onBindData(v: View?, d: Any, position: Int)
}


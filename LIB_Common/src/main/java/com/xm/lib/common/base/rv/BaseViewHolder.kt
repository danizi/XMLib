package com.xm.lib.common.base.rv

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View


abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val views: SparseArray<View>? = null
    @Deprecated("不要使用")
    private var listener: OnBindDataListener? = null

    /**
     * 绑定数据
     * @param d 绑定的数据
     * @param position 绑定的数据下标位置
     */
    abstract fun bindData(d: Any, position: Int)

    /**
     * 当使用嵌套的时候可以使用
     * @param adapter 适配器
     * @param d 绑定的数据
     * @param position 绑定的数据下标位置
     */
    open fun bindData(adapter: BaseRvAdapter, d: Any, position: Int) {}

    /**
     * 查找控件id
     */
    @Deprecated("使用了https://www.buzzingandroid.com/tools/android-layout-finder/来替代了")
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
    @Deprecated("不要使用")
    fun onBindData(v: View?, d: Any, position: Int)
}


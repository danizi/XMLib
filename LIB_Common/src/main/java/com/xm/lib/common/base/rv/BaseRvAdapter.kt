package com.xm.lib.common.base.rv

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * RecyclerView 适配器
 */
abstract class BaseRvAdapter : RecyclerView.Adapter<BaseViewHolder>() {
    var data: List<*>? = null
    private var delegateManager: DelegateManager? = null
    private var onBindDataListener: OnBindDataListener? = null

    init {
        delegateManager = DelegateManager()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val delegateBean: DelegateBean = delegateManager?.targets?.get(viewType)!!
        val v = LayoutInflater.from(parent.context).inflate(delegateBean.itemViewLayoutId!!, parent, false)
        val viewHolder: BaseViewHolder? = delegateBean.viewHolderClass.getConstructor(View::class.java).newInstance(v) as BaseViewHolder?
        return viewHolder!!
    }

    override fun getItemCount(): Int {
        return if (data?.isEmpty()!!) 0 else data?.size!!
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        bindVH(holder, position)
    }

    private fun bindVH(holder: BaseViewHolder, position: Int) {
        /*绑定数据*/
        if (data?.get(position) != null) {
            holder.bindData(data!![position]!!, position)
            onBindDataListener?.onBindData(holder.itemView, data!![position]!!, position)
        }
    }

    fun addItemViewDelegate(viewType: Int, viewHolderClass: Class<*>, itemViewLayoutId: Int?) {
        /*委派者添加需要被委派任务的viewHolder*/
        delegateManager?.targets?.put(viewType, DelegateBean(viewHolderClass, itemViewLayoutId))
    }

    fun setOnBindDataListener(listener: OnBindDataListener) {
        /*itemView绑定监听,处理ViewHolder,对itemView进行绑定操作*/
        onBindDataListener = listener
    }

    /**
     * 委派者
     */
    class DelegateManager {
        val targets = HashMap<Int, DelegateBean>()
    }

    /**
     * 委派者实体
     */
    class DelegateBean(val viewHolderClass: Class<*>, val itemViewLayoutId: Int?)
}


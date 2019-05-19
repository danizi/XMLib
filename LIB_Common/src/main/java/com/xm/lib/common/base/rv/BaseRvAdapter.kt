package com.xm.lib.common.base.rv

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * RecyclerView 适配器
 */
abstract class BaseRvAdapter : RecyclerView.Adapter<BaseViewHolder>() {
    /**
     * 数据集合
     */
    var data: ArrayList<Any>? = ArrayList()
    /**
     * 不同类型ViewHolder管理
     */
    private var delegateManager: DelegateManager? = null
    /**
     * 过时了
     */
    @Deprecated("不要在使用")
    private var onBindDataListener: OnBindDataListener? = null

    init {
        delegateManager = DelegateManager()
    }

    /**
     * 创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        //根据回传的viewType获取指定ViewHolder信息
        val delegateBean: DelegateBean = delegateManager?.targets?.get(viewType)!!  //ps:需要处理一下这里课程会出问题
        //获取ViewHolder View实例
        val v = LayoutInflater.from(parent.context).inflate(delegateBean.itemViewLayoutId!!, parent, false)
        //创建ViewHolder实例
        val viewHolder: BaseViewHolder? = delegateBean.viewHolderClass.getConstructor(View::class.java).newInstance(v) as BaseViewHolder?
        //保存ViewHolder实例
        delegateManager?.typeViewHolders?.add(viewHolder!!)
        return viewHolder!!
    }

    /**
     * item界面返回的类型
     */
    override fun getItemViewType(position: Int): Int {
        for (d in delegateManager?.targets?.entries!!) {
            if (d.value.beanClass == data!![position].javaClass) {
                return d.key
            }
        }
        return super.getItemViewType(position)
    }

    /**
     * 数据数量
     */
    override fun getItemCount(): Int {
        return if (data?.isEmpty()!!) 0 else data?.size!!
    }

    /**
     * 绑定（界面-数据）
     */
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        bindVH(holder, position)
    }

    private fun bindVH(holder: BaseViewHolder, position: Int) {
        /*绑定数据*/
        if (data?.get(position) != null) {
            holder.bindData(data!![position]!!, position)
            holder.bindData(this, data!![position], position)
            onBindDataListener?.onBindData(holder.itemView, data!![position]!!, position)
        }
    }

    /**
     * 委派者添加需要被委派任务的ViewHolder
     */
    fun addItemViewDelegate(viewType: Int, viewHolderClass: Class<*>, BeanClass: Class<*>, itemViewLayoutId: Int?) {
        delegateManager?.targets?.put(viewType, DelegateBean(viewHolderClass, BeanClass, itemViewLayoutId))
    }

    /**
     * 获取ViewHolder实例
     */
    fun getTypeViewHolder(type: Int): BaseViewHolder {
        return delegateManager?.typeViewHolders?.get(type)!!
    }

    /**
     * item界面委派者
     */
    private class DelegateManager {
        val targets = HashMap<Int, DelegateBean>()
        val typeViewHolders = ArrayList<BaseViewHolder>()
    }

    /**
     * item界面委派者实体
     */
    private class DelegateBean(val viewHolderClass: Class<*>, val beanClass: Class<*>, val itemViewLayoutId: Int?)
}


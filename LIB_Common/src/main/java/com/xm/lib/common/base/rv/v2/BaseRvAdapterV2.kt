package com.xm.lib.common.base.rv.v2

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.xm.lib.common.log.BKLog

class BaseRvAdapterV2 private constructor(val builder: Builder?) : RecyclerView.Adapter<BaseViewHolderV2>() {

    companion object {
        private const val TAG = "BaseRvAdapterV2"
    }

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): BaseViewHolderV2 {
        val holderFactory = builder?.holderFactory!![viewType]
                ?: throw IllegalAccessException("holderFactory is null")
        val holder = holderFactory.onCreateViewHolder(p0, viewType)
        BKLog.d(TAG, "onCreateViewHolder: $holder viewType:$viewType")
        return holder
    }

    override fun getItemCount(): Int {
        return if (builder?.dataSource?.isEmpty()!!) {
            BKLog.e(TAG, "dataSource is null getItemCount 0")
            0
        } else {
            BKLog.d(TAG, "getItemCount: ${builder.dataSource.size}")
            builder.dataSource.size
        }
    }

    override fun onBindViewHolder(p0: BaseViewHolderV2, position: Int) {
        if (builder?.dataSource?.size!! > position) {
            p0.onBind(builder.dataSource[position])
            p0.onBind(builder.dataSource[position], position)
            p0.onBind(this, builder.dataSource[position], position)
        } else {
            BKLog.e("dataSource size < position")
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (!builder?.holderFactory.isNullOrEmpty()) {
            val beanCls = builder?.dataSource!![position].javaClass
            for (ent in builder.holderFactory.entries) {
                val factory = ent.value
                val contrastCls = factory.getItemViewType().first
                if (beanCls == contrastCls) {
                    BKLog.d(TAG, "getItemViewType: ${ent.key} - ${factory.getItemViewType().second}")
                    return ent.key // Holder Type 字符串转化的Int值
                }
            }

        }
        //BKLog.e(TAG, "super.getItemViewType(position)")
        return super.getItemViewType(position)
    }

    fun getDataSource(): ArrayList<Any> {
        if (builder?.dataSource == null) {
            throw NullPointerException("dataSource is null")
        }
        return builder.dataSource
    }


    class Builder {
        val holderFactory: HashMap<Int, BaseViewHolderV2.Factory> = HashMap()
        val dataSource: ArrayList<Any> = ArrayList()

        fun addDataResouce(data: ArrayList<Any>): Builder {
            dataSource.clear()
            dataSource.addAll(data)
            return this
        }

        fun addHolderFactory(factory: BaseViewHolderV2.Factory?): Builder {
            if (factory == null) {
                throw NullPointerException("holderFactory is null")
            }

            if (factory.getItemViewType().first == Any::class.java) {
                // 设置系统默认viewType ，0等价于super.getItemViewType(position)
                holderFactory[0] = factory
            } else {
                holderFactory[parseInt(factory.getItemViewType().second)] = factory
            }

            return this
        }

        private fun parseInt(str: String): Int {
            var value = 0
            for (s in str) {
                value += s.toInt()
            }
            BKLog.d(TAG, "viewType : $str viewTypeInt : $value")
            return value
        }

        fun build(): BaseRvAdapterV2 {
            return BaseRvAdapterV2(this)
        }
    }
}
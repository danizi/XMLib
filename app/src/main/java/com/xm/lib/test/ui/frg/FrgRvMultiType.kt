package com.xm.lib.test.ui.frg

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.xm.lib.common.base.mvp.MvpFragment
import com.xm.lib.common.base.rv.v2.BaseRvAdapterV2
import com.xm.lib.test.R
import com.xm.lib.test.adapter.FrgRvMultiTypeContract
import com.xm.lib.test.bean.TypeBean1
import com.xm.lib.test.bean.TypeBean2
import com.xm.lib.test.bean.TypeBean3
import com.xm.lib.test.holder.Holder1
import com.xm.lib.test.holder.Holder2
import com.xm.lib.test.holder.Holder3
import java.util.ArrayList

/**
 * RecyclerView 多类型
 */
class FrgRvMultiType : MvpFragment<FrgRvMultiTypeContract.P>(), FrgRvMultiTypeContract.V {

    private var rv: RecyclerView? = null

    override fun presenter(): FrgRvMultiTypeContract.P {
        return FrgRvMultiTypeContract.P(context, this)
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_rv_multil_type
    }

    override fun findViews(view: View) {
        rv = view.findViewById(R.id.rv)
    }

    override fun initDisplay() {
        rv?.layoutManager = LinearLayoutManager(context)
        rv?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    override fun iniEvent() {

    }

    override fun iniData() {
        rv?.adapter = getRvAdapter()
    }

    private fun getRvAdapter(): BaseRvAdapterV2 {
        return BaseRvAdapterV2.Builder
                .addDataResouce(getData())
                .addHolderFactory(Holder1.Factory1())
                .addHolderFactory(Holder2.Factory2())
                .addHolderFactory(Holder3.Factory3())
                .build()
    }

    private fun getData(): ArrayList<Any> {
        val data = ArrayList<Any>()
        data.add(TypeBean1())
        data.add(TypeBean2())
        data.add(TypeBean2())
        data.add(TypeBean2())
        data.add(TypeBean3())
        return data
    }
}
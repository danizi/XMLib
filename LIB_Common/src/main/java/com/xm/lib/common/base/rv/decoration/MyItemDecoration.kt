package com.xm.lib.common.base.rv.decoration

import android.content.Context
import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View


class MyItemDecoration(context: Context?, orientation: Int) : DividerItemDecoration(context, orientation) {
    /**
     *
     * @param outRect 边界
     * @param view recyclerView ItemView
     * @param parent recyclerView
     * @param state recycler 内部数据管理
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        //设定底部边距为1px
        //outRect.set(0, 0, 0, 0)
    }

    companion object {
        fun divider(context: Context?, orientation: Int, id: Int): RecyclerView.ItemDecoration {
            val d = MyItemDecoration(context, orientation)
            d.setDrawable(ContextCompat.getDrawable(context!!, id)!!)
            return d
        }
    }
}
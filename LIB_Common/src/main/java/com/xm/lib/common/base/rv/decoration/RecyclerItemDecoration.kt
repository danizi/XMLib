package com.xm.lib.common.base.rv.decoration

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 *
 * @param itemSpace item间隔
 * @param itemNum 每行item的个数
 */
class RecyclerItemDecoration(private val itemSpace: Int, private val itemNum: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = itemSpace
        if (parent.getChildLayoutPosition(view) % itemNum == 0) {  //parent.getChildLayoutPosition(view) 获取view的下标
            outRect.left = 0
        } else {
            outRect.left = itemSpace
        }
    }
}


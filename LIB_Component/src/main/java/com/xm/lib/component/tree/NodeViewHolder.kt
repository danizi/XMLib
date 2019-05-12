package com.xm.lib.component.tree

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.xm.lib.component.R


class NodeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        const val TAG = "NodeViewHolder"
    }

    private var viewHolder: ViewHolder? = null

    fun bind(node: Node, pos: Int, onItemClickListener: NodeTreeAdapter.OnItemClickListener) {
        if (viewHolder == null) {
            viewHolder = ViewHolder.create(itemView)
        }

        viewHolder?.tvTitle?.text = node.title
        if (node.childs.size == 0) {
            viewHolder?.ivArrow?.visibility = View.INVISIBLE
        }else{
            viewHolder?.ivArrow?.visibility = View.VISIBLE
        }

        var tempNode = node
        var parentCount = 0
        while (tempNode.parent != null) {
            tempNode = tempNode.parent!!
            parentCount++
        }
        val lp = RelativeLayout.LayoutParams(itemView.layoutParams)
        lp.setMargins(10 * parentCount, 0, 0, 0)

        itemView.layoutParams = lp
        itemView.setOnClickListener {
            onItemClickListener.onItemClick(itemView, pos)
            Log.d(TAG, "click ${node.title} pos:$pos")
        }
    }

    private class ViewHolder private constructor(val tvTitle: TextView, val ivArrow: ImageView) {
        companion object {

            fun create(rootView: View): ViewHolder {
                val tvTitle = rootView.findViewById(R.id.tv_title) as TextView
                val ivArrow = rootView.findViewById(R.id.iv_arrow) as ImageView
                return ViewHolder(tvTitle, ivArrow)
            }
        }
    }

}
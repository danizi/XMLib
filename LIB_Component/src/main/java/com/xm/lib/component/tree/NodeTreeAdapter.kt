package com.xm.lib.component.tree

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xm.lib.component.R
import java.util.*

class NodeTreeAdapter(val data: ArrayList<Node>) : RecyclerView.Adapter<NodeViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NodeViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_node, p0, false)
        return NodeViewHolder(view)
    }

    override fun onBindViewHolder(p0: NodeViewHolder, p1: Int) {
        p0.bind(data[p1], p1, onItemClickListener)
    }

    private val onItemClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, pos: Int) {
            if (data.size > pos) {
                val node = data[pos] //ps  Invalid index 3, size is 2
                if (node.isExpand == true) {
                    Log.d("Node", "收起")
                    node.isExpand = false
                    del(pos, node.childs)
                } else {
                    Log.d("Node", "展開")
                    node.isExpand = true
                    add(pos, node.childs)
                }
            } else {
                Log.e("Node", "data.size>pos")
            }
        }
    }

    override fun getItemCount(): Int {
        return if (data.isEmpty()) {
            0
        } else {
            data.size
        }
    }

    private fun add(pos: Int, childs: ArrayList<Node>) {
        data.addAll(pos + 1, childs)
        for (insertedPos in 1..childs.size) {
            notifyItemInserted(pos + insertedPos)
        }
        notifyItemRangeChanged(pos, data.size - pos)
    }

    private fun del(pos: Int, childs: ArrayList<Node>) {
        data.removeAll(childs)
        for (removedPos in 1..childs.size) {
            notifyItemRemoved(pos + removedPos)
        }
        notifyItemRangeChanged(pos, data.size - pos)
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, pos: Int)
    }
}
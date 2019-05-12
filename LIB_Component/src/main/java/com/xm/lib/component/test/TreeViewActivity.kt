package com.xm.lib.component.test

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.xm.lib.component.R
import com.xm.lib.component.tree.Node
import com.xm.lib.component.tree.NodeTreeAdapter
import java.util.ArrayList

class TreeViewActivity : AppCompatActivity() {
    private var mRecyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tree_view)
        mRecyclerView = findViewById(R.id.recycle_view)
        initData2()
    }

    private fun initData2() {
        val node0Childs = ArrayList<Node>()
        for (i in 1 .. 50){
            node0Childs.add(Node.Builder()
                    .title("章节1.$i")
                    .isExpand(false)
                    .childs()
                    .build())
        }
        val node0 = Node.Builder()
                .title("章节1")
                .isExpand(false)
                .childs(node0Childs)
                .build()


        val node1Childs = ArrayList<Node>()
        for (i in 1 .. 20){
            node1Childs.add(Node.Builder()
                    .title("章节2.$i")
                    .isExpand(false)
                    .childs()
                    .build())
        }
        val node1 = Node.Builder()
                .title("章节2")
                .isExpand(false)
                .childs(node1Childs)
                .build()

        val data = ArrayList<Node>()
        data.add(node0)
        data.add(node1)

        val adapter = NodeTreeAdapter(data)
        mRecyclerView?.layoutManager = MyLinearLayoutManager(this)
        mRecyclerView?.itemAnimator  = null
        mRecyclerView?.adapter = adapter
    }

    private class MyLinearLayoutManager(ctx: Context) : LinearLayoutManager(ctx) {
        override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
            try {
                super.onLayoutChildren(recycler, state)
            } catch (e: IndexOutOfBoundsException) {
                e.printStackTrace()
            }

        }
    }
}

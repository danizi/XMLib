package com.xm.lib.component.test

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.xm.lib.common.base.rv.v1.BaseRvAdapter
import com.xm.lib.common.base.rv.v1.BaseViewHolder
import com.xm.lib.component.R
import java.util.*

class MainActivity : AppCompatActivity() {

    private var rv: RecyclerView? = null
    private var adapter: Adapter? = Adapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv = findViewById(R.id.rv)

        val data = ArrayList<String>()
        data.add("XmIOSDialog")
        data.add("TreeView")
        data.add("SignView")
        data.add("Char")
        data.add("PopWindow")
        data.add("Tip")          //提示相关包括 对话框 / 弹出框 / toast / 广告
        data.add("RecyclerView")

        adapter?.data?.addAll(data)
        adapter?.addItemViewDelegate(
                0,
                ViewHolder::class.java,
                String::class.java,
                R.layout.item_select)
        rv?.layoutManager = LinearLayoutManager(this)
        rv?.adapter = adapter
    }

    class Adapter : BaseRvAdapter()

    class ViewHolder(view: View) : BaseViewHolder(view) {

        private var tv: TextView? = null

        override fun bindData(d: Any, position: Int) {
            tv = itemView.findViewById(R.id.tv)
            tv?.text = d as String
            tv?.setOnClickListener {
                when (position) {
                    0 -> {
                        itemView.context.startActivity(Intent(itemView.context, DialogActivity::class.java))
                    }
                    1 -> {
                        itemView.context.startActivity(Intent(itemView.context, TreeViewActivity::class.java))
                    }
                    2 -> {
                        itemView.context.startActivity(Intent(itemView.context, SignViewActivity::class.java))
                    }
                    3 -> {
                        itemView.context.startActivity(Intent(itemView.context, CharActivity::class.java))
                    }
                    4 -> {

                    }
                    5 -> {
                        itemView.context.startActivity(Intent(itemView.context, TipActivity::class.java))
                    }

                    6 -> {
                        itemView.context.startActivity(Intent(itemView.context, RvActivity::class.java))
                    }
                }
            }
        }
    }
}

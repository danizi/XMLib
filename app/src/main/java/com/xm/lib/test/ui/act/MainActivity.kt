package com.xm.lib.test.ui.act

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.xm.lib.common.base.rv.v2.BaseRvAdapterV2
import com.xm.lib.test.R
import com.xm.lib.test.holder.ItemVH

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rv = findViewById<RecyclerView>(R.id.rv)
        rv.adapter = BaseRvAdapterV2.Builder()
                .addDataResouce(getDataResouce())
                .addHolderFactory(ItemVH.Factory())
                .build()
        rv.layoutManager = LinearLayoutManager(this)
        rv?.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun getDataResouce(): ArrayList<Any> {
        applicationContext
        baseContext
        val items = ArrayList<Any>()
        items.add("Common")
        items.add("Component")
        items.add("Downloader")
        items.add("Media")
        items.add("Pay")
        items.add("Share")
        items.add("Statistics")
        items.add("Web")
        items.add("PrimaryTest")
        return items
    }
}

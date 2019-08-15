package com.xm.lib.test.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.xm.lib.downloader.DownManager
import com.xm.lib.downloader.task.DownTasker
import com.xm.lib.test.R
import com.xm.lib.test.holder.DownViewHolder

class DownAdapter(var downManager: DownManager?, var data: ArrayList<Any>? = ArrayList()) : RecyclerView.Adapter<DownViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DownViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_down, p0, false)
        return DownViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (data?.isEmpty()!!) {
            0
        } else {
            data?.size!!
        }
    }

    override fun onBindViewHolder(p0: DownViewHolder, p1: Int) {
        p0.bind(data?.get(p1) as DownTasker, downManager)
    }
}
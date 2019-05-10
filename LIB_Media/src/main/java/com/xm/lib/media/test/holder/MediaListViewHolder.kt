package com.xm.lib.media.test.holder

import android.app.Activity
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.xm.lib.common.log.BKLog
import com.xm.lib.media.R
import com.xm.lib.media.attachment.AttachmentPre
import com.xm.lib.media.base.XmVideoView
import com.xm.lib.media.test.MediaListEnt
import okhttp3.*
import java.io.IOException
import java.util.*

class MediaListViewHolder private constructor(val context: Context?, val rv: RecyclerView?) {

    companion object {
        val TAG = "MediaListViewHolder"
        fun create(context: Context?, rootView: View?): MediaListViewHolder {
            val rv = rootView?.findViewById<RecyclerView>(R.id.rv)
            return MediaListViewHolder(context, rv)
        }
    }

    init {
        initData()
        initEvent()
    }

    private fun initEvent() {
        rv?.addOnScrollListener(object : RecyclerView.OnScrollListener() {


            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView!!, dx, dy)
                val first = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                val last = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                val visibleCount = last - first + 1
                BKLog.d(TAG, " dx -> $dx   dy -> $dy")
                BKLog.d(TAG, " first -> $first   last -> $last visibleCount -> $visibleCount")
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView!!, newState)
            }
        })
    }

    private fun initData() {
        rv?.layoutManager = LinearLayoutManager(context)
        request() //请求播放列表，设置适配器
    }

    private fun request() {
        OkHttpClient.Builder().build().newCall(Request.Builder()
                .url("https://api.tradestudy.cn/v3/course?courseId=e90b1cbc845411e5a95900163e000c35")
                // ps:如果重新登录需要更改token
                .addHeader("x-tradestudy-access-token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IlpsczdzM0p6SG8zVHl6TkN3UU9iekUzakJNalB1L1loektHemNRYXlzOENHdkxBS1R5REFXbGt1K1FpdFE5WTJqTzAvNnJnQkgwVXA1cjJDYUxTakNBPT0iLCJwaG9uZSI6IjE1MDc0NzcwNzA4IiwiaWQiOiI2NTc4M2IxNWQ0NzcxMWU4OGI0NDAyNDJhYzEzMDAwMyIsInRva2VuIjoiZjc0OTEyYjIzYWFkNDIzMzliNjg1NDdmNzIyY2Y2NDEifQ.I2VniieCs33Q-78jkzfdI4O_aqosAiFOijpbCujtR5g")
                .addHeader("x-tradestudy-client-version", "3.4.3")
                .addHeader("x-tradestudy-client-device", "android_phone")
                .addHeader("x-tradestudy-access-key-id", "c")
                .build())
                .enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("", "")
                    }

                    @Throws(IOException::class)
                    override fun onResponse(call: Call, response: Response) {
                        requestSuccess(response)
                    }
                })
    }

    private fun requestSuccess(response: Response) {
        val body = response.body()?.string()
        val mediaListEnt = Gson().fromJson(body, MediaListEnt::class.java)
        val sections = ArrayList<MediaListEnt.ChaptersBean.SectionsBean>()
        for (ent in mediaListEnt.chapters) {
            sections.addAll(ent.sections)
        }
        (context as Activity).runOnUiThread {
            rv?.adapter = MediaListAdapter(sections)
        }
        Log.d("", "body:$body-$mediaListEnt")
    }
}

private class MediaListViewHolder2(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    fun bind(sectionsBean: MediaListEnt.ChaptersBean.SectionsBean, position: Int) {
        val xmVideoView = itemView.findViewById<XmVideoView>(R.id.video)
        val preUrl = sectionsBean.avatar
        val attachmentPre = AttachmentPre(itemView.context, preUrl)
        attachmentPre.url = sectionsBean.hls1
        xmVideoView.bindAttachmentView(attachmentPre)
        xmVideoView.addPlayerObserver(attachmentPre)
        itemView.setOnClickListener {
            xmVideoView.start(sectionsBean.hls1, true)
        }
    }
}

private class MediaListAdapter(val sections: ArrayList<MediaListEnt.ChaptersBean.SectionsBean>) : RecyclerView.Adapter<MediaListViewHolder2>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaListViewHolder2 {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.test_item_media, parent, false)
        return MediaListViewHolder2(view)
    }

    override fun getItemCount(): Int {
        return sections.size
    }

    override fun onBindViewHolder(holder: MediaListViewHolder2, position: Int) {
        holder.bind(sections[position], position)
    }
}
package com.xm.lib.media.test

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import com.google.gson.Gson
import com.xm.lib.common.log.BKLog

import com.xm.lib.media.R
import com.xm.lib.media.attachment.AttachmentComplete
import com.xm.lib.media.attachment.AttachmentGesture
import com.xm.lib.media.attachment.AttachmentPre
import com.xm.lib.media.attachment.control.AttachmentControl
import com.xm.lib.media.base.XmVideoView
import com.xm.lib.media.cache.M3u8Helper

import java.io.File
import java.io.IOException
import java.util.ArrayList

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class MainActivity : AppCompatActivity() {
    internal var TAG = "MainActivity"
    //private val xmVideoView: XmVideoView? = null
    private var tabLayout: TabLayout? = null
    private var vp: ViewPager? = null

    private fun noTitle() {
        //设置窗体为没有标题的模式
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        noTitle()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        iniData()
        iniEvent()


    }

    private fun iniEvent() {

    }

    private fun iniData() {
        val fragments = ArrayList<Fragment>()
        val titles = ArrayList<String>()
        titles.add("title")
        titles.add("title2")
        fragments.add(PageFragment.create("播放器测试", 0))
        fragments.add(PageFragment.create("播放列表测试", 1))
        vp!!.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(i: Int): Fragment {
                return fragments[i]
            }

            override fun getCount(): Int {
                return fragments.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return titles[position]
            }
        }

        tabLayout!!.setTabTextColors(Color.BLACK, Color.RED)//第一个参
        tabLayout!!.setSelectedTabIndicatorColor(Color.RED)//选中tab下划线颜色
        tabLayout!!.setSelectedTabIndicatorHeight(5)//选中tab下划线高度
        tabLayout!!.setupWithViewPager(vp)
    }


    private fun findViews() {
        tabLayout = findViewById(R.id.tab)
        vp = findViewById(R.id.vp)
    }

//    override fun onPause() {
//        super.onPause()
//        xmVideoView!!.onPause()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        xmVideoView!!.onResume()    //绑定播放服务
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        xmVideoView!!.onDestroy()
//    }

    @SuppressLint("ValidFragment")
    class PageFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val args = arguments
            val title = args!!.getString("title")
            val type = args.getInt("type")
            var view: View? = null
            when (type) {
                0 -> {
                    view = LayoutInflater.from(context).inflate(R.layout.test_frg_media, null, false)
                    MediaViewHolder.create(context, view!!)
                }
                1 -> {
                    view = LayoutInflater.from(context).inflate(R.layout.test_frg_media_list, null, false)
                    MediaListViewHolder.create(context, view)
                }
            }
            return view
        }

        companion object {

            fun create(title: String, type: Int): Fragment {
                val fragment = PageFragment()
                val args = Bundle()
                args.putString("title", title)
                args.putInt("type", type)
                fragment.arguments = args
                return fragment
            }
        }
    }

    /**
     * 播放页面测试
     */
    private class MediaViewHolder private constructor(val context: Context?, val xmVideoView: XmVideoView, val btnMedia2: Button, val btnSet: Button) {

        init {
            initData()
            initEvent()
        }

        private fun initEvent() {
            //解析m3u8地址
            btnSet.setOnClickListener { v ->
                val m3u8 = "http://hls.videocc.net/26de49f8c2/2/26de49f8c253b3715148ea0ebbb2ad95_1.m3u8"
                M3u8Helper.parseDownUrl(m3u8)
            }
        }

        private fun initData() {
            //    8090, /storage/emulated/0/XmDown
            //绑定数据
            val preUrl = "https://img-blog.csdn.net/20160413112832792?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQv/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center"
            val attachmentPre = AttachmentPre(context, preUrl)
            attachmentPre.url = "http://127.0.0.1:8890/26de49f8c273bbc8f6812d1422a11b39_2.m3u8"
            attachmentPre.url = Environment.getExternalStorageDirectory().toString() + File.separator + "26de49f8c273bbc8f6812d1422a11b39_2.m3u8"  //本地缓存m3u8视频
            val attachmentControl = AttachmentControl(context)
            val attachmentGesture = AttachmentGesture(context)
            val attachmentComplete = AttachmentComplete(context)

            xmVideoView.bindAttachmentView(attachmentPre)      //预览附着页面
            xmVideoView.bindAttachmentView(attachmentControl)  //控制器附着页面
            xmVideoView.bindAttachmentView(attachmentGesture)  //手势附着页面(调节亮度和音量)
            xmVideoView.bindAttachmentView(attachmentComplete) //播放完成附着页面

            xmVideoView.addPlayerObserver(attachmentPre)
            xmVideoView.addGestureObserver(attachmentPre)
            xmVideoView.addPhoneStateObserver(attachmentPre)

            xmVideoView.addPlayerObserver(attachmentControl)
            xmVideoView.addGestureObserver(attachmentControl)
            xmVideoView.addPhoneStateObserver(attachmentControl)

            xmVideoView.addPlayerObserver(attachmentGesture)
            xmVideoView.addGestureObserver(attachmentGesture)
            xmVideoView.addPhoneStateObserver(attachmentGesture)

            xmVideoView.addPlayerObserver(attachmentComplete)
            xmVideoView.addGestureObserver(attachmentComplete)
            xmVideoView.addPhoneStateObserver(attachmentComplete)
        }

        companion object {

            fun create(context: Context?, rootView: View): MediaViewHolder {
                val video = rootView.findViewById<XmVideoView>(R.id.video)
                val btnMedia2 = rootView.findViewById<Button>(R.id.btn_media2)
                val btnSet = rootView.findViewById<Button>(R.id.btn_set)
                return MediaViewHolder(context, video, btnMedia2, btnSet)
            }
        }
    }

    /**
     * 播放列表测试
     */
    private class MediaListViewHolder private constructor(val context: Context?, val rv: RecyclerView?) {

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

        }

        private fun initData() {
            rv?.layoutManager = LinearLayoutManager(context)
            rv?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val first = (recyclerView?.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    val last = (recyclerView?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val visibleCount = last - first;
                    BKLog.d(TAG, " dx -> $dx   dy -> $dy")
                    BKLog.d(TAG, " first -> $first   last -> $last visibleCount -> $visibleCount")
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })

            val okHttpClient = OkHttpClient.Builder().build()
            val request = Request.Builder()
                    .url("https://api.tradestudy.cn/v3/course?courseId=e90b1cbc845411e5a95900163e000c35")
                    .addHeader("x-tradestudy-access-token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IlpsczdzM0p6SG8zVHl6TkN3UU9iekUzakJNalB1L1loektHemNRYXlzOENHdkxBS1R5REFXbGt1K1FpdFE5WTJqTzAvNnJnQkgwVXA1cjJDYUxTakNBPT0iLCJwaG9uZSI6IjE1MDc0NzcwNzA4IiwiaWQiOiI2NTc4M2IxNWQ0NzcxMWU4OGI0NDAyNDJhYzEzMDAwMyIsInRva2VuIjoiZjc0OTEyYjIzYWFkNDIzMzliNjg1NDdmNzIyY2Y2NDEifQ.I2VniieCs33Q-78jkzfdI4O_aqosAiFOijpbCujtR5g")
                    .addHeader("x-tradestudy-client-version", "3.4.3")
                    .addHeader("x-tradestudy-client-device", "android_phone")
                    .addHeader("x-tradestudy-access-key-id", "c")
                    .build()
            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("", "")
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
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
            })
        }
    }

    private class MediaListViewHolder2(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(sectionsBean: MediaListEnt.ChaptersBean.SectionsBean) {
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
            holder.bind(sections[position])
        }
    }
}



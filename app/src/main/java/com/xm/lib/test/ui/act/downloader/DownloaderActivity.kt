package com.xm.lib.test.ui.act.downloader

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xm.lib.common.util.M3u8Helper
import com.xm.lib.common.util.M3u8Helper.parseDownUrl
import com.xm.lib.downloader.DownManager
import com.xm.lib.downloader.config.DownConfig
import com.xm.lib.downloader.enum_.DownErrorType
import com.xm.lib.downloader.enum_.DownStateType
import com.xm.lib.downloader.event.DownObserver
import com.xm.lib.downloader.task.DownTask
import com.xm.lib.downloader.task.DownTasker
import com.xm.lib.test.R
import com.xm.lib.test.adapter.DownAdapter
import com.xm.lib.test.holder.DownViewHolder
import okhttp3.*
import java.io.File
import java.io.IOException

class DownloaderActivity : AppCompatActivity() {
    private var rv: RecyclerView? = null
    private var btnAdd: Button? = null
    private var btnEdit: Button? = null
    private var btnDelete: Button? = null
    private var xmDownTest: XmDownTest? = null
    private var count = 0
    private val downUrlArray = arrayOf(
            "http://img1.imgtn.bdimg.com/it/u=2735633715,2749454924&fm=26&gp=0.jpg",
            "http://img4.imgtn.bdimg.com/it/u=3590849871,3724521821&fm=26&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=4060543606,3642835235&fm=26&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3282593745,642847689&fm=26&gp=0.jpg",
            "https://apk.apk.xgdown.com/down/1hd.apk",
            "https://dl.hz.37.com.cn/upload/1_1002822_10664/shikongzhiyiH5_10664.apk",
            "https://cavedl.leiting.com/full/caveonline_M141859.apk",
            "http://gyxz.ukdj3d.cn/vp/yx_sw1/warsong.apk",
            "http://gyxz.ukdj3d.cn/vp1/yx_ljun1/Pokemmo.apk",
            "http://gyxz.ukdj3d.cn/hk1/yx_xxm1/shanshuozhiguang.apk"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_down)
        //startActivity(Intent(this, M3u8DownActivity::class.java))
        findViews()
        iniData()
        initEvent()
        initDisplay()
    }

    private fun findViews() {
        rv = findViewById(R.id.rv)
        btnAdd = findViewById(R.id.btn_add)
        btnEdit = findViewById(R.id.btn_edit)
        btnDelete = findViewById(R.id.btn_delete)
    }

    private fun iniData() {
        xmDownTest = XmDownTest(this)
//        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //创建文件夹
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        val file = File("" + Environment.getExternalStorageDirectory() + "/test/")
                        if (!file.exists()) {
                            Log.d("result", "create result:" + file.mkdirs())
                        }
                    }
                }
            }
        }
    }

    private fun initEvent() {
        btnAdd?.setOnClickListener {
                        if (count < downUrlArray.size) {
                xmDownTest?.add(downUrlArray[count])
                count++
            }
        }
        btnEdit?.setOnClickListener {
            xmDownTest?.editMode()
        }


        btnDelete?.setOnClickListener {
            xmDownTest?.delete()
        }
    }

    private fun initDisplay() {
        xmDownTest?.bindRv(rv)
        xmDownTest?.initDisplay()
    }

    override fun onDestroy() {
        super.onDestroy()
        xmDownTest?.exit()
    }

    private fun mediaDown() {
        val m3u8 = "http://hls.videocc.net/26de49f8c2/2/26de49f8c253b3715148ea0ebbb2ad95_1.m3u8"
        parseDownUrl(m3u8)
        OkHttpClient().newCall(Request.Builder().url(m3u8).build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                val url = M3u8Helper.getDownUrls(response.body()?.byteStream())
                this@DownloaderActivity.runOnUiThread {
                    for (u in url) {
                        xmDownTest?.add(u)
                    }
                    xmDownTest?.add("http://hls.videocc.net/26de49f8c2/5/26de49f8c253b3715148ea0ebbb2ad95_1.key")
                }
            }
        })
    }
}

class XmDownTest(var context: Context) {
    companion object {
        private const val TAG = "XmDownTest"
    }

    private var downManager: DownManager? = null
    private var rv: RecyclerView? = null
    private var downAdapter: DownAdapter? = null

    init {
        downManager = DownManager.createDownManager(context)
        downManager?.downObserverable()?.registerObserver(object : DownObserver {
            override fun onPause(tasker: DownTasker) {
                notifyUI(tasker, DownStateType.PAUSE)
            }

            override fun onDelete(tasker: DownTasker) {
                //notifyUI(tasker, DownStateType.DELETE)
                notifyDelete(tasker)
            }

            private fun notifyDelete(tasker: DownTasker) {
                val it = downAdapter?.data?.iterator() ?: return
                var i = 0
                while (it.hasNext()) {
                    val taskerTemp = (it.next() as DownTasker)
                    if (taskerTemp.task.uuid == tasker.task.uuid) {
                        it.remove()
                        (context as Activity).runOnUiThread {
                            rv?.adapter?.notifyItemRangeChanged(i, 1)
                        }
                        break
                    }
                    i++
                }
            }

            override fun onComplete(tasker: DownTasker, total: Long) {
                notifyUI(tasker, DownStateType.COMPLETE, total)
            }

            override fun onError(tasker: DownTasker, typeError: DownErrorType, s: String) {
                notifyUI(tasker, DownStateType.ERROR, DownConfig.DEFAULT, typeError, s)
            }

            override fun onProcess(tasker: DownTasker, process: Long, total: Long, present: Float) {
                notifyUI(tasker, DownStateType.RUNNING, total, null, "", process, present)
            }
        })
        downAdapter = DownAdapter(downManager, ArrayList())
    }

    fun bindRv(rv: RecyclerView?) {
        this.rv = rv
        rv?.itemAnimator = null
        rv?.adapter = downAdapter
        rv?.layoutManager = LinearLayoutManager(context)
    }

    private val handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            val tasker = msg?.obj as DownTasker
            notifyItem(tasker.task)
        }
    }

    fun notifyUI(tasker: DownTasker, stateType: DownStateType, total: Long = 0, typeError: DownErrorType? = null, s: String = "", process: Long = 0L, present: Float = 0F) {
        when (stateType) {
            DownStateType.COMPLETE -> {
                notifyItem(tasker.task)
            }
            DownStateType.ERROR -> {
                notifyItem(tasker.task)
            }
            DownStateType.RUNNING -> {
                val msg = handler.obtainMessage()
                msg.what = 1
                msg.obj = tasker
                handler.sendMessageDelayed(msg, 1000)
            }
            DownStateType.PAUSE -> {
                notifyItem(tasker.task)
            }
            DownStateType.DELETE -> {
                notifyItem(tasker.task)
            }
            else -> {
            }
        }
    }

    private fun notifyItem(task: DownTask) {
        for (i in 0..(downAdapter?.data?.size!! - 1)) {
            val tempTask = (downAdapter?.data!![i] as DownTasker)
            if (tempTask.task.uuid == task.uuid) {
                val tasker = ((downAdapter?.data!![i]) as DownTasker)
                tasker.task = task
                //主线程上刷新
                (context as Activity).runOnUiThread {
                    rv?.adapter?.notifyItemRangeChanged(i, 1)
                }
            }
        }
    }

    fun exit() {
        downManager?.pauseAllDownTasker()
    }

    @SuppressLint("CheckResult")
    fun add(url: String) {
        RxPermissions(context as AppCompatActivity)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe { aBoolean ->
                    if (aBoolean!!) {
                        //当所有权限都允许之后，返回true
                        val task = DownTask()
                        task.url = url
                        downAdapter?.data?.add(downManager?.createDownTasker(task)!!)
                        rv?.adapter?.notifyDataSetChanged()
                    } else {
                        //只要有一个权限禁止，返回false，
                        //下一次申请只申请没通过申请的权限
                        Log.i("permissions", "btn_more_sametime：$aBoolean")
                    }
                }
    }

    fun initDisplay() {
        //首先从数据库中读取任务
        val tasks = downManager?.downDao()?.queryAll() ?: return
        for (task in tasks) {
            downAdapter?.data?.add(downManager?.createDownTasker(task)!!)
        }
        rv?.adapter?.notifyDataSetChanged()
    }

    fun editMode() {
        /*编辑模式*/
        DownViewHolder.isEdit = !DownViewHolder.isEdit
        downAdapter?.notifyDataSetChanged()
    }

    fun delete() {
        /*删除任务*/
        for (tasker in downAdapter?.data!!) {
            val downTasker = tasker as DownTasker
            if (downTasker.task.isSelect) {
                downTasker.delete()
            }
        }
    }
}

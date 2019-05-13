package com.xm.lib.downloader.test

import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import com.xm.lib.common.util.M3u8Helper
import com.xm.lib.common.util.M3u8Helper.parseDownUrl
import com.xm.lib.downloader.R
import com.xm.lib.downloader.test.m3u8down.M3u8DownActivity
import okhttp3.*
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_main)
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
                this@MainActivity.runOnUiThread {
                    for (u in url) {
                        xmDownTest?.add(u)
                    }
                    xmDownTest?.add("http://hls.videocc.net/26de49f8c2/5/26de49f8c253b3715148ea0ebbb2ad95_1.key")
                }

            }
        })
    }
}

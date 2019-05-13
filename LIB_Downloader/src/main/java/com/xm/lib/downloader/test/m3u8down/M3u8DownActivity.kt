package com.xm.lib.downloader.test.m3u8down

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.xm.lib.common.log.BKLog
import com.xm.lib.downloader.DownManager
import com.xm.lib.downloader.R
import com.xm.lib.downloader.enum_.DownErrorType
import com.xm.lib.downloader.event.DownObserver
import com.xm.lib.downloader.task.DownTask
import com.xm.lib.downloader.task.DownTasker
import com.xm.lib.downloader.utils.FileUtil
import okhttp3.*
import java.io.*

class M3u8DownActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_m3u8_down)
        /*
         * 需求：
         *     缓存m3u8文件，让播放器能够本地播放
         *
         * 基础认知：
         *         m3u8文件格式讲解：https://www.cnblogs.com/shakin/p/3870439.html
         *
         * 操作步骤：
         *         1 获取m3u8 key和ts地址，并将其缓存下来
         *         2 缓存m3u8 文件，并且将里面的域名替换成本地路径
         *         3 播放器调用本地m3u8文件进行播放
         */
        val m3u8 = "http://hls.videocc.net/26de49f8c2/2/26de49f8c253b3715148ea0ebbb2ad95_1.m3u8"
        val m3u8DownDir = "M3u8/adf"
        // 请求网络
        val okHttpClient = OkHttpClient()
        val request = Request.Builder().url(m3u8).get().build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                BKLog.d("请求m3u8失败")
            }

            override fun onResponse(call: Call, response: Response) {
                val inputStream = response.body()?.byteStream()
                val (m3u8Key, m3u8Ts) = analysis(inputStream)
                //writeLocal(inputStream, File(""))

                //开启下载的线程
                val downManager = DownManager.createDownManager(this@M3u8DownActivity)

                val task = DownTask()
                task.url = m3u8Key
                downManager.createDownTasker(task).enqueue()
                for (ts in m3u8Ts) {
                    val task = DownTask()
                    task.url = ts
                    downManager.createDownTasker(task).enqueue()
                }

                downManager.pauseAllDownTasker()

                //进度
                downManager.downObserverable()?.registerObserver(object : DownObserver {
                    override fun onComplete(tasker: DownTasker, total: Long) {
                        BKLog.d(task.fileName+"total:$total")
                    }

                    override fun onError(tasker: DownTasker, typeError: DownErrorType, s: String) {

                    }

                    override fun onProcess(tasker: DownTasker, process: Long, total: Long, present: Float) {
                       // BKLog.d(task.fileName+"process:$process")
                    }

                    override fun onPause(tasker: DownTasker) {

                    }

                    override fun onDelete(tasker: DownTasker) {

                    }
                })
            }

            private fun analysis(inputStream: InputStream?): Pair<String, ArrayList<String>> {
                val ts = ArrayList<String>()
                var key = ""
                val br = BufferedReader(InputStreamReader(inputStream))
                while (true) {
                    val line = br.readLine()
                    if (line == null) {
                        BKLog.d("文件读取完成")
                        return Pair(key, ts)
                    }

                    //保存key地址
                    if (line.startsWith("#EXT-X-KEY")) {
                        val startIndex = line.indexOf("\"") + 1
                        val endIndex = line.lastIndexOf("\"")
                        key = line.substring(startIndex, endIndex)
                    }

                    //保存ts地址
                    if (line.startsWith("http") || line.startsWith("https")) {
                        ts.add(line)
                    }
                }
                BKLog.d("********************")
                BKLog.d("mu38文件截取key:$key")
                for (t in ts?.iterator()) {
                    BKLog.d("mu38文件截取ts:$t")
                }
                BKLog.d("********************")
                return Pair(key, ts)
            }

            private fun writeLocal(inputStream: InputStream?, url:String) {
                val outFile = FileUtil.createNewFile(Environment.getExternalStorageDirectory().canonicalPath,"xmDown/${m3u8FileName(url)}",m3u8FileName(url))

                val outputStream = outFile?.outputStream()
                var length = 0
                val buff = ByteArray(1024 * 1)
                while (true) {
                    length = inputStream?.read(buff)!!
                    if (length == -1) {
                        return
                    }
                    outputStream?.write(buff)
                }
            }

            private fun m3u8FileName(url: String): String {
                val start = url.lastIndexOf("/") + 1
                val end = url.lastIndexOf(".")
                val name = url.substring(start, end)
                BKLog.d("${url}解析m3u8文件名称：$name")
                return name
            }
        })
    }
}

fun main(args: Array<String>) {
    val line = "#EXT-X-KEY:METHOD=AES-128,URI=\"http://hls.videocc.net/26de49f8c2/6/26de49f8c22abafd8adc1b49246262c6_1.key\",IV=0x67a8ba97a8a8ea61fca434869eb9c8ed"
    if (line.startsWith("#EXT-X-KEY")) {
        val startIndex = line.indexOf("\"") + 1
        val endIndex = line.lastIndexOf("\"")
        val key = line.substring(startIndex, endIndex)
        print("mu38文件截取key:$key")
    }
}

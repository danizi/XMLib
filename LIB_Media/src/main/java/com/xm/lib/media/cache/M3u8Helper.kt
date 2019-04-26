package com.xm.lib.media.cache

import android.os.Environment
import okhttp3.*
import java.io.*
import java.io.File.separator
import android.os.Environment.getExternalStorageDirectory
import com.xm.lib.common.log.BKLog


/**
 * m3u8本地缓存思路
 * 1 解析 m3u8中视频真实地址
 * 2 下载真实的视频到本地
 * 3 替换 m3u8中视频真实地址
 * 4 播放器播放直接调用本地m3u8文件
 */
object M3u8Helper {
    private val TAG = "M3u8Helper"
    /**
     * @param m3u8 播放真实地址
     */
    fun parseDownUrl(m3u8: String) {
        OkHttpClient().newCall(Request.Builder().url(m3u8).build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                getDownUrls(response.body()?.byteStream())

                val cachePaths = ArrayList<String>()
                for (i in 0..73) {
                    val cache = "" + Environment.getExternalStorageDirectory() + File.separator + "XmDown" + File.separator + "26de49f8c253b3715148ea0ebbb2ad95_1_" + i + ".ts"
                    cachePaths.add(cache)
                }
                val cacheM3u8File = File("" + Environment.getExternalStorageDirectory() + File.separator + "26de49f8c273bbc8f6812d1422a11b39_2.m3u8")
                var create: Boolean? = true
                if (!cacheM3u8File.exists()) {
                    try {
                        create = cacheM3u8File.createNewFile()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
                if (create!!) {
                    cacheM3U8(m3u8, cachePaths, cacheM3u8File)
                }
            }
        })
    }

    private fun getDownUrls(inputStream: InputStream?): ArrayList<String> {
        val urls = ArrayList<String>()

        val br = BufferedReader(InputStreamReader(inputStream))
        var line: String? = null
        while (true) {
            line = br.readLine() ?: break
//            if (line == null) break
            if (line.startsWith("http://")) {
                urls.add(line)
            }

        }
        br.close()
        inputStream?.close()

        return urls
    }

    /**
     * @param m3u8 播放真实地址
     * @param cachePaths m3u8中解析的地址
     * @param cacheM3u8File 缓存m3u8文件
     */
    fun cacheM3U8(m3u8: String, cachePaths: ArrayList<String>, cacheM3u8File: File) {
        // 首先判断权限
        if (!cacheM3u8File.exists()) {
            throw Exception(cacheM3u8File.absolutePath + "不存在")
        }
        OkHttpClient().newCall(Request.Builder().url(m3u8).build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                val br = BufferedReader(InputStreamReader(response.body()?.byteStream()))
                val bw = BufferedWriter(OutputStreamWriter(FileOutputStream(cacheM3u8File)))
                var line: String? = null
                var index = 0
                while (true) {
                    line = br.readLine()
                    if (line == null) break
                    if (line.startsWith("http://")) {
                        line = cachePaths[index++]
                    }
                    if (line.startsWith("#EXT-X-KEY:METHOD=AES-128")) {
                        line = "#EXT-X-KEY:METHOD=AES-128,URI=\"/storage/emulated/0/XmDown/26de49f8c253b3715148ea0ebbb2ad95_1.key\",IV=0xd1daf60b88d67793bf7a8f07e6c7ebd1"
                    }
                    bw.write(line + "\r\n")
                }
                bw.close()
                br.close()
            }
        })
    }

}
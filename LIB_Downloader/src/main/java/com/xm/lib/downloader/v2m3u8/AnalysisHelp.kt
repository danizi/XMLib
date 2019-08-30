package com.xm.lib.downloader.v2m3u8

import android.text.TextUtils
import com.xm.lib.common.log.BKLog
import okhttp3.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

/**
 * m3u8文件解析
 */
class AnalysisHelp {

    interface Callback {
        fun onSuccess(m3u8Key: String, m3u8Ts: ArrayList<String>)
        fun onFailure(e: Exception)
    }


    /**
     * 解析m3u8文件到到本地
     */
    fun analysisM3u8File(m3u8: String, callback: Callback) {

        if (TextUtils.isEmpty(m3u8)) {
            callback.onFailure(Exception("m3u8 is null"))
            return
        }

        val okHttpClient = OkHttpClient()
        val request = Request.Builder().url(m3u8).get().build()
        okHttpClient.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                val pair = analysis(response.body()?.byteStream())
                callback.onSuccess(pair.first, pair.second)
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
}
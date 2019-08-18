package com.xm.lib.downloader.v2

import com.xm.lib.downloader.v2.imp.IRequest
import okhttp3.*
import java.io.IOException

/**
 * 下载请求
 */
open class XmDownRequest private constructor(val b: Builder) : IRequest() {

    class Builder {
        /**
         * 资源id
         */
        var id: String = ""

        /**
         * 下载地址
         */
        var url: String? = ""

        /**
         * 下载文件名称，不传则由系统默认设置
         */
        var fileName: String? = ""

        fun url(url: String): Builder {
            this.url = url
            return this
        }

        fun fileName(fileName: String): Builder {
            this.fileName = fileName
            return this
        }

        fun build(): XmDownRequest {
            return XmDownRequest(this)
        }
    }

}

class DownInfoProvider {

    fun url(listener: ProviderListener) {
        val client = OkHttpClient()
        client.newCall(Request.Builder().build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {

            }
        })
    }

    fun fileName(): String {
        return ""
    }

    interface ProviderListener {

        fun onSuceess() {

        }

        fun onFailure() {

        }
    }
}
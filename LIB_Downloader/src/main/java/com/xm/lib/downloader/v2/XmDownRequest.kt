package com.xm.lib.downloader.v2

import com.xm.lib.downloader.v2.abs.AbsRequest

/**
 * 下载请求
 */
open class XmDownRequest private constructor(private val b: Builder) : AbsRequest() {

    init {
        id = b.id
        url = b.url
        fileName = b.fileName
    }

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
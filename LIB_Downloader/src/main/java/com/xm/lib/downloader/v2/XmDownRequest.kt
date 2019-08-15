package com.xm.lib.downloader.v2

/**
 * 下载请求
 */
class XmDownRequest private constructor(val b: Builder) {

    class Builder {
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
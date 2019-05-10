package com.xm.lib.share

class ShareConfig(builder: Builder) {

    var appid = ""

    init {
        appid = builder.appid
    }


    class Builder {
        var appid = ""
        fun appid(appid: String):Builder {
            this.appid = appid
            return this
        }

        fun build(): ShareConfig {
            return ShareConfig(this)
        }
    }
}
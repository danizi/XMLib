package com.xm.lib.pay

class PayConfig(builder: Builder) {

    var appid = ""

    init {
        appid = builder.appid
    }


    class Builder {
        var appid = ""
        fun appid(appid: String) {
            this.appid = appid
        }

        fun build(): PayConfig {
            return PayConfig(this)
        }
    }
}
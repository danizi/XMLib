package com.xm.lib.pay

class PayConfig(builder: Builder) {

    var appid = ""

    init {
        appid = builder.appid
    }


    class Builder {
        var appid = ""
        fun appid(appid: String): Builder {
            this.appid = appid
            return this
        }

        fun build(): PayConfig {
            return PayConfig(this)
        }
    }
}
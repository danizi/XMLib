package com.xm.lib.pay.wx.uikit.wxapi

import com.google.gson.annotations.SerializedName

class PayParameters {
    /**
     * package : Sign=WXPay
     * appid : wxd37fb8ce51a02360
     * sign : A1C433367FC6FA1B279DF25579235CC1
     * partnerid : 1507283191
     * prepayid : wx091153404274087ebf63570c2172257868
     * noncestr : 61acc180b95a42eb8d7822032fc354a9
     * timestamp : 1557374020
     */
    @SerializedName("package")
    var packageName: String? = null
    var appid: String? = null
    var sign: String? = null
    var partnerid: String? = null
    var prepayid: String? = null
    var noncestr: String? = null
    var timestamp: String? = null
    override fun toString(): String {
        return "PayParameters(packageName=$packageName, appid=$appid, sign=$sign, partnerid=$partnerid, prepayid=$prepayid, noncestr=$noncestr, timestamp=$timestamp)"
    }

}
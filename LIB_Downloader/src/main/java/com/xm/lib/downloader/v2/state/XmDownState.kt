package com.xm.lib.downloader.v2.state

/**
 * 下载状态枚举
 */
object XmDownState {
    const val START = "队列中..."
    const val NOT_STARTED = "未下载"
    const val RUNNING = "运行"
    const val CANCLE = "取消"
    const val PAUSE = "暂停"
    const val COMPLETE = "完成"
    const val ERROR = "下载错误"
    fun getError(type: XmDownError): String {
        var subError = ""
        when (type) {
            XmDownError.CREATE_FILE_ERROR -> {
                subError = "创建文件目录失败"
            }
            XmDownError.NETWORK -> {
                subError = "网络连接失败"
            }
            XmDownError.NO_SPACE -> {
                subError = "手机空间不足"
            }
            XmDownError.UNKNOWN -> {
                subError = "未知错误"
            }
            XmDownError.CLIENT -> {
                subError = "服务端请求端错误"
            }
            XmDownError.SERVER -> {
                subError = "服务端请求端错误"
            }
            else -> {
                subError = "其他错误"
            }
        }
        return "下载错误 - $subError"
    }
}
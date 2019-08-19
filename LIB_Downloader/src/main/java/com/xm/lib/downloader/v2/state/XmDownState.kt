package com.xm.lib.downloader.v2.state

/**
 * 下载状态枚举
 */
object XmDownState {
    const val START = "开始下载"
    const val NOT_STARTED = "未下载"
    const val RUNNING = "运行"
    const val CANCLE = "取消"
    const val COMPLETE = "完成"
    const val ERROR = "下载错误"

    const val ERROR_CARETE_FILE = 0x01
    const val ERROR_NETWORK = 0x02
    const val ERROR_NO_SPACE = 0x03

    fun getError(type: Int): String {
        var subError = ""
        when (type) {
            ERROR_CARETE_FILE -> {
                subError = "创建文件目录失败"
            }
            ERROR_NETWORK -> {
                subError = "网络未连接"
            }
            ERROR_NO_SPACE -> {
                subError = "手机空间不足"
            }
        }
        return "下载错误 - $subError"
    }

}
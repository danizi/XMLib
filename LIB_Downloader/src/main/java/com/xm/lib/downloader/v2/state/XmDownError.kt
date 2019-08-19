package com.xm.lib.downloader.v2.state

/**
 * 下载错误信息枚举
 */
enum class XmDownError {
    /**
     * 空间不足情况
     */
    NO_SPACE,
    /**
     * 文件创建错误
     */
    CREATE_FILE_ERROR,
    /**
     * 连接超时情况
     */
    CONNECT_TIMEOUT,
    /**
     * 未知错误
     */
    UNKNOWN,
    /**
     * 请求客服端错误
     */
    CLIENT,
    /**
     * 请求服务器错误
     */
    SERVER,
    /**
     * 网络错误
     */
    NETWORK
}


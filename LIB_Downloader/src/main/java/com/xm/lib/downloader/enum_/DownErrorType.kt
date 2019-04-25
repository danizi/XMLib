package com.xm.lib.downloader.enum_

enum class DownErrorType {
    NO_SPACE, //空间不足情况
    CREATE_FILE_ERROR, //文件创建错误
    CONNECT_TIMEOUT, //连接超时情况
    UNKNOWN,  //未知错误
    REQUEST_FAILURE
}
package com.xm.lib.downloader.enum_

@Deprecated("")
enum class DownStateType {
    NOT_STARTED,   //未下载
    RUNNING,//运行
    PAUSE,//暂停
    START,//开始
    COMPLETE,//完成状态
    ERROR,//下载错误
    DELETE//任务删除
}
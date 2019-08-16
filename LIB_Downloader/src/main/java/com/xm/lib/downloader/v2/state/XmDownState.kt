package com.xm.lib.downloader.v2.state

/**
 * 下载状态枚举
 */
enum class XmDownState {
    /**
     * 未下载
     */
    NOT_STARTED,
    /**
     * 运行
     */
    RUNNING,
    /**
     * 暂停
     */
    PAUSE,
    /**
     * 开始
     */
    START,
    /**
     * 完成状态
     */
    COMPLETE,
    /**
     * 下载错误
     */
    ERROR,
    /**
     * 任务删除
     */
    DELETE
}
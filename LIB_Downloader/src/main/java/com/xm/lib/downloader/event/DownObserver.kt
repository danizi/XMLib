package com.xm.lib.downloader.event

import com.xm.lib.downloader.enum_.DownErrorType
import com.xm.lib.downloader.task.DownTasker


interface DownObserver {
    fun onComplete(tasker: DownTasker, total: Long)
    fun onError(tasker: DownTasker, typeError: DownErrorType, s: String)
    fun onProcess(tasker: DownTasker, process: Long, total: Long, present: Float)
    fun onPause(tasker: DownTasker)
    fun onDelete(tasker: DownTasker)
}
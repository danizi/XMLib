package com.xm.lib.downloader.event

import com.xm.lib.downloader.enum_.DownErrorType
import com.xm.lib.downloader.task.DownTasker
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class DownObservable {
    companion object {
        const val TAG = "DownObserverable"
    }

    private var queue: BlockingQueue<DownObserver>? = LinkedBlockingQueue<DownObserver>()

    @Synchronized
    fun registerObserver(o: DownObserver) {
        queue?.add(o)
    }

    @Synchronized
    fun removeObserver(o: DownObserver) {
        queue?.remove(o)
    }

    fun notifyObserver(type: Int, tasker: DownTasker? = null, typeError: DownErrorType? = null, s: String? = "", process: Long = -1, total: Long = -1, present: Float = -1f) {
        if (tasker == null) return
        for (downObserverable in queue!!) {
            when (type) {
                0 -> {
                    downObserverable.onComplete(tasker, total)
                }
                1 -> {
                    if (typeError == null) return
                    downObserverable.onError(tasker, typeError, s!!)
                }
                2 -> {
                    if (process > 0 && total > 0 && present > 0) {
                        downObserverable.onProcess(tasker, process, total, present)
                    }
                }
                3 -> {
                    downObserverable.onPause(tasker)
                }
                4 -> {
                    downObserverable.onDelete(tasker)
                }
            }
        }
    }

    fun notifyObserverComplete(tasker: DownTasker?, total: Long) {
        notifyObserver(0, tasker, null, "", -1, total)
    }

    fun notifyObserverError(tasker: DownTasker?, typeError: DownErrorType, s: String) {
        notifyObserver(1, tasker, typeError, s)
    }

    fun notifyObserverProcess(tasker: DownTasker?, process: Long, total: Long, present: Float) {
        notifyObserver(2, tasker, null, "", process, total, present)
    }

    fun notifyObserverPause(tasker: DownTasker?) {
        notifyObserver(3, tasker)
    }

    fun notifyObserverDelete(tasker: DownTasker?) {
        notifyObserver(4, tasker)
    }
}
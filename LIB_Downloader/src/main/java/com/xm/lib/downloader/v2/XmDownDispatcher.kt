package com.xm.lib.downloader.v2

import com.xm.lib.common.log.BKLog
import com.xm.lib.downloader.v2.imp.IXmDownDispatcher
import java.util.concurrent.*

/**
 * 任务调度
 */
class XmDownDispatcher : IXmDownDispatcher {

    private var runningQueueNum = 5
    private var pool: ThreadPoolExecutor? = null
    private val runningQueue = LinkedBlockingQueue<XmRealCall.DownRunnable>()
    private val readyQueue = LinkedBlockingQueue<XmRealCall.DownRunnable>()
    private val finishedQueue = LinkedBlockingQueue<XmRealCall.DownRunnable>()

    constructor(runningQueueNum: Int) {
        this.runningQueueNum = runningQueueNum
        pool = ThreadPoolExecutor(this.runningQueueNum, this.runningQueueNum, 30, TimeUnit.SECONDS, ArrayBlockingQueue(2000))
    }

    constructor(pool: ThreadPoolExecutor?) {
        this.pool = pool
    }

    constructor() {
        pool = ThreadPoolExecutor(runningQueueNum, runningQueueNum, 30, TimeUnit.SECONDS, ArrayBlockingQueue(2000))
    }

    companion object {
        const val TAG = "XmDownDispatcher"
    }

    override fun enqueue(downRunnable: XmRealCall.DownRunnable) {
        if (runningQueue.size < runningQueueNum) {
            runningQueue.add(downRunnable)
            pool?.submit(downRunnable)
            BKLog.d(TAG, "进入运行队列 : ${downRunnable.request.b.url}")
        } else {
            readyQueue.add(downRunnable)
        }
    }

    override fun finished(downRunnable: XmRealCall.DownRunnable) {
        if (runningQueue.contains(downRunnable)) {
            try {
                /**
                 * poll -->【若队列为空，返回null】
                 * remove >【若队列为空，抛出NoSuchElementException异常】
                 * take -->【若队列为空，发生阻塞，等待有元素】
                 */
                val task = readyQueue.poll()
                if (task != null) {
                    enqueue(task)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                BKLog.e(TAG, "准备队列提交到线程池中失败 ${e.message}")
            } finally {
                runningQueue.remove(downRunnable)
                finishedQueue.add(downRunnable)
            }
        } else {
            BKLog.e(TAG, "运行任务移除失败")
        }
    }

    override fun cancel(downRunnable: XmRealCall.DownRunnable) {
        if (runningQueue.contains(downRunnable)) {
            runningQueue.remove(downRunnable)
        }
        pool?.remove(downRunnable)
    }

    override fun cancelAll() {
        for (running in runningQueue) {
            pool?.remove(running)
        }
        readyQueue.clear()
    }
}

package com.xm.lib.downloader

import android.content.Context
import android.os.Environment
import com.xm.lib.downloader.config.DownConfig
import com.xm.lib.downloader.db.DownDao
import com.xm.lib.downloader.dispatcher.DownDispatcher
import com.xm.lib.downloader.event.DownObservable
import com.xm.lib.downloader.task.DownTask
import com.xm.lib.downloader.task.DownTasker
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class DownManager {
    var context: Context? = null
    private var downDao: DownDao? = null //数据库操作对象
    private var downConfig: DownConfig? = null //配置对象
    private var downDispatcher: DownDispatcher? = null //下载分发器
    private var downObserverable: DownObservable? = null //被观察者对象 ps:监听下载状态，相比监听接口更灵活
    private var downTaskers: ArrayList<DownTasker>? = null

    private constructor()

    private constructor(context: Context?, downDao: DownDao?, downConfig: DownConfig, downDispatcher: DownDispatcher, downObserverable: DownObservable) {
        this.context = context
        this.downDao = downDao
        this.downConfig = downConfig
        this.downDispatcher = downDispatcher
        this.downObserverable = downObserverable
        this.downTaskers = ArrayList()
    }

    companion object {
        fun createDownManager(context: Context): DownManager {
            //初始化数据库
//            val dao = DownDao(context, "XmDown", null, 100)
            val dao: DownDao? = null
            //初始化配置参数
            val config = DownConfig()
            config.path = Environment.getExternalStorageDirectory().absolutePath
            config.dir = "xmDown/26de49f8c253b3715148ea0ebbb2ad95_1"
            config.threadNum = 2
            config.downTaskerPool = ThreadPoolExecutor(config.threadNum.toInt(), config.threadNum.toInt(), 30, TimeUnit.SECONDS, ArrayBlockingQueue(2000)) //多任务下载线程池
            config.isMultiRunnable = false
            config.isSingleRunnable = true
            config.bufferSize =  1024 * 4
            config.runqueues = 2
            config.downDispatcherPool = ThreadPoolExecutor(config.runqueues.toInt(), config.runqueues.toInt(), 30, TimeUnit.SECONDS, ArrayBlockingQueue(2000)) //分发器线程池

            //初始化分发器
            val dispatcher = DownDispatcher()
            dispatcher.pool = config.downDispatcherPool
            dispatcher.runqueues = config.runqueues
            dispatcher.readyQueue = LinkedBlockingQueue<DownTasker>()
            dispatcher.runningQueue = LinkedBlockingQueue<DownTasker>()

            //初始化观察者 ps：任务状态监听器
            val observerable = DownObservable()

            /*创建下载管理者*/
            return DownManager(context, dao, config, dispatcher, observerable)
        }
    }

    fun createDownTasker(task: DownTask): DownTasker {
        /*创建下载者*/
        val downTasker = DownTasker(this, task)
        downTaskers?.add(downTasker)
        return downTasker
    }

    fun pauseAllDownTasker() {
        /*暂停所有下载任务*/
        if (downTaskers?.isNotEmpty()!!) {
            for (tasker in downTaskers!!) {
                tasker.pause()
            }
        }
    }

    fun deleteAllDownTasker() {
        /*删除所有下载任务*/
        if (downTaskers?.isNotEmpty()!!) {
            for (tasker in downTaskers!!) {
                tasker.delete()
            }
        }
    }

    fun pauseDownTasker(task: DownTask) {
        /*暂停指定下载任务*/
        if (downTaskers?.isNotEmpty()!!) {
            val iterator = downTaskers?.iterator()
            while (iterator?.hasNext()!!) {
                val tasker = iterator.next()
                if (tasker.task == task) {
                    tasker.pause()
                    iterator.remove()
                }
            }
        }
    }

    fun downDao(): DownDao? {
        return downDao
    }

    fun downConfig(): DownConfig? {
        return downConfig
    }

    fun downDispatcher(): DownDispatcher? {
        return downDispatcher
    }

    fun downObserverable(): DownObservable? {
        return downObserverable
    }

    fun downTaskers(): ArrayList<DownTasker>? {
        return downTaskers
    }
}
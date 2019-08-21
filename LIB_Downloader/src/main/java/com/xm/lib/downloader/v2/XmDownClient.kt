package com.xm.lib.downloader.v2

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import com.xm.lib.downloader.v2.db.XmDownDao
import com.xm.lib.downloader.v2.imp.Call
import java.io.File

/**
 * 下载管理器
 */
class XmDownClient private constructor(private val builder: Builder) : Call.Factory {

    var dispatcher: XmDownDispatcher? = null
    var dao: XmDownDao? = null
    var ctx: Context? = null
    var dir: String = ""
    var runMaxQueuesNum: Int = 1
    var breakpoint: Boolean = true
    var overSameTask: Boolean = true

    init {
        this.dispatcher = builder.dispatcher
        this.dao = builder.dao
        this.ctx = builder.ctx
        this.dir = builder.dir
        this.runMaxQueuesNum = builder.runMaxQueuesNum
        this.breakpoint = builder.breakpoint
        this.overSameTask = builder.overSameTask
    }

    override fun newCall(request: XmDownRequest): Call {
        return XmRealCall.newXmRealDown(this, request)
    }

    class Builder {
        /**
         * 任务分发器
         */
        var dispatcher: XmDownDispatcher? = null

        /**
         * 数据库
         */
        var dao: XmDownDao? = null

        /**
         * 上下文对象
         */
        var ctx: Context? = null

        /**
         * 下载路径
         */
        var dir: String = ""

        /**
         * 下载最大任务数量
         */
        var runMaxQueuesNum: Int = 1

        /**
         * true 断点下载
         */
        var breakpoint: Boolean = true

        /**
         * true 覆盖相同任务。反之不覆盖，例如：下载一个任务task.xxx,再次下载时新建task(2).xxx。
         */
        var overSameTask: Boolean = true

        fun context(ctx: Context): Builder {
            this.ctx = ctx
            return this
        }

        fun dir(dir: String): Builder {
            this.dir = dir
            return this
        }

        fun runMaxQueuesNum(runMaxQueuesNum: Int): Builder {
            this.runMaxQueuesNum = runMaxQueuesNum
            return this
        }

        fun breakpoint(breakpoint: Boolean): Builder {
            this.breakpoint = breakpoint
            return this
        }

        fun overSameTask(overSameTask: Boolean): Builder {
            this.overSameTask = overSameTask
            return this
        }

        fun build(): XmDownClient {

            if (ctx == null) {
                throw NullPointerException("ctx is null")
            }

            if (TextUtils.isEmpty(dir)) {
                dir = Environment.getExternalStorageState() + File.separator
            }

            if (runMaxQueuesNum < 1) {
                runMaxQueuesNum = 1
            }

            //创建分发器
            dispatcher = XmDownDispatcher(runMaxQueuesNum)

            //创建数据库
            dao = XmDownDao(100, "xmDownloader", ctx)

            //创建
            return XmDownClient(this)
        }
    }
}
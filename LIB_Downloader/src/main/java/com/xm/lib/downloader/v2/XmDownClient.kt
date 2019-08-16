package com.xm.lib.downloader.v2

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import com.xm.lib.downloader.v2.imp.Call
import java.io.File

/**
 * 下载管理器
 */
class XmDownClient private constructor(val builder: Builder) : Call.Factory {

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
        var dao: Any? = null

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

            //创建
            return XmDownClient(this)
        }
    }
}
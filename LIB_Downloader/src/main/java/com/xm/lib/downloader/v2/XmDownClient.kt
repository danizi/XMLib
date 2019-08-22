package com.xm.lib.downloader.v2

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import com.xm.lib.downloader.v2.abs.AbsRequest
import com.xm.lib.downloader.v2.db.XmDownDao
import com.xm.lib.downloader.v2.imp.Call
import com.xm.lib.downloader.v2.imp.IXmDownDao
import com.xm.lib.downloader.v2.imp.IXmDownDispatcher
import java.io.File

/**
 * 下载管理器
 */
class XmDownClient private constructor(private val builder: Builder) : Call.Factory {

    var dispatcher: IXmDownDispatcher? = null
    var dao: IXmDownDao? = null
    var ctx: Context? = null
    var dir: String = ""
    var breakpoint: Boolean = true
    var overSameTask: Boolean = true

    init {
        this.dispatcher = builder.dispatcher
        this.dao = builder.dao
        this.ctx = builder.ctx
        this.dir = builder.dir
        this.breakpoint = builder.breakpoint
        this.overSameTask = builder.overSameTask
    }

    override fun newCall(request: AbsRequest): Call {
        return XmRealCall.newXmRealDown(this, request)
    }

    class Builder {
        /**
         * 任务分发器
         */
        var dispatcher: IXmDownDispatcher? = null

        /**
         * 数据库
         */
        var dao: IXmDownDao? = null

        /**
         * 上下文对象
         */
        var ctx: Context? = null

        /**
         * 下载路径
         */
        var dir: String = ""

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

        fun breakpoint(breakpoint: Boolean): Builder {
            this.breakpoint = breakpoint
            return this
        }

        fun overSameTask(overSameTask: Boolean): Builder {
            this.overSameTask = overSameTask
            return this
        }

        fun dao(dao: IXmDownDao): Builder {
            this.dao = dao
            return this
        }

        fun dispatcher(dispatcher: XmDownDispatcher) {
            this.dispatcher = dispatcher
        }

        fun build(): XmDownClient {

            if (ctx == null) {
                throw NullPointerException("ctx is null")
            }

            if (TextUtils.isEmpty(dir)) {
                dir = Environment.getExternalStorageState() + File.separator
            }

            //任务分发器
            if (dispatcher == null) {
                dispatcher = XmDownDispatcher(1)
            }

            //任务数据库
            if (dao == null) {
                dao = XmDownDao(100, "xmDownloader", ctx)
            }

            //创建
            return XmDownClient(this)
        }
    }
}
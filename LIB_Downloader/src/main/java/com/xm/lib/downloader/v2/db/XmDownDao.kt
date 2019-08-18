package com.xm.lib.downloader.v2.db

import android.content.Context

/**
 * 操作数据库
 */
class XmDownDao(private var version: Int, private var name: String, private var context: Context?) {

    private var daoHelp: XmDownDaoHelp? = null

    init {
        daoHelp = XmDownDaoHelp(context, name, null, version)
    }
}
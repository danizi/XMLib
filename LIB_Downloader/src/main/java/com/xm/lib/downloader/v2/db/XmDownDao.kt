package com.xm.lib.downloader.v2.db

import android.content.Context
import com.xm.lib.common.log.BKLog
import com.xm.lib.downloader.v2.imp.IXmDownDao

/**
 * 操作数据库
 */
class XmDownDao(private var version: Int, private var name: String, private var context: Context?) : IXmDownDao {

    private var daoHelp: XmDownDaoHelp? = null
    private var TAG = "XmDownDao"

    init {
        daoHelp = XmDownDaoHelp(context, name, null, version)
    }

    override fun insert(bean: XmDownDaoBean?) {
        daoHelp?.writableDatabase?.execSQL(XmDownDaoContract.SQL_INSERT, arrayOf(
                bean?.url,
                bean?.fileName,
                bean?.total,
                bean?.progress,
                bean?.state
        ))
    }

    override fun delete(url: String) {
        daoHelp?.writableDatabase?.execSQL(XmDownDaoContract.SQL_DELETE, arrayOf(url))
    }

    override fun update(url: String, progress: Int, state: String) {
        daoHelp?.writableDatabase?.execSQL(XmDownDaoContract.SQL_UPDATE, arrayOf(progress, state, url))
    }

    override fun selectAll(): List<XmDownDaoBean> {
        val downDaoBeans = ArrayList<XmDownDaoBean>()
        val cursor = daoHelp?.writableDatabase?.rawQuery(XmDownDaoContract.SQL_SELECT_ALL, arrayOf())

        if (cursor == null) {
            BKLog.e(TAG, "selectAll failure is null")
            return downDaoBeans
        }

        while (cursor.moveToNext()) {
            val downDaoBean = XmDownDaoBean()
            downDaoBean.url = cursor.getString(1)
            downDaoBean.fileName = cursor.getString(2)
            downDaoBean.total = cursor.getLong(3)
            downDaoBean.progress = cursor.getLong(4)
            downDaoBean.state = cursor.getString(5)
            downDaoBeans.add(downDaoBean)
        }

        return downDaoBeans
    }
}
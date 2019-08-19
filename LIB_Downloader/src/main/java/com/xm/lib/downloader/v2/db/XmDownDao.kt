package com.xm.lib.downloader.v2.db

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import com.xm.lib.common.log.BKLog
import com.xm.lib.downloader.v2.imp.IXmDownDao
import com.xm.lib.downloader.v2.state.XmDownError
import com.xm.lib.downloader.v2.state.XmDownState

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
        if (TextUtils.isEmpty(bean?.url)) {
            BKLog.d(TAG, "url is null")
            return
        }
        if (exist(bean?.url!!)) {
            daoHelp?.writableDatabase?.execSQL(XmDownDaoContract.SQL_INSERT, arrayOf(
                    bean?.url,
                    bean?.fileName,
                    bean?.total,
                    bean?.progress,
                    bean?.state
            ))
        }

    }

    override fun delete(url: String?) {
        daoHelp?.writableDatabase?.execSQL(XmDownDaoContract.SQL_DELETE, arrayOf(url))
    }

    override fun updateProgress(url: String?, progress: Int) {
        daoHelp?.writableDatabase?.execSQL(XmDownDaoContract.SQL_UPDATE, arrayOf(progress, XmDownState.RUNNING, url))
    }

    override fun updateComplete(url: String?) {
        daoHelp?.writableDatabase?.execSQL(XmDownDaoContract.SQL_UPDATE_STATE, arrayOf(XmDownState.COMPLETE, url))
    }

    override fun updateFailed(url: String?, error: XmDownError) {
        daoHelp?.writableDatabase?.execSQL(XmDownDaoContract.SQL_UPDATE_STATE, arrayOf(XmDownState.getError(error), url))
    }

    @SuppressLint("Recycle")
    override fun selectAll(): List<XmDownDaoBean> {
        val downDaoBeans = ArrayList<XmDownDaoBean>()
        val cursor = daoHelp?.writableDatabase?.rawQuery(XmDownDaoContract.SQL_SELECT_ALL, arrayOf())

        if (cursor == null) {
            BKLog.e(TAG, "selectAll failure, cursor is null")
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

    override fun exist(url: String): Boolean {
        val downDaoBeans = ArrayList<XmDownDaoBean>()
        val cursor = daoHelp?.writableDatabase?.rawQuery(XmDownDaoContract.SQL_SELECT_BY_URL, arrayOf(url))

        if (cursor == null) {
            BKLog.e(TAG, "exist failure, cursor is null")
            return false
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
        return if (downDaoBeans.size == 1) {
            true
        } else {
            BKLog.d(TAG, "downDaoBeans size : ${downDaoBeans.size}")
            false
        }
    }

}
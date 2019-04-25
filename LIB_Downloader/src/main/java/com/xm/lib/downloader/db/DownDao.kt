package com.xm.lib.downloader.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.TextUtils
import com.xm.lib.common.log.BKLog
import com.xm.lib.downloader.task.DownTask


class DownDao(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) {

    companion object {
        const val TAG = "DownDao"
    }

    private var helper: SQLiteOpenHelper? = null

    init {
        helper = DownDBHelp(context, name, factory, version)
        /* 数据库操作
         * helper.writableDatabase 可读可写数据库
         * helper.readableDatabase 只读数据库
         *
         * 数据库生成所在路径
         * dada/data/包名/databases/xxx.db
         */
    }

    fun insert(downInfo: DownTask) {
        /*插入数据*/
        //首先判断是否存在
        if (exists(downInfo.uuid)) {
            BKLog.d(TAG, "数据数据库已存在...，${downInfo.toString()}")
            return
        }
        //数据插入数据库
        helper?.writableDatabase?.execSQL(
                DownDBContract.DownTable.SQL_QUERY_INSERT,
                arrayOf(downInfo.uuid,
                        downInfo.url,
                        downInfo.name,
                        downInfo.present,
                        downInfo.total,
                        downInfo.progress,
                        downInfo.absolutePath,
                        downInfo.state))
        helper?.writableDatabase?.close()
    }

    fun update(downInfo: DownTask) {
        /*更新数据*/
        helper?.writableDatabase?.execSQL(
                DownDBContract.DownTable.SQL_UPDATE,
                arrayOf(downInfo.url,
                        downInfo.present,
                        downInfo.total,
                        downInfo.progress,
                        downInfo.state,
                        downInfo.uuid /*查询条件*/))
        helper?.writableDatabase?.close()
    }

    fun delete(downInfo: DownTask) {
        /*删除数据*/
        helper?.writableDatabase?.execSQL(DownDBContract.DownTable.SQL_DELETE, arrayOf(downInfo.uuid/*查询条件*/))
        helper?.writableDatabase?.close()
    }

    fun queryAll(): ArrayList<DownTask> {
        /*查询所有数据*/
        val downs = ArrayList<DownTask>()
        val cursor = helper?.readableDatabase?.rawQuery(DownDBContract.DownTable.SQL_QUERY_ALL, null)
                ?: return downs
        while (cursor.moveToNext()) {
            val down = DownTask()
            BKLog.d("id: ${cursor.getInt(0)} url:${cursor.getString(2)}")
            down.uuid = cursor.getString(1)
            down.url = cursor.getString(2)
            down.name = cursor.getString(3)
            down.present = cursor.getLong(4)
            down.total = cursor.getLong(5)
            down.progress = cursor.getLong(6)
            down.absolutePath = cursor.getString(7)
            down.state = cursor.getInt(8)
            downs.add(down)
        }
        helper?.writableDatabase?.close()
        return downs
    }

    fun query(uuid: String): ArrayList<DownTask> {
        /*查询数据通过uuid*/
        var downs = ArrayList<DownTask>()
        if (TextUtils.isEmpty(uuid)) return downs
        downs = ArrayList<DownTask>()
        val cursor = helper?.readableDatabase?.rawQuery(
                DownDBContract.DownTable.SQL_QUERY_UUID,
                arrayOf(uuid)) ?: return downs
        while (cursor.moveToNext()) {
            val down = DownTask()
            BKLog.d("id: ${cursor.getInt(0)} url:${cursor.getString(2)}")
            down.uuid = cursor.getString(1)
            down.url = cursor.getString(2)
            down.name = cursor.getString(3)
            down.present = cursor.getLong(4)
            down.total = cursor.getLong(5)
            down.progress = cursor.getLong(6)
            down.absolutePath = cursor.getString(7)
            down.state = cursor.getInt(8)
            downs.add(down)
        }
        helper?.writableDatabase?.close()
        return downs
    }

    private fun exists(uuid: String): Boolean {
        /*通过uuid列值，判断数据库是否存在*/
        val cursor = helper?.readableDatabase?.rawQuery(
                DownDBContract.DownTable.SQL_QUERY_UUID,
                arrayOf(uuid)) ?: return false
        if (cursor.moveToNext()) {
            helper?.readableDatabase?.close()
            return true
        }
        return false
    }
}
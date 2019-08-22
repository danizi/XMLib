package com.xm.lib.downloader.v2.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * 数据库帮助类
 */
class XmDownDaoHelp(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(XmDownDaoContract.SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}
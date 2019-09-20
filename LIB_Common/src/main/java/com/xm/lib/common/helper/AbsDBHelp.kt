package com.xm.lib.common.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * 数据库帮助类
 */
abstract class AbsDBHelp(context: Context?, name: String?, version: Int) : SQLiteOpenHelper(context, name, null, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        for (sql in getCreateTables()) {
            db?.execSQL(sql)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    /**
     * @return 创建表列表语句集合
     */
    abstract fun getCreateTables(): ArrayList<String>

}

/**
 * 数据库契约类
 */
class DBContract {

    /**
     * 表
     * 表名 + 字段
     * 主键、外键
     *
     *
     */
    abstract class AbsSQL {
        /**
         * 主键类型
         */
        val SQL_TYPE_KEY = "INTEGER PRIMARY KEY AUTOINCREMENT"
        /**
         * 字符串
         */
        val SQL_TYPE_TEXT = "TEXT NOT NULL"
        /**
         * 数字
         */
        val SQL_TYPE_INTEGER = "INTEGER NOT NULL DEFAULT 0"

        fun createTable(b: List<Any>): ArrayList<String> {
            val tables = ArrayList<String>()
            return tables
        }

        /**
         * 获取字段名称
         */
        private fun getFiledName(o: Any) {
            val fields = o.javaClass.declaredFields
            for (field in fields) {
                System.out.println(field.name)
            }
        }

        /**
         * 获取信息
         */
        private fun getFiledsInfo() {}

        /**
         * 通过字段获取值
         */
        private fun getFieldValueByName() {}

    }

    /**
     * 数据库 CRUD 操作类
     *
     */
    abstract class AbsDao<B> {
        /**
         * 判断数据库中是否存在
         */
        abstract fun exist(bean: B): Boolean

        /**
         * 插入数据
         */
        abstract fun insert(bean: B): Boolean

        /**
         * 删除指定记录
         */
        abstract fun delete(bean: B): Boolean

        /**
         * 修改指定记录
         */
        abstract fun update(bean: B): Boolean

        /**
         * 查询指定内容
         */
        abstract fun select(bean: B): ArrayList<B>

        /**
         * 删除所有记录
         */
        abstract fun deleteAll(): Boolean

        /**
         * 查询所有记录
         */
        abstract fun selectALL(): ArrayList<B>
    }
}
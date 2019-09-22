package com.xm.lib.common.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.xm.lib.common.util.ReflectUtil
import com.xm.lib.common.util.ReflectUtil.getFiledsInfo

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
     */
    abstract class AbsSQL {
        /**
         * 创建表
         */
        private val SQL_CREATE_TABLE = "CREATE TABLE"
        /**
         * 插入数据
         */
        private val SQL_INSERT_INTO = "INSERT INTO"
        /**
         * 删除数据
         */
        private val SQL_DELETE_FROM = "DELETE FROM"
        /**
         * 修改数据
         */
        private val SQL_UPDATE = "UPDATE @ SET"
        /**
         * 查询数据
         */
        private val SQL_SELECT = "SELECT * FROM"

        /**
         * 主键类型
         */
        private val SQL_TYPE_KEY = "INTEGER PRIMARY KEY AUTOINCREMENT"
        /**
         * 字符串
         */
        private val SQL_TYPE_TEXT = "TEXT NOT NULL"
        /**
         * 数字
         */
        private val SQL_TYPE_INTEGER = "INTEGER NOT NULL DEFAULT 0"

        /**
         * 创建数据库表语句
         */
        fun createSQLTable(b: Any, tableName: String): String {
            val infos = getFiledsInfo(b)
            val sql = StringBuilder()
            sql.append("$SQL_CREATE_TABLE $tableName(")
            sql.append("id $SQL_TYPE_KEY,")

            for (i in 0 until infos.size) {
                var type = ""
                val info = infos[i]

                //根据属性数据类型，设置不同SQL类型。
                if (info.genericType.contains("String")) {
                    type = SQL_TYPE_TEXT
                } else if (info.genericType.contains("double") || info.genericType.contains("int")) {
                    type = SQL_TYPE_INTEGER
                }

                //位置到最后一个字段的时候不加“，”
                if (i == infos.size - 1) {
                    sql.append("${info.name} $type")
                } else {
                    sql.append("${info.name} $type,")
                }
            }
            sql.append(");")
            return sql.toString()
        }

        /**
         * 插入数据语句
         */
        fun createSQLInsert(b: Any, tableName: String): String {
            val infos = getFiledsInfo(b)
            val sql = StringBuilder()
            val values = StringBuilder()
            values.append("VALUES(")
            sql.append("$SQL_INSERT_INTO $tableName(")

            for (i in 0 until infos.size) {
                val info = infos[i]

                //位置到最后一个字段的时候不加“，”
                if (i == infos.size - 1) {
                    values.append("?")
                    sql.append("${info.name}")
                } else {
                    values.append("?,")
                    sql.append("${info.name},")
                }
            }
            values.append(");")
            sql.append(") ${values.toString()}")
            return sql.toString()
        }

        /**
         * 删除数据语句
         * @param condition 查询条件
         */
        fun createSQLDelete(b: Any, tableName: String, vararg selectionArgs: String?): String {
            if (selectionArgs.isEmpty()) {
                return "$SQL_DELETE_FROM $tableName;"
            }

            for (c in selectionArgs) {

            }
            return ""
        }

        /**
         * 修改数据语句
         */
        fun createSQLUpdate() {

        }

        /**
         * 创建查询条件
         */
        fun createSQLQuery(vararg condition: String) {

        }
    }

    /**
     * 数据库 CRUD 操作类
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

class SQL : DBContract.AbsSQL()

fun main(args: Array<String>) {
    System.out.println(SQL().createSQLTable(ReflectUtil.Bean(), "bean"))
    System.out.println(SQL().createSQLInsert(ReflectUtil.Bean(), "bean"))
    System.out.println(getFiledsInfo(ReflectUtil.Bean()).toString())
}
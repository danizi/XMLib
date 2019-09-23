package com.xm.lib.common.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ReflectUtil.getFiledsInfo

/**
 * 数据库帮助类
 * @param context 上下文对象
 * @param name 数据库名称
 * @param version 数据库版本号
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
    abstract class AbsSqlStatementCreation {
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

        private fun check(b: Any?, tableName: String?) {
            //println("tableName:$tableName daoBean:${daoBean.toString()}")
            if (tableName == "") {
                throw IllegalAccessException("tableName is null")
            }
            if (b == null) {
                throw IllegalAccessException("daoBean is null")
            }
        }

        /**
         * 创建数据库表语句
         */
        open fun createSQLTable(b: Any, tableName: String): String? {
            check(b, tableName)
            val infos = getFiledsInfo(b)
            val sql = StringBuilder()
            sql.append("$SQL_CREATE_TABLE $tableName(")
            sql.append("id $SQL_TYPE_KEY,")

            for (i in 0 until infos.size) {
                var type = ""
                val info = infos[i]

                //根据属性数据类型，设置不同SQL类型。
                type = if (info.genericType.contains("String")) {
                    SQL_TYPE_TEXT
                } else if (info.genericType.contains("double") || info.genericType.contains("int")) {
                    SQL_TYPE_INTEGER
                } else {
                    SQL_TYPE_TEXT
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
        open fun createSQLInsert(b: Any, tableName: String): String {
            check(b, tableName)
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
                    sql.append(info.name)
                } else {
                    values.append("?,")
                    sql.append("${info.name},")
                }
            }
            values.append(");")
            sql.append(") ${values.toString()}")
            BKLog.e("InsertSQL:"+sql.toString())
            return sql.toString()
        }

        /**
         * 删除数据语句 todo 查询条件只能and
         */
        open fun createSQLDelete(b: Any?, tableName: String, vararg selectionArgs: String?): String {
            check(b, tableName)
            if (selectionArgs.isEmpty()) {
                return "$SQL_DELETE_FROM $tableName;"
            }

            val infos = getFiledsInfo(b!!)
            val sql = StringBuilder()
            sql.append("$SQL_DELETE_FROM $tableName WHERE ")
            for (i in 0 until selectionArgs.size) {
                val selectionArg = selectionArgs[i]
                if (i == 0) {
                    sql.append("$selectionArg")
                    continue
                }
                if (i < selectionArgs.size) {
                    sql.append(" AND $selectionArg ")
                } else {
                    sql.append("$selectionArg")
                }
            }
            sql.append(";")
            return sql.toString()
        }

        /**
         * 修改数据语句 todo 查询条件只能and
         */
        open fun createSQLUpdate(b: Any, tableName: String, vararg selectionArgs: String?): String {
            check(b, tableName)
            if (selectionArgs.isEmpty()) {
                throw IllegalAccessException("selectionArgs is null")
            }
            val infos = getFiledsInfo(b)

            //添加表名称
            val sql = StringBuilder(SQL_UPDATE)
            sql.replace(6, 8, " $tableName")

            //需要修改的值
            for (i in 0 until infos.size) {
                val info = infos[i]
                if (i == 0) {
                    sql.append(" ${info.name}=?,")
                    continue
                }
                //位置到最后一个字段的时候不加“，”
                if (i == infos.size - 1) {
                    sql.append("${info.name}=?")
                } else {
                    sql.append("${info.name}=?,")
                }
            }
            //判断条件
            sql.append(" WHERE ")
            for (i in 0 until selectionArgs.size) {
                val arg = selectionArgs[i]
                if (i == selectionArgs.size - 1) {
                    sql.append("$arg")
                } else {
                    sql.append("$arg AND ")
                }
            }
            sql.append(";")
            return sql.toString()
        }

        /**
         * 创建查询条件 todo 查询条件只能and
         */
        open fun createSQLQuery(b: Any?, tableName: String, vararg selectionArgs: String?): String {
            check(b, tableName)
            if (selectionArgs.isEmpty()) {
                return "$SQL_SELECT $tableName"
            }
            val infos = getFiledsInfo(b!!)
            val sql = StringBuilder()
            sql.append("$SQL_SELECT $tableName")
            sql.append(" WHERE ")
            for (i in 0 until selectionArgs.size) {
                val arg = selectionArgs[i]
                if (i == selectionArgs.size - 1) {
                    sql.append("$arg")
                } else {
                    sql.append("$arg AND ")
                }
            }
            sql.append(";")
            return sql.toString()
        }
    }

    /**
     * 数据库 CRUD 操作类
     * @param writableDatabase 数据库打开设置
     * @param sqlStatementCreation sql语句创建类
     */
    abstract class AbsDao<B>() {

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

        /**
         * 销毁资源
         */
        abstract fun clear()
    }
}




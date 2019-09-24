package com.xm.lib.common.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ReflectUtil
import com.xm.lib.common.util.ReflectUtil.getFiledInfo

/**
 * 数据库帮助类
 * @param context 上下文对象
 * @param name 数据库名称
 * @param version 数据库版本号
 */
abstract class AbsDBHelp(context: Context?, name: String?, version: Int) : SQLiteOpenHelper(context, name, null, version) {

    /**
     * SQL 语句创建模板通过表绑定的实例对象创建
     */
    private var sqlStatementCreation: DBContract.AbsSqlStatementCreation? = null

    fun getLockSqlStatementCreation(): DBContract.AbsSqlStatementCreation? {
        if (sqlStatementCreation == null) {
            sqlStatementCreation = object : DBContract.AbsSqlStatementCreation() {}
            return sqlStatementCreation!!
        }
        return sqlStatementCreation!!
    }

    /**
     * 数据库创建，创建了之后就不会再调用了。
     */
    override fun onCreate(db: SQLiteDatabase?) {
        for (ent in getCreateTables().entries) {
            val sql = getLockSqlStatementCreation()?.createSQLTable(ent.value, ent.key)!!
            db?.execSQL(sql)
        }
    }

    /**
     * 数据库升级
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    /**
     * @return 创建表所需参数 key表名称 value建立表实例对象
     */
    abstract fun getCreateTables(): HashMap<String, Any>

}

/**
 * 数据库契约类
 */
class DBContract {

    /**
     * 创建sql语句模板操作类
     */
    abstract class AbsSqlStatementCreation {
        companion object {
            private const val SQL_CREATE_TABLE = "CREATE TABLE" // 创建表
            private const val SQL_INSERT_INTO = "INSERT INTO"   // 插入数据
            private const val SQL_DELETE_FROM = "DELETE FROM"   // 删除数据
            private const val SQL_UPDATE = "UPDATE @ SET"       // 修改数据
            private const val SQL_SELECT = "SELECT * FROM"      // 查询数据

            private const val SQL_TYPE_KEY = "INTEGER PRIMARY KEY AUTOINCREMENT" // 主键类型
            private const val SQL_TYPE_TEXT = "TEXT NOT NULL"                    // 字符串
            private const val SQL_TYPE_INTEGER = "INTEGER NOT NULL DEFAULT 0"    // 数字
        }

        open fun createSQLTable(b: Any, tableName: String): String? {
            check(b, tableName)
            val infos = getFiledInfo(b)
            val sql = StringBuilder()
            sql.append("$SQL_CREATE_TABLE $tableName(id $SQL_TYPE_KEY,")
            for (i in 0 until infos.size) {
                var type: String
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
            if (sql.contains("check")) {
                throw IllegalAccessException("check 字段是不能使用的，不然执行sql会报错。")
            }
            return sql.toString()
        }

        open fun createSQLInsert(b: Any, tableName: String): String {
            check(b, tableName)
            val infos = getFiledInfo(b)
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
            BKLog.e("InsertSQL:" + sql.toString())
            return sql.toString()
        }

        open fun createSQLDelete(b: Any?, tableName: String, vararg selectionArgs: String?): String {
            // 查询条件只能and
            check(b, tableName)
            //如果过滤条件为控件就返回删除所有的sql语句
            if (selectionArgs.isEmpty()) {
                return "$SQL_DELETE_FROM $tableName;"
            }

            //val infos = getFiledInfo(b!!)
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

        open fun createSQLUpdate(b: Any, tableName: String, vararg selectionArgs: String?): String {
            // 查询条件只能and
            check(b, tableName)
            if (selectionArgs.isEmpty()) {
                throw IllegalAccessException("selectionArgs is null")
            }
            val infos = getFiledInfo(b)

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
                    //dddd
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

        open fun createSQLQuery(b: Any?, tableName: String, vararg selectionArgs: String?): String {
            // 查询条件只能and
            check(b, tableName)
            if (selectionArgs.isEmpty()) {
                return "$SQL_SELECT $tableName"
            }
            //val infos = getFiledInfo(b!!)
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

        private fun check(b: Any?, tableName: String?) {
            //println("tableName:$tableName daoBean:${daoBean.toString()}")
            if (tableName == "") {
                throw IllegalAccessException("tableName is null")
            }
            if (b == null) {
                throw IllegalAccessException("daoBean is null")
            }
        }
    }

    /**
     * 数据库 CRUD 操作类
     * @param tableName 数据库类
     * @param dbHelp 数据库帮助类
     */
    abstract class AbsDao<B : Any>(private val tableName: String?, private val dbHelp: AbsDBHelp?) {
        companion object {
            private const val TAG = "AbsDao"
        }

        var writableDatabase: SQLiteDatabase? = null
        var statementCreation: AbsSqlStatementCreation? = null

        init {
            writableDatabase = dbHelp?.writableDatabase
            statementCreation = dbHelp?.getLockSqlStatementCreation()
        }

        abstract fun getDeleteSelection(): String

        abstract fun getUpdateSelection(): String

        abstract fun getSelectSelection(): String

        abstract fun getDeleteSelectionValaue(bean: B?): Array<Any?>

        abstract fun getSelectSelectionValue(bean: B?): Array<String?>

        abstract fun newInstance(): B

        open fun exist(bean: B): Boolean {
            if (select(bean).isNotEmpty() && select(bean).size > 0) {
                return true
            }
            return false
        }

        open fun insert(bean: B): Boolean {
            try {
                writableDatabase = dbHelp?.writableDatabase
                check(bean)
                if (exist(bean)) {
                    BKLog.e(TAG, "该记录存在 ${bean.toString()}")
                    return false
                }
                val insertSql = statementCreation?.createSQLInsert(bean, tableName!!)
                val infos = ReflectUtil.getFiledInfo(bean)
                val valusArray = arrayOfNulls<Any>(infos.size)
                for (i in 0 until infos.size) {
                    val info = infos[i]
                    valusArray[i] = info.value
                }
                writableDatabase?.execSQL(insertSql, valusArray)
                BKLog.d(TAG, "插入数据")
                BKLog.d(TAG, "insertSql : $insertSql")
                BKLog.d(TAG, "valusArray : ${valusArray.toString()}")
                return true
            } catch (e: Exception) {
                e.printStackTrace()
                BKLog.e(TAG, "插入数据失败 : ${e.message}")
            } finally {
                writableDatabase?.close()
            }
            return false
        }

        open fun delete(bean: B): Boolean {
            writableDatabase = dbHelp?.writableDatabase
            check(bean)
            return try {
                val deleteSql = statementCreation?.createSQLDelete(bean, tableName!!, getDeleteSelection())
                writableDatabase?.execSQL(deleteSql, getDeleteSelectionValaue(bean))
                BKLog.d(TAG, "删除数据")
                BKLog.d(TAG, "deleteSql : $deleteSql")
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            } finally {
                writableDatabase?.close()
            }
        }

        open fun update(bean: B): Boolean {
            try {
                writableDatabase = dbHelp?.writableDatabase
                check(bean)
                val infos = ReflectUtil.getFiledInfo(bean)
                val bindArgs = arrayOfNulls<Any>(infos.size + 1)
                var selectionValue: Any? = null
                for (i in 0 until infos.size) {
                    bindArgs[i] = infos[i].value
                    if (getUpdateSelection().contains(infos[i].name)) {
                        selectionValue = infos[i].value
                    }
                }
                bindArgs[infos.size] = selectionValue

                val updateSql = statementCreation?.createSQLUpdate(bean, tableName!!, getUpdateSelection())
                writableDatabase?.execSQL(updateSql, arrayOf(bindArgs))
                BKLog.d(TAG, "更新数据")
                BKLog.d(TAG, "updateSql : $updateSql")
                BKLog.d(TAG, "bindArgs : ${bindArgs.toString()}")
                return true
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            } finally {
                writableDatabase?.close()
            }
        }

        open fun select(bean: B): ArrayList<B> {
            return select(bean, getSelectSelection())
        }

        open fun deleteAll(): Boolean {
            return try {
                writableDatabase = dbHelp?.writableDatabase
                check()
                val deleteAllSql = statementCreation?.createSQLDelete(Any(), tableName!!, "")
                writableDatabase?.execSQL(deleteAllSql)
                BKLog.d(TAG, "删所有数据除数据")
                BKLog.d(TAG, "deleteSql : $deleteAllSql")
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            } finally {
                writableDatabase?.close()
            }

        }

        open fun selectALL(): ArrayList<B> {
            return select(null, "")
        }

        open fun clear() {
            if (writableDatabase?.isOpen == true) {
                writableDatabase?.close()
            }
            writableDatabase = null
            statementCreation = null
        }

        private fun select(bean: B?, selectionArg: String?): ArrayList<B> {
            writableDatabase = dbHelp?.writableDatabase
            check()
            val objList = ArrayList<B>()
            val selectSql = statementCreation?.createSQLQuery(bean, tableName!!, selectionArg)
            val selectSelectionValue = getSelectSelectionValue(bean)
            val cursor = writableDatabase?.rawQuery(selectSql, selectSelectionValue)

            if (cursor == null) {
                writableDatabase?.close()
                return objList
            }
            try {
                while (cursor.moveToNext()) {
                    val obj = newInstance()
                    val infos = ReflectUtil.getFiledInfo(obj)
                    for (i in 0 until infos.size) {
                        val info = infos[i]
                        if (info.genericType.contains("String")) {
                            ReflectUtil.setFieldValueByName(obj, info.name, cursor.getString(i + 1))
                        } else if (info.genericType.contains("int") || info.genericType.contains("double")) {
                            ReflectUtil.setFieldValueByName(obj, info.name, cursor.getInt(i + 1))
                        }
                    }
                    objList.add(obj)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                cursor.close()
            } finally {
                if (cursor.isClosed) {
                    cursor.close()
                }
                writableDatabase?.close()
            }
            BKLog.d(TAG, "=====================")
            BKLog.d(TAG, "selectSql            : $selectSql")
            BKLog.d(TAG, "selectSelectionValue : ${selectSelectionValue[0]}")
            BKLog.d(TAG, "selectItemListSize   : ${objList.size}")
            BKLog.d(TAG, "selectItemList       : ${objList.toString()}")
            BKLog.d(TAG, "=====================")
            return objList
        }

        private fun check(bean: B?) {
            if (bean == null) {
                throw IllegalAccessException("bean is null")
            }
            check()
        }

        private fun check() {
            if (tableName == "") {
                throw IllegalAccessException("tableName is null")
            }
            if (writableDatabase?.isOpen != true) {
                throw IllegalAccessException("writableDatabase Did not open")
            }
        }
    }
}




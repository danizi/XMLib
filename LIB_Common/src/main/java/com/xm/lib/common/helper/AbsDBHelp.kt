package com.xm.lib.common.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.TextUtils
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
                    "TEXT"
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
            //如果过滤条件为控件就返回删除所有的sql语句
            if (selectionArgs.isNotEmpty() && TextUtils.isEmpty(selectionArgs[0])) {
                return "$SQL_DELETE_FROM $tableName;"
            }

            // 查询条件只能and
            check(b, tableName)

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
            if (selectionArgs.isNotEmpty() && TextUtils.isEmpty(selectionArgs[0])) {
                return "$SQL_SELECT $tableName"
            }
            check(b, tableName)
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
            private const val DEBUG = false
        }

        private var writableDatabase: SQLiteDatabase? = null
        private var statementCreation: AbsSqlStatementCreation? = null

        init {
            writableDatabase = dbHelp?.writableDatabase
            statementCreation = dbHelp?.getLockSqlStatementCreation()
        }


        abstract fun getDeleteSelection(): String //删除条件
        abstract fun getUpdateSelection(): String //更新条件
        abstract fun getSelectSelection(): String //查找条件

        abstract fun getDeleteBindValue(bean: B?): Array<Any?> //删除条件所绑定的值
        abstract fun getSelectBindValue(bean: B?): Array<String?> //查找条件所绑定的值
        abstract fun newInstance(): B  //实例化数据绑定实体类

        open fun exist(bean: B): Boolean {
            val items = select(bean)
            if (items.isNotEmpty() && items.size > 0) {
                return true
            }
            return false
        }

        open fun insert(bean: B): Boolean {
            try {
//                if(!writableDatabase?.isOpen!!){
//                    writableDatabase = dbHelp?.writableDatabase
//                }
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
                //writableDatabase?.close()
            }
            return false
        }

        open fun delete(bean: B): Boolean {
            return delete(bean, getDeleteSelection(), getDeleteBindValue(bean))
        }

        open fun update(bean: B): Boolean {
            try {
                //writableDatabase = dbHelp?.writableDatabase
                check(bean)
                if (!exist(bean)) {
                    BKLog.e(TAG, "更新数据失败,数据库中没有${bean.toString()}该记录")
                    return false
                }
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
                writableDatabase?.execSQL(updateSql, bindArgs)
                BKLog.d(TAG, "更新数据成功")
                if (DEBUG) {
                    BKLog.d(TAG, "updateSql : $updateSql")
                    BKLog.d(TAG, "bindArgs : ${bindArgs.asList().toString()}")
                }
                return true
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            } finally {
                //writableDatabase?.close()
            }
        }

        open fun select(bean: B): ArrayList<B> {
            return select(bean, getSelectSelection(), getSelectBindValue(bean))
        }

        open fun deleteAll(): Boolean {
            return delete(null, "", arrayOf())
        }

        open fun selectALL(): ArrayList<B> {
            return select(null, "", arrayOf())
        }

        open fun clear() {
            if (writableDatabase?.isOpen == true) {
                writableDatabase?.close()
            }
            writableDatabase = null
            statementCreation = null
        }

        /**
         * 特定条件删除 bean selectionArg deleteBindValue都不为null
         * 删除全部 bean selectionArg deleteBindValue 为null
         * @param bean 实体bean
         * @param selectionArg 过滤条件
         * @param deleteBindValue 绑定条件值
         */
        private fun delete(bean: B?, selectionArg: String?, deleteBindValue: Array<Any?>): Boolean {
            //writableDatabase = dbHelp?.writableDatabase
            check()
            try {
                val deleteSql = statementCreation?.createSQLDelete(bean, tableName!!, selectionArg)
                if (bean == null && TextUtils.isEmpty(selectionArg) && deleteBindValue.isEmpty()) {
                    BKLog.d("删除所有数据")
                    writableDatabase?.execSQL(deleteSql, deleteBindValue)
                    return true
                } else {
                    if (exist(bean!!)) {
                        writableDatabase?.execSQL(deleteSql, deleteBindValue)
                        BKLog.d(TAG, "删除特定条件数据")
                        BKLog.d(TAG, "deleteSql : $deleteSql")
                        return true
                    } else {
                        BKLog.e(TAG, "数据库中不存在${bean.toString()}")
                        return false
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            } finally {
                //writableDatabase?.close()
            }
        }

        /**
         * 特定条件查找 bean selectionArg selectSelectionValue都不为null
         * 查找全部 bean selectionArg selectSelectionValue 为null
         * @param bean 实体bean
         * @param selectionArg 过滤条件
         * @param selectSelectionValue 绑定条件值
         */
        private fun select(bean: B?, selectionArg: String?, selectSelectionValue: Array<String?>): ArrayList<B> {
            //writableDatabase = dbHelp?.writableDatabase
            check()
            val objList = ArrayList<B>()
            val selectSql = statementCreation?.createSQLQuery(bean, tableName!!, selectionArg)
            val cursor = writableDatabase?.rawQuery(selectSql, selectSelectionValue)
                    ?: //writableDatabase?.close()
                    return objList
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
                // writableDatabase?.close()
            }
            if (DEBUG) {
                BKLog.d(TAG, "=====================")
                BKLog.d(TAG, "selectSql            : $selectSql")
                if (selectSelectionValue.isNotEmpty()) {
                    BKLog.d(TAG, "selectSelectionValue : ${selectSelectionValue[0]}")
                }
                BKLog.d(TAG, "selectItemListSize   : ${objList.size}")
                BKLog.d(TAG, "selectItemList       : ${objList.toString()}")
                BKLog.d(TAG, "=====================")
            }
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




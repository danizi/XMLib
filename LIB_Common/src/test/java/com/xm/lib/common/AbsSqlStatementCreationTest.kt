package com.xm.lib.common

import com.xm.lib.common.bean.AppInfoBean
import com.xm.lib.common.helper.DBContract
import com.xm.lib.common.util.ReflectUtil.getFiledsInfo
import org.junit.Test

/**
 * 生成sql语句测试
 */
class AbsSqlStatementCreationTest {

    class Bean {
        var field1: String = ""
        var field2: Double = 1.0
        var field3: Int = 1
        var field4: ArrayList<String> = ArrayList()
    }

    class SQL : DBContract.AbsSqlStatementCreation()

    @Test
    fun sql() {
        val sql = SQL()
        val tableName = "bean"
        val bean = AppInfoBean()
        System.out.println("实体信息:" + getFiledsInfo(Bean()).toString())
        System.out.println("创建表  : " + sql.createSQLTable(bean, tableName))
        System.out.println("增      : " + sql.createSQLInsert(bean, tableName))
        System.out.println("删      : " + sql.createSQLDelete(bean, tableName, "column=?"))
        System.out.println("改      : " + sql.createSQLUpdate(bean, tableName, "column=?"))
        System.out.println("查      : " + sql.createSQLQuery(bean, tableName, "column=?"))
    }
}
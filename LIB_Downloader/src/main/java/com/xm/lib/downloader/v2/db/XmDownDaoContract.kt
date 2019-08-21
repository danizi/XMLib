package com.xm.lib.downloader.v2.db

/**
 * 数据库契约类
 */
object XmDownDaoContract {

    private const val tableName = "xmDownloader"
    private const val column_id = "id"
    private const val column_url = "url"
    private const val column_fileName = "fileName"
    private const val column_total = "total"
    private const val column_progress = "progress"
    private const val column_state = "state"

    /**
     * 创建表
     */
    const val SQL_CREATE_TABLE = "create table $tableName( " +
            "$column_id integer primary key autoincrement," +
            "$column_url varchar(255) not null," +
            "$column_fileName varchar(255) not null," +
            "$column_total integer default 0," +
            "$column_progress integer default 0, " +
            "$column_state varchar(255) not null" +
            ");"

    /**
     * 插入数据
     */
    const val SQL_INSERT = "insert into $tableName (" +
            "$column_url," +
            "$column_fileName," +
            "$column_total," +
            "$column_progress," +
            "$column_state)values (?,?,?,?,?);"

    /**
     * 删除所有数据
     */
    const val SQL_DELETE = "delete * from $tableName where $column_url=?;"
    const val SQL_DELETE_ALL = "delete from $tableName;"

    /**
     * 更新数据
     */
    const val SQL_UPDATE_PROGRESS = "update $tableName set $column_progress=?,$column_state=? where $column_url=?;"
    const val SQL_UPDATE_TOTAL = "update $tableName set $column_total=?,$column_state=? where $column_url=?;"
    const val SQL_UPDATE_STATE = "update $tableName set $column_state=? where $column_url=?;"

    /**
     * 提取所有数据
     */
    const val SQL_SELECT_ALL = "select * from $tableName;"
    const val SQL_SELECT_BY_URL = "select * from $tableName where $column_url=?;"

}
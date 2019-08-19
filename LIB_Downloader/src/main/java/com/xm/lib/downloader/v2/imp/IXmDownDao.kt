package com.xm.lib.downloader.v2.imp

import com.xm.lib.downloader.v2.db.XmDownDaoBean

/**
 * 数据库操作接口
 */
interface IXmDownDao {

    /**
     * 插入数据
     * @param bean 插入数据实体
     */
    fun insert(bean: XmDownDaoBean?)

    /**
     * 删除数据
     * @param url 删除条件
     */
    fun delete(url: String)

    /**
     * 更新数据
     * @param url 删除条件
     * @param progress 进度 单位(B)
     * @param state 状态
     */
    fun update(url: String, progress: Int, state: String)

    /**
     * 查询所有数据
     */
    fun selectAll(): List<XmDownDaoBean>
}
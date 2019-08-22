package com.xm.lib.downloader.v2.imp

import com.xm.lib.downloader.v2.db.XmDownDaoBean
import com.xm.lib.downloader.v2.state.XmDownError

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
     * @param url 查询条件
     */
    fun delete(url: String?)

    fun deleteAll()

    /**
     * 更新进度数据
     * @param url 查询条件
     * @param progress 进度 单位(B)
     */
    fun updateProgress(url: String?, progress: Long, total: Long)

    /**
     * 更新下载完成数据
     * @param url
     */
    fun updateComplete(url: String?)

    /**
     * 更新下载失败数据
     * @param url 查询条件
     * @param error 错误类型
     */
    fun updateFailed(url: String?, error: XmDownError)

    /**
     * 统一更新状态
     * @param state 当前任务状态
     */
    fun updateAllState(state: String)

    /**
     * 查询所有数据
     */
    fun selectAll(): List<XmDownDaoBean>

    /**
     * 查询记录-可能多条
     * @param url 查询条件
     */
    fun select(url: String): List<XmDownDaoBean>

    /**
     * 查询记录-单条
     */
    fun selectSingle(url: String): XmDownDaoBean

    /**
     * 是否存在
     */
    fun exist(url: String): Boolean
}
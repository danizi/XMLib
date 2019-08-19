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
     * @param url 删除条件
     */
    fun delete(url: String?)

    /**
     * 更新进度数据
     * @param url 删除条件
     * @param progress 进度 单位(B)
     */
    fun updateProgress(url: String?, progress: Int)

    /**
     * 更新下载完成数据
     * @param url
     */
    fun updateComplete(url: String?)

    /**
     * 更新下载失败数据
     * @param url
     */
    fun updateFailed(url: String?, error: XmDownError)

    /**
     * 查询所有数据
     */
    fun selectAll(): List<XmDownDaoBean>

    /**
     * 是否存在
     */
    fun exist(url: String): Boolean
}
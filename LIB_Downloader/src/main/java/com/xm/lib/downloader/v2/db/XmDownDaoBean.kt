package com.xm.lib.downloader.v2.db

import com.xm.lib.downloader.v2.XmDownRequest
import com.xm.lib.downloader.v2.state.XmDownState

/**
 * 数据库操作实体bean
 */
class XmDownDaoBean {

    var id = 0
    var url = ""
    var fileName = ""
    var total = 0L
    var progress = 0L
    var state = XmDownState.NOT_STARTED

    companion object {
        fun newXmDownDaoBean(state: String, request: XmDownRequest): XmDownDaoBean {
            val daoBean = XmDownDaoBean()
            daoBean.url = request.url!!
            daoBean.fileName = request.fileName!!
            return daoBean
        }
    }
}

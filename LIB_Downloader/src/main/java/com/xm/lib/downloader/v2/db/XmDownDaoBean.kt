package com.xm.lib.downloader.v2.db

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
    var path = ""

    /**
     * 扩展的字段
     */
    var isEdit = false
    var isSelect = false

}

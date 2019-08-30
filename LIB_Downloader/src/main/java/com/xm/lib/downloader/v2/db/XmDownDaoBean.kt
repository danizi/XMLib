package com.xm.lib.downloader.v2.db

import com.xm.lib.downloader.v2.state.XmDownError
import com.xm.lib.downloader.v2.state.XmDownState

/**
 * 数据库操作实体bean
 */
class XmDownDaoBean {
    var id = 0
    var url = ""
    var fileName = ""
    var path = ""
    var fileLength = 0L
    var total = 0L
    var progress = 0L
    var state = XmDownState.NOT_STARTED


    /**************************/
    /**以下字段不会存入数据库**/
    /**************************/
    //扩展的字段,在编辑是需要的
    var isEdit = false
    var isSelect = false

    //下载错误提示
    var error = XmDownError.UNKNOWN
}

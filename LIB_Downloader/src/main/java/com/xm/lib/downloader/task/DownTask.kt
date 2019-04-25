package com.xm.lib.downloader.task

import com.xm.lib.downloader.config.DownConfig.Companion.DEFAULT
import com.xm.lib.downloader.config.DownConfig.Companion.EMPTY_STRING
import com.xm.lib.downloader.enum_.DownStateType


class DownTask {

    //请求下载字段
    var name = EMPTY_STRING
    var url = EMPTY_STRING
    var uuid = EMPTY_STRING
    var fileName = EMPTY_STRING
    var state = DownStateType.NOT_STARTED.ordinal

    //存储到数据库所用字段
    var progress = DEFAULT
    var total = DEFAULT
    var present = DEFAULT
    var path = EMPTY_STRING
    var dir = EMPTY_STRING
    var absolutePath = EMPTY_STRING

    //编辑相关字段
//    var isEdit = false
    var isSelect = false
}
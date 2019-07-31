package com.xm.lib.component.tip.dlg.core

/**
 * 对话框创建类型
 */
enum class CreateDialogType {
    /**
     * 原生AlertDialog
     */
    NATIVE_ALERT_DIALOG,
    /**
     * 原生ProgressDialog
     */
    NATIVE_PROGRESS_DIALOG,
    /**
     * 自定义IOS风格对话框
     */
    CUSTOM_IOS_DIALOG,
    /**
     * 自定义IOS进度框
     */
    CUSTOM_IOS_PROGRESS_DIALOG
}
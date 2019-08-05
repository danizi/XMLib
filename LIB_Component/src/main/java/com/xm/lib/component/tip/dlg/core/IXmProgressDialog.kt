package com.xm.lib.component.tip.dlg.core

/**
 * 等待对话框
 */
interface IXmProgressDialog : XmDialogInterface {

    fun setTitle(title: String): IXmProgressDialog

    fun setMessage(msg: String): IXmProgressDialog

    fun setIndeterminate(indeterminate: Boolean): IXmProgressDialog

    fun setCancelable(flag: Boolean): IXmProgressDialog

    fun setProgress(value: Int): IXmProgressDialog

    fun setProgressStyle(): IXmProgressDialog

    fun setMax(max: Int): IXmProgressDialog

    fun show(): IXmProgressDialog

}
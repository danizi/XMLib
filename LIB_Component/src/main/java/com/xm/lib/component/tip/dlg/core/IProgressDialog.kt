package com.xm.lib.component.tip.dlg.core

/**
 * 等待对话框
 */
interface IProgressDialog {
    fun setTitle(title: String): IProgressDialog
    fun setMessage(msg:String):IProgressDialog
    fun setIndeterminate(indeterminate: Boolean): IProgressDialog
    fun setCancelable(flag: Boolean): IProgressDialog
    fun setProgress(value: Int): IProgressDialog
    fun setProgressStyle(): IProgressDialog
    fun setMax(max: Int): IProgressDialog
    fun show(): IProgressDialog
    fun cancel(): IProgressDialog
}
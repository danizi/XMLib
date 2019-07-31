package com.xm.lib.component.tip.dlg.core

import android.view.View

/**
 * 对话框接口
 */
interface IDialog {
    fun setIcon(id: Int): IDialog
    fun setTitle(title: String): IDialog
    fun setMessage(msg: String): IDialog
    fun setPositiveButton(): IDialog
    fun setNeutralButton(): IDialog
    fun setNegativeButton(): IDialog
    fun setItems(items: Any, click: Any): IDialog
    fun setSingleChoiceItems(): IDialog
    fun setMultiChoiceItems(): IDialog
    fun setView(view: View?): IDialog
    fun show(): IDialog
}


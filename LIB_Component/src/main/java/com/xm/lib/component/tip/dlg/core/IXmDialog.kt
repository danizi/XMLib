package com.xm.lib.component.tip.dlg.core

import android.view.View

/**
 * 对话框接口
 */
interface IXmDialog : XmDialogInterface {

    fun setIcon(id: Int): IXmDialog

    fun setTitle(title: String): IXmDialog

    fun setMessage(msg: String): IXmDialog

    fun setPositiveButton(btn: String? = "确认", listener: XmDialogInterface.OnClickListener? = null): IXmDialog

    fun setNeutralButton(btn: String? = "中立", listener: XmDialogInterface.OnClickListener? = null): IXmDialog

    fun setNegativeButton(btn: String? = "取消", listener: XmDialogInterface.OnClickListener? = null): IXmDialog

    fun setItems(items: Array<String>, listener: XmDialogInterface.OnClickListener?): IXmDialog

    fun setSingleChoiceItems(items: Array<String>, listener:XmDialogInterface.OnClickListener?): IXmDialog

    fun setMultiChoiceItems(items: Array<String>, checkedItems: BooleanArray, listener:XmDialogInterface.OnMultiChoiceClickListener?): IXmDialog

    fun setView(view: View?): IXmDialog

    fun show(): IXmDialog

}


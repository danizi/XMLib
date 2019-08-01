package com.xm.lib.component.tip.dlg.core

/**
 * 相关接口
 */
interface XmDialogInterface {

    fun setOnDismissListener(listener: XmDialogInterface.OnDismissListener)

    fun setOnShowListener(listener: XmDialogInterface.OnShowListener)

    fun setOnCancelListener(listener: XmDialogInterface.OnCancelListener)

    interface OnCancelListener {
        fun onCancel(dialog: XmDialogInterface)
    }

    interface OnDismissListener {
        fun onDismiss(dialog: XmDialogInterface)
    }

    interface OnShowListener {
        fun onShow(dialog: XmDialogInterface)
    }

    interface OnClickListener {
        fun onClick(dialog: XmDialogInterface, which: Int)
    }

    interface OnMultiChoiceClickListener {
        fun onClick(dialog: XmDialogInterface, which: Int, isChecked: Boolean)
    }
}
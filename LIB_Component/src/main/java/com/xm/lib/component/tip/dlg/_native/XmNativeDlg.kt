package com.xm.lib.component.tip.dlg._native

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText
import com.xm.lib.component.tip.core.IXmDialog
import com.xm.lib.component.tip.core.XmDialogInterface

/**
 * 原生对话框
 */
class XmNativeDlg(private val context: Context?) : IXmDialog {

    private var builder: AlertDialog.Builder? = null
    var alertDialog: AlertDialog? = null

    init {
        if (context == null) {
            throw NullPointerException("context is null")
        }
        builder = AlertDialog.Builder(context)
    }


    override fun setIcon(id: Int): IXmDialog {
        builder?.setIcon(id)
        return this
    }

    override fun setTitle(title: String): IXmDialog {
        builder?.setTitle(title)
        return this
    }

    override fun setMessage(msg: String): IXmDialog {
        builder?.setMessage(msg)
        return this
    }

    override fun setPositiveButton(btn: String?, listener: XmDialogInterface.OnClickListener?): IXmDialog {
        builder?.setPositiveButton(btn) { _, which -> listener?.onClick(this@XmNativeDlg, which) }
        return this
    }

    override fun setNeutralButton(btn: String?, listener: XmDialogInterface.OnClickListener?): IXmDialog {
        builder?.setNeutralButton(btn) { _, which -> listener?.onClick(this@XmNativeDlg, which) }
        return this
    }

    override fun setNegativeButton(btn: String?, listener: XmDialogInterface.OnClickListener?): IXmDialog {
        builder?.setNegativeButton(btn) { _, which -> listener?.onClick(this@XmNativeDlg, which) }
        return this
    }

    override fun setItems(items: Array<String>, listener: XmDialogInterface.OnClickListener?): IXmDialog {

        builder?.setItems(items) { _, which ->
            listener?.onClick(this@XmNativeDlg, which)
        }
        return this
    }

    override fun setSingleChoiceItems(items: Array<String>, listener: XmDialogInterface.OnClickListener?): IXmDialog {
        builder?.setSingleChoiceItems(items, 0) { _, which ->
            listener?.onClick(this@XmNativeDlg, which)
        }
        return this
    }

    override fun setMultiChoiceItems(items: Array<String>, checkedItems: BooleanArray, listener: XmDialogInterface.OnMultiChoiceClickListener?): IXmDialog {
        builder?.setMultiChoiceItems(items, checkedItems) { _, which, isChecked ->
            listener?.onClick(this@XmNativeDlg, which, isChecked)
        }
        return this
    }

    override fun setView(view: View?): IXmDialog {
        if (view is EditText) {
            builder?.setView(view as EditText)
        }
        builder?.setView(view)
        return this
    }

    override fun show(): IXmDialog {
        alertDialog = builder?.create()
        alertDialog?.show()
        return this
    }

    override fun dismiss() {

    }

    override fun cancel() {

    }

    private fun size() {
        //设置窗口的大小
        //val alertDialog = (dialog as XmNativeDlg).alertDialog
        alertDialog?.setCanceledOnTouchOutside(true) // Sets whether this dialog is
        val w = alertDialog?.window
        val lp = w?.attributes
        lp?.width = 400
        lp?.height = 400
        lp?.x = 0
        lp?.y = 0
        alertDialog?.onWindowAttributesChanged(lp)
    }

    override fun setOnDismissListener(listener: XmDialogInterface.OnDismissListener) {
        alertDialog?.setOnDismissListener {
            listener.onDismiss(this)
        }
    }

    override fun setOnShowListener(listener: XmDialogInterface.OnShowListener) {
        alertDialog?.setOnShowListener {
            listener.onShow(this)
        }
    }

    override fun setOnCancelListener(listener: XmDialogInterface.OnCancelListener) {
        alertDialog?.setOnCancelListener {
            listener.onCancel(this)
        }
    }
}
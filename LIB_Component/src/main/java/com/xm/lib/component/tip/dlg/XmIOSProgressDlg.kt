package com.xm.lib.component.tip.dlg

import android.app.ProgressDialog
import android.content.Context
import com.xm.lib.component.tip.dlg.core.IXmProgressDialog
import com.xm.lib.component.tip.dlg.core.XmDialogInterface

/**
 * 苹果进度对话框
 */
class XmIOSProgressDlg(context: Context?) : IXmProgressDialog {

    private var p: XmIOSDlg.Control.P? = null
    private var control: XmIOSDlg.Control? = null

    init {
        p = XmIOSDlg.Control.P(this, context)
        control = XmIOSDlg.Control(this, context)
    }

    override fun setTitle(title: String): IXmProgressDialog {
        p?.title = title
        return this
    }

    override fun setMessage(msg: String): IXmProgressDialog {
        p?.message = msg
        return this
    }

    override fun setIndeterminate(indeterminate: Boolean): IXmProgressDialog {
        p?.indeterminate = indeterminate
        return this
    }

    override fun setCancelable(flag: Boolean): IXmProgressDialog {
        p?.cancelable = flag
        return this
    }

    override fun setProgress(value: Int): IXmProgressDialog {
        p?.progressValue = value
        return this
    }

    override fun setProgressStyle(): IXmProgressDialog {
        p?.progressStyle = ProgressDialog.STYLE_HORIZONTAL
        return this
    }

    override fun setMax(max: Int): IXmProgressDialog {
        p?.progressMax = max
        return this
    }

    override fun show(): IXmProgressDialog {
        p?.apply(control)
        control?.show()
        return this
    }

    override fun cancel(): IXmProgressDialog {
        return this
    }

    override fun setOnDismissListener(listener: XmDialogInterface.OnDismissListener) {
        p?.setOnDismissListener(listener)
    }

    override fun setOnShowListener(listener: XmDialogInterface.OnShowListener) {
        p?.setOnShowListener(listener)
    }

    override fun setOnCancelListener(listener: XmDialogInterface.OnCancelListener) {
        p?.setOnCancelListener(listener)
    }
}
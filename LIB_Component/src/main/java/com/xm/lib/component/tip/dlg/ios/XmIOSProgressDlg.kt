package com.xm.lib.component.tip.dlg.ios

import android.app.ProgressDialog
import android.content.Context
import com.xm.lib.component.tip.core.IXmProgressDialog
import com.xm.lib.component.tip.core.XmDialogInterface

/**
 * 苹果进度对话框
 */
class XmIOSProgressDlg(context: Context?) : IXmProgressDialog {


    private var p: XmDialogInterface.Control.P? = null
    private var control: XmDialogInterface.Control? = null

    init {
        p = XmDialogInterface.Control.P(this, context)
        control = XmDialogInterface.Control(this, context)
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
        control?.setProgressValue(value)
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

    override fun cancel() {
        control?.cancel()
    }

    override fun dismiss() {

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
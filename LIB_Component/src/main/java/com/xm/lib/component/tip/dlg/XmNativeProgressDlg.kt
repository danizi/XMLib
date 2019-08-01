package com.xm.lib.component.tip.dlg

import android.app.ProgressDialog
import android.content.Context
import com.xm.lib.common.log.BKLog
import com.xm.lib.component.tip.dlg.core.IXmProgressDialog
import com.xm.lib.component.tip.dlg.core.XmDialogInterface

/**
 * 原生进度条 & 加载
 */
class XmNativeProgressDlg(private var context: Context?) : IXmProgressDialog {


    /**
     * 加载对话框
     */
    private var progressDialog: ProgressDialog? = null

    init {
        progressDialog = ProgressDialog(context)
    }

    override fun setTitle(title: String): IXmProgressDialog {
        progressDialog?.setTitle(title)
        return this
    }

    override fun setMessage(msg: String): IXmProgressDialog {
        progressDialog?.setMessage(msg)
        return this
    }

    override fun setIndeterminate(indeterminate: Boolean): IXmProgressDialog {
        progressDialog?.isIndeterminate = indeterminate
        return this
    }

    override fun setCancelable(flag: Boolean): IXmProgressDialog {
        progressDialog?.setCancelable(flag)
        return this
    }

    override fun setProgress(value: Int): IXmProgressDialog {
        progressDialog?.progress = value
        return this
    }

    override fun setProgressStyle(): IXmProgressDialog {
        progressDialog?.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        return this
    }

    override fun setMax(max: Int): IXmProgressDialog {
        progressDialog?.max = max
        return this
    }

    override fun show(): IXmProgressDialog {
        progressDialog?.show()
        return this
    }

    override fun cancel(): IXmProgressDialog {
        progressDialog?.cancel()
        return this
    }

    override fun setOnDismissListener(listener: XmDialogInterface.OnDismissListener) {
        progressDialog?.setOnDismissListener {
            if (listener == null) {
                BKLog.e("OnDismissListener is null ")
                return@setOnDismissListener
            }
            listener.onDismiss(this)
        }
    }

    override fun setOnShowListener(listener: XmDialogInterface.OnShowListener) {
        progressDialog?.setOnShowListener {
            if (listener == null) {
                BKLog.e("OnShowListener is null ")
                return@setOnShowListener
            }
            listener.onShow(this)
        }
    }

    override fun setOnCancelListener(listener: XmDialogInterface.OnCancelListener) {
        progressDialog?.setOnCancelListener {
            if (listener == null) {
                BKLog.e("OnCancelListener is null ")
                return@setOnCancelListener
            }
            listener.onCancel(this)
        }
    }
}
package com.xm.lib.component.tip.dlg

import android.app.ProgressDialog
import android.content.Context
import com.xm.lib.component.tip.dlg.core.IProgressDialog

/**
 * 原生进度条
 */
class NativeProgressDlg(private var context: Context?) : IProgressDialog {


    private var progressDialog: ProgressDialog? = null

    init {
        progressDialog = ProgressDialog(context)
    }

    override fun setTitle(title: String): IProgressDialog {
        progressDialog?.setTitle(title)
        return this
    }

    override fun setMessage(msg: String): IProgressDialog {
        progressDialog?.setMessage(msg)
        return this
    }

    override fun setIndeterminate(indeterminate: Boolean): IProgressDialog {
        progressDialog?.isIndeterminate = indeterminate
        return this
    }

    override fun setCancelable(flag: Boolean): IProgressDialog {
        progressDialog?.setCancelable(flag)
        return this
    }

    override fun setProgress(value: Int): IProgressDialog {
        progressDialog?.progress = value

        return this
    }

    override fun setProgressStyle(): IProgressDialog {
        progressDialog?.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        return this
    }

    override fun setMax(max: Int): IProgressDialog {
        progressDialog?.max = max
        return this
    }

    override fun show(): IProgressDialog {
        progressDialog?.show()
        return this
    }

    override fun cancel(): IProgressDialog {
        progressDialog?.cancel()
        return this
    }


}
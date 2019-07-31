package com.xm.lib.component.tip.dlg.core

import android.content.Context
import com.xm.lib.component.tip.AbsTipFactory
import com.xm.lib.component.tip.dlg.IOSDlg
import com.xm.lib.component.tip.dlg.IOSProgressDlg
import com.xm.lib.component.tip.dlg.NativeDlg
import com.xm.lib.component.tip.dlg.NativeProgressDlg
import com.xm.lib.component.tip.pop.IPopWindow
import com.xm.lib.component.tip.toast.IToast


/**
 * 弹出框
 */
class DialogFactory : AbsTipFactory() {

    override fun getProgressDialog(context: Context?, type: CreateDialogType): IProgressDialog? {
        return when (type) {
            CreateDialogType.NATIVE_PROGRESS_DIALOG -> {
                NativeProgressDlg(context)
            }
            CreateDialogType.CUSTOM_IOS_PROGRESS_DIALOG -> {
                IOSProgressDlg()
            }
            else -> {
                NativeProgressDlg(context)
            }
        }
    }

    override fun getDialog(context: Context?, type: CreateDialogType): IDialog? {
        return when (type) {
            CreateDialogType.NATIVE_ALERT_DIALOG -> {
                NativeDlg(context)
            }
            CreateDialogType.CUSTOM_IOS_DIALOG -> {
                IOSDlg()
            }
            else -> {
                NativeDlg(context)
            }
        }
    }

    override fun getToast(context: Context?): IToast? {
        return null
    }

    override fun getPopWindow(context: Context?): IPopWindow? {
        return null
    }

    override fun getAdDialog(context: Context?) {

    }
}
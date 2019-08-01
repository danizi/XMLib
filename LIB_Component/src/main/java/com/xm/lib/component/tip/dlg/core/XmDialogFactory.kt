package com.xm.lib.component.tip.dlg.core

import android.content.Context
import com.xm.lib.component.tip.AbsTipFactory
import com.xm.lib.component.tip.dlg.XmIOSDlg
import com.xm.lib.component.tip.dlg.XmIOSProgressDlg
import com.xm.lib.component.tip.dlg.XmNativeDlg
import com.xm.lib.component.tip.dlg.XmNativeProgressDlg
import com.xm.lib.component.tip.pop.IPopWindow
import com.xm.lib.component.tip.toast.IToast


/**
 * 弹出框
 */
class XmDialogFactory : AbsTipFactory() {

    override fun getProgressDialog(context: Context?, type: CreateDialogType): IXmProgressDialog? {
        return when (type) {
            CreateDialogType.NATIVE_PROGRESS_DIALOG -> {
                XmNativeProgressDlg(context)
            }
            CreateDialogType.CUSTOM_IOS_PROGRESS_DIALOG -> {
                XmIOSProgressDlg()
            }
            else -> {
                XmNativeProgressDlg(context)
            }
        }
    }

    override fun getDialog(context: Context?, type: CreateDialogType): IXmDialog? {
        return when (type) {
            CreateDialogType.NATIVE_ALERT_DIALOG -> {
                XmNativeDlg(context)
            }
            CreateDialogType.CUSTOM_IOS_DIALOG -> {
                XmIOSDlg(context)
            }
            else -> {
                XmNativeDlg(context)
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
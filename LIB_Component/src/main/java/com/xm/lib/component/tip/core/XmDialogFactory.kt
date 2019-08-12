package com.xm.lib.component.tip.core

import android.content.Context
import com.xm.lib.component.tip.dlg._native.XmNativeDlg
import com.xm.lib.component.tip.dlg._native.XmNativeProgressDlg
import com.xm.lib.component.tip.dlg.ad.XmAdDlg
import com.xm.lib.component.tip.dlg.ios.XmIOSDlg
import com.xm.lib.component.tip.dlg.ios.XmIOSProgressDlg
import com.xm.lib.component.tip.pop.XmPopWindow
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
                XmIOSProgressDlg(context)
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
        return XmPopWindow(context)
    }

    override fun getAdDialog(context: Context?): XmAdDlg? {
        return XmAdDlg(context)
    }
}
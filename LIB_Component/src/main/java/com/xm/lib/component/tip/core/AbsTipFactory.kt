package com.xm.lib.component.tip.core

import android.content.Context
import com.xm.lib.component.tip.dlg.ad.XmAdDlg
import com.xm.lib.component.tip.core.CreateDialogType
import com.xm.lib.component.tip.core.IXmDialog
import com.xm.lib.component.tip.core.IXmProgressDialog
import com.xm.lib.component.tip.core.IPopWindow
import com.xm.lib.component.tip.toast.IToast

/**
 * 提示组件抽象工厂
 */
abstract class AbsTipFactory {

    /**
     * 对话框
     */
    abstract fun getDialog(context: Context?, type: CreateDialogType): IXmDialog?

    /**
     * 进度对话框
     */
    abstract fun getProgressDialog(context: Context?,type: CreateDialogType): IXmProgressDialog?

    /**
     * 得到广告
     */
    abstract fun getAdDialog(context: Context?): XmAdDlg?

    /**
     * Toast提示
     */
    abstract fun getToast(context: Context?): IToast?

    /**
     * 弹出框实例
     */
    abstract fun getPopWindow(context: Context?): IPopWindow?

}







package com.xm.lib.component.tip

import android.content.Context
import com.xm.lib.component.tip.dlg.core.CreateDialogType
import com.xm.lib.component.tip.dlg.core.IDialog
import com.xm.lib.component.tip.dlg.core.IProgressDialog
import com.xm.lib.component.tip.pop.IPopWindow
import com.xm.lib.component.tip.toast.IToast

/**
 * 提示组件抽象工厂
 */
abstract class AbsTipFactory {

    /**
     * 对话框
     */
    abstract fun getDialog(context: Context?, type: CreateDialogType): IDialog?

    /**
     * 进度对话框
     */
    abstract fun getProgressDialog(context: Context?,type: CreateDialogType): IProgressDialog?

    /**
     * 得到广告
     */
    abstract fun getAdDialog(context: Context?)

    /**
     * Toast提示
     */
    abstract fun getToast(context: Context?): IToast?

    /**
     * 弹出框实例
     */
    abstract fun getPopWindow(context: Context?): IPopWindow?

}







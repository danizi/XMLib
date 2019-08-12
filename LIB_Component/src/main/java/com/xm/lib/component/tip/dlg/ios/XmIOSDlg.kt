package com.xm.lib.component.tip.dlg.ios

import android.content.Context
import android.view.View
import com.xm.lib.component.tip.core.IXmDialog
import com.xm.lib.component.tip.core.XmDialogInterface


/**
 * 苹果风格对话框
 */
class XmIOSDlg(context: Context?) : IXmDialog {


    private var p: XmDialogInterface.Control.P? = null
    private var control: XmDialogInterface.Control? = null

    init {
        control = XmDialogInterface.Control(this, context)
        p = XmDialogInterface.Control.P(this, context)
    }

    override fun setIcon(id: Int): IXmDialog {
        p?.iconId = id
        return this
    }

    override fun setTitle(title: String): IXmDialog {
        p?.title = title
        return this
    }

    override fun setMessage(msg: String): IXmDialog {
        p?.message = msg
        return this
    }

    override fun setPositiveButton(btn: String?, listener: XmDialogInterface.OnClickListener?): IXmDialog {
        p?.setPositiveButton(btn, listener)
        return this
    }

    override fun setNeutralButton(btn: String?, listener: XmDialogInterface.OnClickListener?): IXmDialog {
        p?.setNeutralButton(btn, listener)
        return this
    }

    override fun setNegativeButton(btn: String?, listener: XmDialogInterface.OnClickListener?): IXmDialog {
        p?.setNegativeButton(btn, listener)
        return this
    }

    override fun setItems(items: Array<String>, listener: XmDialogInterface.OnClickListener?): IXmDialog {
        p?.setItems(items, listener)
        return this
    }

    override fun setSingleChoiceItems(items: Array<String>, listener: XmDialogInterface.OnClickListener?): IXmDialog {
        p?.setSingleChoiceItems(items, listener)
        return this
    }

    override fun setMultiChoiceItems(items: Array<String>, checkedItems: BooleanArray, listener: XmDialogInterface.OnMultiChoiceClickListener?): IXmDialog {
        p?.setMultiChoiceItems(items, checkedItems, listener)
        return this
    }

    override fun setView(view: View?): IXmDialog {
        p?.setView = view
        return this
    }

    override fun show(): IXmDialog {
        p?.apply(control)
        control?.show()
        return this
    }

    override fun dismiss() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cancel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
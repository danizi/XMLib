package com.xm.lib.component.tip.dlg

import android.content.Context
import android.view.View
import com.xm.lib.component.R
import com.xm.lib.component.XmIOSDialog
import com.xm.lib.component.tip.dlg.core.IXmDialog
import com.xm.lib.component.tip.dlg.core.XmDialogInterface

/**
 * 苹果风格对话框
 */
class XmIOSDlg(private var context: Context?) : IXmDialog {

    private var p: Control.P? = null
    private var control: Control? = null

    init {
        control = Control()
        p = Control.P()
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
        p?.msg = msg
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
        //首先设置好界面
        //显示界面
        control?.show()
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

    class Control {
        private var context: Context? = null
        private var xmIOSDlg: XmIOSDialog? = null

        fun show() {
            //
            xmIOSDlg = XmIOSDialog(context).setLayoutId(R.layout.view_dlg)
        }

        init {
            //布局设置
        }

        /**
         * 相关参数
         */
        class P {
            var iconId: Int = -1
            var title: String = ""
            var message: String = ""
            var setView: View? = null
            var msg: String = ""

            private var positiveBtn: String? = ""
            private var neutralBtn: String? = ""
            private var negativeBtn: String? = ""

            private var positiveListener: XmDialogInterface.OnClickListener? = null
            private var neutralListener: XmDialogInterface.OnClickListener? = null
            private var negativeListener: XmDialogInterface.OnClickListener? = null
            private var listListener: XmDialogInterface.OnClickListener? = null
            private var multiChoiceListener: XmDialogInterface.OnMultiChoiceClickListener? = null
            private var dismissListener: XmDialogInterface.OnDismissListener? = null
            private var showListener: XmDialogInterface.OnShowListener? = null
            private var cancelListener: XmDialogInterface.OnCancelListener? = null
            private var singleListener: XmDialogInterface.OnClickListener? = null

            private var listLayoutID = R.layout.view_dlg
            private var multiLayoutID = R.layout.view_dlg
            private var singleLayoutID = R.layout.view_dlg

            private var listItems: Array<String>? = null
            private var multiChoiceItems: Array<String>? = null
            private var multiChoiceCheckedItems: BooleanArray? = null
            private var singleItems: Array<String>? = null

            fun apply(control: Control?) {

            }

            fun setPositiveButton(btn: String?, listener: XmDialogInterface.OnClickListener?) {
                positiveBtn = btn
                positiveListener = listener
            }

            fun setNeutralButton(btn: String?, listener: XmDialogInterface.OnClickListener?) {
                neutralBtn = btn
                neutralListener = listener
            }

            fun setNegativeButton(btn: String?, listener: XmDialogInterface.OnClickListener?) {
                negativeBtn = btn
                negativeListener = listener
            }

            fun setItems(items: Array<String>, listener: XmDialogInterface.OnClickListener?) {
                listItems = items
                listListener = listener
            }

            fun setSingleChoiceItems(items: Array<String>, listener: XmDialogInterface.OnClickListener?) {
                singleItems = items
                singleListener = listener
            }

            fun setMultiChoiceItems(items: Array<String>, checkedItems: BooleanArray, listener: XmDialogInterface.OnMultiChoiceClickListener?) {
                multiChoiceItems = items
                multiChoiceCheckedItems = checkedItems
                multiChoiceListener = listener
            }

            fun setOnDismissListener(listener: XmDialogInterface.OnDismissListener) {
                dismissListener = listener
            }

            fun setOnShowListener(listener: XmDialogInterface.OnShowListener) {
                showListener = listener
            }

            fun setOnCancelListener(listener: XmDialogInterface.OnCancelListener) {
                cancelListener = listener
            }
        }
    }
}
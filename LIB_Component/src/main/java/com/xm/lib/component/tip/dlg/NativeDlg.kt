package com.xm.lib.component.tip.dlg

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText
import com.xm.lib.component.tip.dlg.core.IDialog

/**
 * 原生对话框
 */
class NativeDlg(private val context: Context?) : IDialog {

    private var builder: AlertDialog.Builder? = null

    init {
        if (context == null) {
            throw NullPointerException("context is null")
        }
        builder = AlertDialog.Builder(context)
    }

    override fun setIcon(id: Int): IDialog {
        builder?.setIcon(id)
        return this
    }

    override fun setTitle(title: String): IDialog {
        builder?.setTitle(title)
        return this
    }

    override fun setMessage(msg: String): IDialog {
        builder?.setMessage(msg)
        return this
    }

    override fun setPositiveButton(): IDialog {
        builder?.setPositiveButton("确认", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {

            }
        })
        return this
    }

    override fun setNeutralButton(): IDialog {
        builder?.setNeutralButton("中立", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {

            }
        })
        return this
    }

    override fun setNegativeButton(): IDialog {
        builder?.setNegativeButton("取消", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {

            }
        })
        return this
    }

    override fun setItems(items: Any, click: Any): IDialog {
        builder?.setItems(arrayOf("1,2,3,4,5,6"), object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {

            }
        })
        return this
    }

    override fun setSingleChoiceItems(): IDialog {
        builder?.setSingleChoiceItems(arrayOf("1", "2", "3"), 0, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {

            }
        })
        return this
    }

    override fun setMultiChoiceItems(): IDialog {
        builder?.setMultiChoiceItems(arrayOf("1", "2", "3"), booleanArrayOf(false, false, false), object : DialogInterface.OnMultiChoiceClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int, isChecked: Boolean) {

            }
        })
        return this
    }

    override fun setView(view: View?): IDialog {
        if (view is EditText) {
            builder?.setView(view as EditText)
        }
        builder?.setView(view)
        return this
    }

    override fun show(): IDialog {
        builder?.show()
        return this
    }
}
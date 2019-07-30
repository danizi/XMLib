package com.xm.lib.component.tip

import android.view.View
import android.widget.EditText

/**
 * 提示组件抽象工厂
 */
abstract class TipFactory {
    /**
     * 对话框实例
     */
    abstract fun getDialog(): IDialog

    /**
     * Toast提示实例
     */
    abstract fun getToast(): IToast

    /**
     * 弹出框实例
     */
    abstract fun getPopWindow(): IPopWindow
}

/**
 * 对话框
 */
interface IDialog {

    fun setIcon(id: Int)
    fun setTitle()
    fun setMessage()
    fun setPositiveButton()
    fun setNeutralButton()
    fun setNegativeButton()

    //多选
    fun setItems(items: Any, click: Any)

    //当选
    fun setSingleChoiceItems()

    fun setPositiveButton(dialog: Any, which: Int)

    //等待
    fun setIndeterminate()

    //进度
    fun setProgress()

    fun setProgressStyle()
    fun setMax()

    //编辑对话框
    fun setView(editText: EditText?)

    //自定义对话框
    fun setView(view: View?)

    fun show()
}

/**
 * Toast
 */
interface IToast {
    //自定义对话框
    fun setView(view: View?)

    fun show()

}

/**
 * 弹出框PopWindow
 */
interface IPopWindow {

    fun setView(view: View)

    fun showAtLocation()
}

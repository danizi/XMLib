package com.xm.lib.component.tip.toast

import android.view.View

/**
 * Toast
 */
interface IToast {

    /**
     * 设置自定义View
     */
    fun setView(view: View?)

    /**
     * 显示
     */
    fun show()

}
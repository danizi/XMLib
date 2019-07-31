package com.xm.lib.component.tip.pop

import android.view.View

/**
 * 弹出框PopWindow
 */
interface IPopWindow {

    /**
     * 设置自定义View
     */
    fun setView(view: View)

    /**
     * 显示
     */
    fun showAtLocation()
}
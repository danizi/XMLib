package com.xm.lib.component

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.PopupWindow

/**
 * 弹出框
 */
class XmPopWindow(val context: Context?) : PopupWindow(context) {

    fun ini(contentView: View?, width: Int = ViewGroup.LayoutParams.WRAP_CONTENT, height: Int = ViewGroup.LayoutParams.WRAP_CONTENT) {
        this.contentView = contentView
        this.isOutsideTouchable = true
        this.isFocusable = true
        this.width = width
        this.height = height
        //实例化一个ColorDrawable颜色为半透明,设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(ColorDrawable(0x00000000))

        //this.setBackgroundDrawable(  ColorDrawable(android.graphics.Color.BLACK))
        this.setOnDismissListener {
            lp?.alpha = 1.0f
            (context as Activity).window.attributes = lp
        }
    }

    init {

    }

    private var lp: WindowManager.LayoutParams? = null
    fun showAtLocation(location: Location?, animationStyle: Int, parentView: View, x: Int, y: Int) {
        var gravity = Gravity.CENTER_HORIZONTAL
        when (location) {
            Location.LEFT -> {
                gravity = Gravity.LEFT
            }
            Location.RIGHT -> {
                gravity = Gravity.RIGHT
            }
            Location.TOP -> {
                gravity = Gravity.TOP
            }
            Location.BOTTOM -> {
                gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            }
        }
        if (animationStyle != 0) {
            this.animationStyle = animationStyle
        }
        lp = (context as Activity).window.attributes;
        lp?.alpha = 0.3f
        (context as Activity).window.attributes = lp
        showAtLocation(parentView, gravity, x, y)
    }

    /**
     * 弹出位置
     */
    enum class Location {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM
    }
}
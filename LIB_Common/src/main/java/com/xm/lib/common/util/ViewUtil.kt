package com.xm.lib.common.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.*

object ViewUtil {

    /**
     * 设置宽高
     */
    fun setViewSize(view: View?, width: Int, height: Int) {
        view?.layoutParams?.width = width
        view?.layoutParams?.height = height
    }

    /**
     * 通过layoutid获取view
     */
    @SuppressLint("InflateParams")
    fun viewById(context: Context?, resID: Int, root: ViewGroup? = null): View? {
        return if (root == null) {
            LayoutInflater.from(context).inflate(resID, null, false)
        } else {
            LayoutInflater.from(context).inflate(resID, root, false)
        }
    }


    /**
     * 显示
     */
    fun show(target: View?) {
        if (target?.visibility == View.GONE) {
            target.visibility = View.VISIBLE
        }
    }

    /**
     * 隐藏
     */
    fun hide(target: View?) {
        if (target?.visibility == View.VISIBLE) {
            target.visibility = View.GONE
        }
    }
}
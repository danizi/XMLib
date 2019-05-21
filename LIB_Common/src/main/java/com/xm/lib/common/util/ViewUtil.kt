package com.xm.lib.common.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.*
import android.widget.RelativeLayout
import android.graphics.Bitmap
import android.graphics.Canvas


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

    fun setMargins(view: View?, left: Int, top: Int, right: Int, bottom: Int) {
        val lp = RelativeLayout.LayoutParams(view?.layoutParams)
        lp.setMargins(left, top, right, bottom)
    }

    /**
     * view转bitmap
     */
    @Deprecated("")
    fun getViewBitmap(addViewContent: View): Bitmap {

        addViewContent.isDrawingCacheEnabled = true

        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        addViewContent.layout(0, 0,
                addViewContent.measuredWidth,
                addViewContent.measuredHeight)

        addViewContent.buildDrawingCache()
        val cacheBitmap = addViewContent.drawingCache

        return Bitmap.createBitmap(cacheBitmap)
    }

    fun getViewBitmap2(view: View): Bitmap {
        val shareBitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_4444)
        val c = Canvas(shareBitmap)
        view.draw(c)
        return shareBitmap
        //return setImgSize(shareBitmap, view.measuredWidth, (view.measuredWidth * 1.7).toInt())
    }

}
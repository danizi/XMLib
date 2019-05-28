package com.xm.lib.component

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

import com.xm.lib.common.util.ScreenUtil

import java.util.HashMap

class XmAutoViewPager : ViewPager {

    private val map = HashMap<Int, Int>(2)

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    //    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        var heightMeasureSpec = heightMeasureSpec
//        var height = 0
//        for (i in 0 until childCount) {
//            val child = getChildAt(i)
//            child.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
//            val h = child.measuredHeight
//            if (h > height) {
//                height = h
//            }
//        }
//
//        // Viewpager网页处理
//        if (height > 0 && currentItem == 0) {
//            val webViewH = (getChildAt(0) as ViewGroup).getChildAt(0).measuredHeight
//            height = webViewH
//        }
//
//        height += ScreenUtil.dip2px(context, 30)
//        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//    }
    protected override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        var height = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            val h = child.measuredHeight
            if (h > height) {
                height = h
            }
        }

        // Viewpager网页处理
        if (height > 0 && currentItem == 0) {
            val webViewH = (getChildAt(0) as ViewGroup).getChildAt(0).measuredHeight
            height = webViewH
        }

        height = height + ScreenUtil.dip2px(context, 70)
        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}

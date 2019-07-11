package com.xm.lib.component

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.xm.lib.common.log.BKLog

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


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

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

        // Viewpager网页处理 ps:免费播放视频页面中
        if (height > 0 && currentItem == 0) {
            val webViewH = (getChildAt(0) as ViewGroup).getChildAt(0).measuredHeight
            height = webViewH
            BKLog.d("网页高度：$height")
        }


        //height = measuredHeight1(height)
        height = measuredHeight2(height)

        height += ScreenUtil.dip2px(context, 70)
        //设置最低高度
        if (defaultHeight > height) {
            height = defaultHeight
        }
        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private val defaultHeight = 500

    /**
     * 测量有一个WebView和RecyclerView
     */
    private fun measuredHeight1(height: Int): Int {
        var height1 = height
        if (height1 > 0 && currentItem == 0) {
            val a = (getChildAt(0) as ViewGroup).measuredHeight
            height1 = a
            BKLog.d("a height -> $a")
        } else if (height1 > 0 /*&& currentItem == 1*/) {
            try {
                val b = (getChildAt(currentItem) as ViewGroup).measuredHeight
                BKLog.d("b height -> $b ")
                height1 = b
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return height1
    }

    /**
     * 测量有RecyclerView
     */
    private fun measuredHeight2(height: Int): Int {
        var height2 = height
        try {
            val b = (getChildAt(currentItem) as ViewGroup).measuredHeight
            BKLog.d("b height -> $b ")
            height2 = b
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return height2
    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        Log.d("", "onMeasure")
//        // find the current child view
//        val view = getChildAt(currentItem)
//        view?.measure(widthMeasureSpec, heightMeasureSpec)
//
//        setMeasuredDimension(measuredWidth, measureHeight(heightMeasureSpec, view))
//    }
//
//    /**
//     * Determines the height of this view
//     *
//     * @param measureSpec A measureSpec packed into an int
//     * @param view the base view with already measured height
//     *
//     * @return The height of the view, honoring constraints from measureSpec
//     */
//    private fun measureHeight(measureSpec: Int, view: View?): Int {
//        var result = 0
//        val specMode = View.MeasureSpec.getMode(measureSpec)
//        val specSize = View.MeasureSpec.getSize(measureSpec)
//
//        if (specMode == View.MeasureSpec.EXACTLY) {
//            result = specSize
//        } else {
//            // set the height from the base view if available
//            if (view != null) {
//                result = view.measuredHeight
//            }
//            if (specMode == View.MeasureSpec.AT_MOST) {
//                result = Math.min(result, specSize)
//            }
//        }
//        return result
//    }

}

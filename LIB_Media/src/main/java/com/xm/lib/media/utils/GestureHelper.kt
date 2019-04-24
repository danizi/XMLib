package com.xm.lib.media.utils

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import com.xm.lib.common.log.BKLog
import kotlin.math.abs

/**
 * 手势
 * 常见手势有：单击，长按，双击，滑动，快速滑动，缩放
 * Android手势监听器
 * GestureDetector.OnGestureListener
 * ScaleGestureDetector.OnScaleGestureListener
 *
 */
class GestureHelper(context: Context?) {
    companion object {
        const val TAG = "GestureHelper"
        const val VERTICAL_LEFT_VALUE = "vertical_left"
        const val VERTICAL_RIGHT_VALUE = "vertical_right"
    }

    private var listener: OnGestureListener? = null
    private var simpleGestureListener: GestureDetector? = null
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var simpleOnGestureListener: GestureDetector.SimpleOnGestureListener? = null
    private var scaleGestureListener: ScaleGestureDetector.OnScaleGestureListener? = null
    private var widget: View? = null
    private var w = 0
    private var h = 0
    private var wHalf = 0

    init {
        simpleOnGestureListener = object : GestureDetector.SimpleOnGestureListener() {

            private val VERTICAL_LEFT = VERTICAL_LEFT_VALUE    //左半部分上下滑动
            private val VERTICAL_RIGHT = VERTICAL_RIGHT_VALUE  //右半部分上下滑动
            private val HORIZONTAL = "horizontal"              //水平方向滑动
            private val DEFALUT_HORIZONTAL = 10                //水平滑动判断基准值
            private val NONE = "none"                          //水平方向滑动
            private var scrollType = NONE                      //用户"点击释放"都需要初始化为NODE

            override fun onDown(e: MotionEvent?): Boolean {
                scrollType = NONE
                listener?.onDown()
                return super.onDown(e)
            }

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                BKLog.d(TAG, "单击")
                scrollType = NONE
                listener?.onClick()
                return super.onSingleTapUp(e)
            }

            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                val offsetX = e2?.x!! - e1?.x!!
                val offsetY = e2.y - e1.y

                if (scrollType == NONE) { //判断当前滑动类型
                    scrollType = when {
                        Math.abs(offsetX) > DEFALUT_HORIZONTAL -> //横向滑动
                            HORIZONTAL
                        e1.x > wHalf -> //事件在控件中间分割线的右边
                            VERTICAL_RIGHT
                        e1.x < wHalf -> //事件在控件中间分割线的左边
                            VERTICAL_LEFT
                        else ->
                            NONE
                    }
                }

                if (scrollType == NONE) {
                    return super.onScroll(e1, e2, distanceX, distanceY)
                }
                when (scrollType) {
                    HORIZONTAL -> {
                        val present = (offsetX * 100 / w).toInt()
                        if (abs(present) < 100) {
                            BKLog.i(TAG, "水平滑动:$present%")
                            listener?.onHorizontal(present)
                        }
                    }
                    VERTICAL_RIGHT -> {
                        val present = (offsetY * 100 / h).toInt()
                        if (abs(present) < 100) {
                            BKLog.i(TAG, "右半部分滑动:${-present}%")  //PS:百分比
                            listener?.onVertical(VERTICAL_RIGHT, present)
                        }
                    }
                    VERTICAL_LEFT -> {
                        val present = (offsetY * 100 / h).toInt()
                        if (abs(present) < 100) {
                            BKLog.i(TAG, "左半部分滑动:${-present}%")
                            listener?.onVertical(VERTICAL_LEFT, present)
                        }
                    }
                }
                return super.onScroll(e1, e2, distanceX, distanceY)
            }

            override fun onDoubleTap(e: MotionEvent?): Boolean {
                BKLog.d(TAG, "双击")
                return super.onDoubleTap(e)
            }
        }
        scaleGestureListener = object : ScaleGestureDetector.OnScaleGestureListener {
            override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
                BKLog.d(TAG, "onScaleBegin scaleFactor:${detector?.scaleFactor}")
                return true
            }

            override fun onScaleEnd(detector: ScaleGestureDetector?) {
                BKLog.d(TAG, "onScaleEnd scaleFactor:${detector?.scaleFactor}")
            }

            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                BKLog.d(TAG, "onScale scaleFactor:${detector?.scaleFactor}放大因子")
                return false
            }

        }
        simpleGestureListener = GestureDetector(context, simpleOnGestureListener)
        scaleGestureDetector = ScaleGestureDetector(context, scaleGestureListener)
    }

    fun bind(widget: View?) {
        this.widget = widget
        w = this.widget?.measuredWidth!!
        h = this.widget?.measuredHeight!!
        wHalf = w.div(2)
    }

    fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            listener?.onDownUp()                    //监听UP事件
        }
        if (event?.pointerCount == 2) {
            scaleGestureDetector?.onTouchEvent(event)//两指操作
        } else {
            simpleGestureListener?.onTouchEvent(event)//一指操作
        }
        return true
    }

    fun setOnGestureListener(listener: OnGestureListener) {
        this.listener = listener
    }

    /**
     * 对外提供的监听
     */
    interface OnGestureListener {
        fun onDown()
        fun onDownUp()
        fun onClick()
        fun onHorizontal(present: Int)
        fun onVertical(type: String, present: Int)
        fun onDoubleClick()
        fun onScaleEnd(scaleFactor: Float)
    }
}
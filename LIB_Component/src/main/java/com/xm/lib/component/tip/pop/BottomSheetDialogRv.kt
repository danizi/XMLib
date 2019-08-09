package com.xm.lib.component.tip.pop

import android.annotation.SuppressLint
import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.xm.lib.common.log.BKLog

class BottomSheetDialogRv : RecyclerView {

    private var downY: Float = 0.toFloat()
    private var moveY: Float = 0.toFloat()
    var isOverScroll = true
    private var bottomCoordinator: CoordinatorLayout? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    fun setCoordinatorDisallow() {
        if (bottomCoordinator == null)
            return
        bottomCoordinator?.requestDisallowInterceptTouchEvent(true)
    }

    /**
     * 绑定需要被拦截 intercept 的 CoordinatorLayout
     * @param contentView View
     */
    fun bindBottomSheetDialog(contentView: View) {
        // try throw illegal
        try {
            val parentOne = contentView.parent as FrameLayout
            this.bottomCoordinator = parentOne.parent as CoordinatorLayout
            setOnTouchListener(OnTouchListener { v, event ->
                if (canScrollVertically(-1)) {
                    requestDisallowInterceptTouchEvent(false)
                } else {
                    requestDisallowInterceptTouchEvent(true)
                }
                false

                if (bottomCoordinator == null)
                    return@OnTouchListener false
                val firstVisiblePos = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        Log.e("aaaaa", "child onTouch ACTION_DOWN")
                        downY = event.rawY
                        bottomCoordinator?.requestDisallowInterceptTouchEvent(true)
                        return@OnTouchListener false
                    }
                    MotionEvent.ACTION_MOVE -> {
                        moveY = event.rawY
                        Log.e("aaaaa", "child onTouch ACTION_MOVE firstVisiblePos $firstVisiblePos -- isOverScroll $isOverScroll")
                        if (moveY - downY > 10) {
                            Log.e("aaaaa", "child onTouch 不阻断")
                            // coordinator.requestDisallowInterceptTouchEvent(true);
                            if (firstVisiblePos == 0 && isOverScroll) {
                                bottomCoordinator?.requestDisallowInterceptTouchEvent(false)
                                return@OnTouchListener false
                            }
                        }
                        Log.e("aaaaa", "child onTouch 阻断")
                        bottomCoordinator?.requestDisallowInterceptTouchEvent(true)
                        return@OnTouchListener false
                    }
                    MotionEvent.ACTION_UP -> {
                        Log.e("aaaaa", "child onTouch ACTION_UP")
                        return@OnTouchListener false
                    }
                }
                false
            }
            )
        } catch (e: Exception) {
            // maybe 可能是强转异常
            // todo
        }

    }

    @SuppressLint("WrongConstant")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (bottomCoordinator == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }
        val size = (resources.displayMetrics.heightPixels * 0.618).toFloat().toInt()
        val newHeightSpec = View.MeasureSpec.makeMeasureSpec(
                size,
                Integer.MIN_VALUE // mode
        )
        super.onMeasure(widthMeasureSpec, newHeightSpec)
    }
//
//    override fun onOverScrolled(scrollX: Int, scrollY: Int, clampedX: Boolean, clampedY: Boolean) {
//        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY)
//        isOverScroll = clampedY
//    }
//
//    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
//        super.onScrollChanged(l, t, oldl, oldt)
//
//    }
}
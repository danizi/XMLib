package com.xm.lib.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
/**
 * 圆弧背景https://blog.csdn.net/mq2856992713/article/details/78635790
 */
class XmArcView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var paint = Paint()
    private var canvas: Canvas? = null
    private var w: Float = 0F
    private var h: Float = 0F

    private var mArcHeight = 0

    init {
        paint = Paint()
        paint.color = StringToColor("#FFFFFFFF")
        paint.strokeWidth = 1F
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        mArcHeight = dp2px(context!!, 30f)
    }

    private fun dp2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        this.canvas = canvas
        this.w = width.toFloat()
        this.h = height.toFloat()
        val rectF = RectF(0f, 0f, w, h)

        drawArc(rectF)       // 画弧形
        drawRect(rectF)      // 画底部矩形
    }

    private fun drawRect(rectF: RectF) {
        /*绘制矩形*/
        val rect = Rect(0, mArcHeight, w.toInt(), h.toInt())
        canvas?.drawRect(rect, paint)
    }

    private fun drawArc(rectF: RectF) {
        /*绘制扇形*/
        //绘制规则解释 https://www.cnblogs.com/huyang011/p/9551576.html
        //canvas?.drawArc(rectF, -180f, 180f, false, paint)
        val path = Path()
        val pointF1 = PointF(w / 2, (-mArcHeight).toFloat())        // 控制点()
        val pointF2 = PointF(w, mArcHeight.toFloat()) // 数据点右边点
        path.moveTo(0f, mArcHeight.toFloat())     // 数据点左边点
        path.quadTo(pointF1.x, pointF1.y, pointF2.x, pointF2.y)
        canvas?.drawPath(path, paint)
    }

    private fun drawArcStroke(rectF: RectF) {
        /*扇形描边*/
        if (false) {
            paint.color = StringToColor("#FFFFFFFF")
            paint.style = Paint.Style.STROKE
            drawArc(rectF)
        }
    }

    private fun StringToColor(str: String): Int {
        return -0x1000000 or Integer.parseInt(str.substring(2), 16)
    }

}
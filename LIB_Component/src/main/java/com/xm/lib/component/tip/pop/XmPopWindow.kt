package com.xm.lib.component.tip.pop

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.PopupWindow
import com.xm.lib.common.log.BKLog
import com.xm.lib.component.R


class XmPopWindow(private val context: Context?) : IPopWindow {

    private var pop: PopupWindow? = null
    private var lp: WindowManager.LayoutParams? = null

    init {
        pop = PopupWindow()
        pop?.isOutsideTouchable = true
        pop?.isFocusable = true
        pop?.width = ViewGroup.LayoutParams.MATCH_PARENT
        pop?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        pop?.setBackgroundDrawable(ColorDrawable(0x00000000))     //实例化一个ColorDrawable颜色为半透明,设置SelectPicPopupWindow弹出窗体的背景
    }

    override fun setView(view: View): IPopWindow {
        pop?.contentView = view
        return this
    }

    override fun showAtLocation(): IPopWindow {
        //设置弹出框动画
        pop?.animationStyle = R.style.pop_ani

        //窗口背景灰显
        setWindowBkAlpha(0.3f)
        pop?.setOnDismissListener {
            //窗口透明
            setWindowBkAlpha(1.0f)
        }
//        val gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        val gravity = Gravity.CENTER
        pop?.showAtLocation(getWindow().decorView, gravity, 0, 0)
        return this
    }

    private fun getWindow(): Window {
        return (context as Activity).window
    }

    private fun setWindowBkAlpha(alpha: Float) {
        if (lp == null) {
            lp = getWindow().attributes
        }
        var values1 = 0f
        var values2 = 0f

        if (lp?.alpha!! >= 1) {  //透明的
            values1 = 1f
            values2 = alpha
        } else {
            values1 = lp?.alpha!!
            values2 = 1f
        }

        val animator = ValueAnimator.ofFloat(values1, values2)
        animator.addUpdateListener { animation ->
            val alpha = animation.animatedValue as Float
            BKLog.d("alpha:$alpha")
            lp?.alpha = alpha
            getWindow().attributes = lp
        }
        animator.duration = 200
        animator.start()
    }
}
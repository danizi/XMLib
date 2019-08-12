package com.xm.lib.component.tip.pop

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.annotation.NonNull
import android.view.*
import android.widget.PopupWindow
import com.xm.lib.common.log.BKLog
import com.xm.lib.component.R
import android.support.design.widget.BottomSheetBehavior
import android.view.ViewGroup
import com.xm.lib.component.tip.core.IPopWindow
import com.xm.lib.component.tip.dlg.sheet.SpringBackBottomSheetDialog


class XmPopWindow(private val context: Context?) : IPopWindow {

    private var control: Control? = null

    init {
        control = Control(context)
    }

    override fun setView(view: View): IPopWindow {
        control?.setContentView(view)
        return this
    }

    override fun showAtLocation(): IPopWindow {
        control?.showAtLocation()
        return this
    }

    private class Control(private val context: Context?) {
        companion object {
            const val TYPE_BOTTOM_SHEET_DIALOG = 0X00
            const val TYPE_POPUP_WINDOW = 0X01
        }

        private var type: Int = TYPE_BOTTOM_SHEET_DIALOG
        private var bottomSheetDialog: SpringBackBottomSheetDialog? = null
        private var pop: PopupWindow? = null
        private var lp: WindowManager.LayoutParams? = null

        init {
            when (type) {
                TYPE_BOTTOM_SHEET_DIALOG -> {
                    iniBottomSheetDialog()
                }
                TYPE_POPUP_WINDOW -> {
                    initPopWindow()
                }
            }
        }

        private fun iniBottomSheetDialog() {
            bottomSheetDialog = SpringBackBottomSheetDialog(context!!)


        }

        private fun initPopWindow() {
            pop = PopupWindow()
            isOutsideTouchable(true)
            isFocusable(true)
            setSize(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBkTransparent()
        }

        private fun setSize(width: Int, height: Int) {
            when (type) {
                TYPE_BOTTOM_SHEET_DIALOG -> {

                }
                TYPE_POPUP_WINDOW -> {
                    pop?.width = width
                    pop?.height = height
                }
            }

        }

        private fun setBkTransparent() {
            //实例化一个ColorDrawable颜色为半透明,设置SelectPicPopupWindow弹出窗体的背景
            pop?.setBackgroundDrawable(ColorDrawable(0x00000000))
        }

        private fun isOutsideTouchable(flag: Boolean) {
            pop?.isOutsideTouchable = flag
            //bottomSheetDialog?.setCanceledOnTouchOutside(false)
        }

        private fun isFocusable(flag: Boolean) {
            pop?.isFocusable = flag
        }

        fun showAtLocation() {
            when (type) {
                TYPE_BOTTOM_SHEET_DIALOG -> {
                    bottomSheetDialog?.addSpringBackDisLimit(-1)
                    bottomSheetDialog?.show()

                    //以下设置是为了解决：下滑隐藏dialog后，再次调用show方法显示时，不能弹出Dialog----在真机测试时不写下面的方法也未发现问题
                    val delegateView = bottomSheetDialog?.delegate?.findViewById<View>(android.support.design.R.id.design_bottom_sheet)
                    if (delegateView != null) {
                        val sheetBehavior = BottomSheetBehavior.from<View>(delegateView)
                        sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                            //在下滑隐藏结束时才会触发
                            override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
                                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                                    bottomSheetDialog?.dismiss()
                                    sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                                }
                            }

                            //每次滑动都会触发
                            override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {
                                println("onSlide = [$bottomSheet], slideOffset = [$slideOffset]")
                            }
                        })
                        sheetBehavior.isHideable = true
                        sheetBehavior.skipCollapsed = true
                        sheetBehavior.peekHeight = 500
                    }
                }
                TYPE_POPUP_WINDOW -> {
                    //设置弹出框动画
                    pop?.animationStyle = R.style.pop_ani

                    //窗口背景灰显
                    setWindowBkAlpha(0.3f)
                    pop?.setOnDismissListener {
                        //窗口透明
                        setWindowBkAlpha(1.0f)
                    }
                    // val gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                    val gravity = Gravity.CENTER
                    pop?.showAtLocation(getWindow().decorView, gravity, 0, 0)
                }
            }
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

        fun setContentView(view: View) {
            when (type) {
                TYPE_BOTTOM_SHEET_DIALOG -> {
                    bottomSheetDialog?.setContentView(view)
                }
                TYPE_POPUP_WINDOW -> {
                    pop?.contentView = view
                }
            }

        }
    }
}
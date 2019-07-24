package com.xm.lib.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

/**
 * 状态页面管理
 * 1 加载【鱼骨架，图片，圆圈加载】
 * 2 错误
 * 3 空数据
 * 4 隐藏
 */
class XmStateView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs), IXmStateView {

    private var progressCircular: ProgressBar? = null
    private var ivIcon: ImageView? = null
    private var tvDes: TextView? = null
    private var view: View? = null
    private var btnReplay: Button? = null

    init {
        view = LayoutInflater.from(context).inflate(R.layout.view_state, this, false)
        progressCircular = view?.findViewById(R.id.progress_circular)
        ivIcon = view?.findViewById(R.id.iv_icon)
        tvDes = view?.findViewById(R.id.tv_des)
        btnReplay = view?.findViewById(R.id.btn_replay)
        addView(view, LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        h(this)
    }

    override fun showLoading(loading: String) {
//        this.visibility = View.VISIBLE
//        progressCircular?.visibility = View.VISIBLE
//        ivIcon?.visibility = View.GONE
//        btnReplay?.visibility = View.GONE
//        tvDes?.text = loading
        s(this)
        s(progressCircular)
        h(ivIcon)
        h(btnReplay)
        setResAndTip(-1, loading)
    }

    override fun showError(error: String, listener: OnClickListener) {
//        this.visibility = View.VISIBLE
//        progressCircular?.visibility = View.GONE
//        ivIcon?.visibility = View.VISIBLE
//        btnReplay?.visibility = View.VISIBLE
        s(this)
        h(progressCircular)
        s(ivIcon)
        s(btnReplay)
        hide()

        setResAndTip(R.mipmap.common_warn, error)
        btnReplay?.setOnClickListener(listener)
    }


    override fun showNoData(noData: String) {
//        this.visibility = View.VISIBLE
//        progressCircular?.visibility = View.GONE
//        ivIcon?.visibility = View.VISIBLE
//        btnReplay?.visibility = View.GONE
//        ivIcon?.setImageResource(R.mipmap.common_empty)
//        tvDes?.text = noData
        s(this)
        h(progressCircular)
        s(ivIcon)
        h(btnReplay)
        setResAndTip(R.mipmap.common_empty, noData)

    }

    override fun hide() {
        //this.visibility = View.GONE
        h(this)
    }

    private fun setResAndTip(resId: Int = -1, error: String) {
        if (resId != -1) {
            ivIcon?.setImageResource(resId)
        }
        tvDes?.text = error
    }

    private fun s(view: View?) {
        if (view?.visibility == View.GONE) {
            view.visibility = View.VISIBLE
        }
    }

    private fun h(view: View?) {
        if (view?.visibility == View.VISIBLE) {
            view.visibility = View.GONE
        }
    }
}

/**
 * 接口
 */
interface IXmStateView {

    /**
     * 显示加载状态
     */
    fun showLoading(loading: String)

    /**
     * 显示错误状态
     */
    fun showError(error: String, listener: View.OnClickListener)

    /**
     * 显示空数据状态
     */
    fun showNoData(noData: String)

    /**
     * 隐藏
     */
    fun hide()
}
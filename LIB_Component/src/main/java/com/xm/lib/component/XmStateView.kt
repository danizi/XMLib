package com.xm.lib.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*

/**
 * 状态页面管理
 */
class XmStateView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var progressCircular: ProgressBar? = null
    private var ivIcon: ImageView? = null
    private var tvDes: TextView? = null
    private var view: View? = null
    private var btnReplay: Button? = null

    init {
        view = LayoutInflater.from(context).inflate(R.layout.view_state, null, false)
        progressCircular = view?.findViewById(R.id.progress_circular)
        ivIcon = view?.findViewById(R.id.iv_icon)
        tvDes = view?.findViewById(R.id.tv_des)
        btnReplay = view?.findViewById(R.id.btn_replay)
        addView(view)
        this.visibility = View.GONE
    }

    fun showLoading(loading: String) {
        this.visibility = View.VISIBLE
        progressCircular?.visibility = View.VISIBLE
        ivIcon?.visibility = View.GONE
        btnReplay?.visibility = View.GONE
        tvDes?.text = loading
    }

    fun showError(error: String, listener: OnClickListener) {
        this.visibility = View.VISIBLE
        progressCircular?.visibility = View.GONE
        ivIcon?.visibility = View.VISIBLE
        btnReplay?.visibility = View.VISIBLE
        ivIcon?.setImageResource(R.mipmap.common_warn)
        tvDes?.text = error
        btnReplay?.setOnClickListener(listener)
    }

    fun showNoData(nodata: String) {
        this.visibility = View.VISIBLE
        progressCircular?.visibility = View.GONE
        ivIcon?.visibility = View.VISIBLE
        btnReplay?.visibility = View.GONE
        ivIcon?.setImageResource(R.mipmap.common_empty)
        tvDes?.text = nodata
    }

    fun hide() {
        this.visibility = View.GONE
    }
}
package com.xm.lib.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.xm.lib.common.log.BKLog
import com.xm.lib.component.XmIOSDialog.ViewHolder.clEnter
import com.xm.lib.component.XmIOSDialog.ViewHolder.clEnterCancel
import com.xm.lib.component.XmIOSDialog.ViewHolder.findViews
import com.xm.lib.component.XmIOSDialog.ViewHolder.tvCancel
import com.xm.lib.component.XmIOSDialog.ViewHolder.tvEnter
import com.xm.lib.component.XmIOSDialog.ViewHolder.tvEnter2
import com.xm.lib.component.XmIOSDialog.ViewHolder.tvMsg
import com.xm.lib.component.XmIOSDialog.ViewHolder.tvTitle

/**
 * 各种类型弹框
 */
open class XmIOSDialog(var ctx: Context?) {

    private var width = 0
    private var height = 0
    private var cancelable = true
    private var layoutId = 0
    private var bind: OnBind? = null
    private var type: Type? = Type.GENERAL

    fun setSize(w: Int, h: Int): XmIOSDialog {
        this.width = w
        this.height = h
        return this
    }

    fun setType(type: Type): XmIOSDialog {
        this.type = type
        return this
    }

    fun setLayoutId(id: Int): XmIOSDialog {
        this.layoutId = id
        return this
    }

    fun setOnBind(bind: OnBind): XmIOSDialog {
        this.bind = bind
        return this
    }

    fun show() {

    }

    fun dismiss() {

    }

    fun setCancelable(cancelable: Boolean): XmIOSDialog {
        this.cancelable = cancelable
        return this
    }

    /**
     * 特有的
     */
    var builder: AlertDialog.Builder? = null
    private var dlg: AlertDialog? = null
    private var enterListener: OnEnterListener? = null
    private var cancelListener: OnCancelListener? = null
    private var title = ""
    private var msg = ""

    fun setTitle(title: String): XmIOSDialog {
        this.title = title
        return this
    }

    fun setMsg(msg: String): XmIOSDialog {
        this.msg = msg
        return this
    }

    fun setOnEnterListener(listener: OnEnterListener?): XmIOSDialog {
        this.enterListener = listener
        return this
    }

    fun setOnCancelListener(listener: OnCancelListener?): XmIOSDialog {
        this.cancelListener = listener
        return this
    }

    @SuppressLint("InflateParams")
    fun build(): AlertDialog {

        when (type) {
            Type.GENERAL -> {
                val view = LayoutInflater.from(ctx)?.inflate(R.layout.view_dlg, null, false)
                if (view != null) {
                    bind?.onBind(view)
                }
                builder = AlertDialog.Builder(ctx!!)
                builder?.setView(view)
                builder?.setCancelable(cancelable)
                findViews(view)
                //显示标题
                if (!TextUtils.isEmpty(title)) {
                    tvTitle?.visibility = View.VISIBLE
                }

                //显示内容
                if (!TextUtils.isEmpty(msg)) {
                    tvMsg?.text = msg
                }

                //显示确认取消按钮
                if (enterListener != null && cancelListener != null) {
                    clEnterCancel?.visibility = View.VISIBLE
                    tvEnter?.setOnClickListener {
                        enterListener?.onEnter(dlg!!)
                    }
                    tvCancel?.setOnClickListener {
                        cancelListener?.onCancel(dlg!!)
                    }
                } else if (enterListener != null) {
                    //显示确认按钮
                    clEnter?.visibility = View.VISIBLE
                    tvEnter2?.setOnClickListener {
                        enterListener?.onEnter(dlg!!)
                    }
                }
                dlg = builder?.create()!!
                dlg?.setOnShowListener {
                    //设置窗口透明，保证圆角显示出来
                    BKLog.d("窗体的宽高：$width $height")
                    dlg?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dlg?.window?.setLayout(width, height)
                }

            }
            Type.TOAST -> {

            }
            Type.LOADING -> {
                val view = LayoutInflater.from(ctx)?.inflate(layoutId, null, false)
                if (view != null) {
                    bind?.onBind(view)
                }
                view?.findViewById<TextView>(R.id.tv_msg)?.text = msg
                builder = AlertDialog.Builder(ctx!!)
                builder?.setView(view)
                //builder?.setCancelable(cancelable)
                dlg = builder?.create()!!
                dlg?.setOnShowListener {
                    //设置窗口透明，保证圆角显示出来
                    dlg?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dlg?.window?.setLayout(width, height)
                }
                dlg?.setCanceledOnTouchOutside(false)
            }
            Type.POP -> {

            }
        }
        return dlg!!
    }

    @SuppressLint("StaticFieldLeak")
    object ViewHolder {
        var tvTitle: TextView? = null
        var tvMsg: TextView? = null
        var clEnterCancel: ConstraintLayout? = null
        var tvCancel: TextView? = null
        var tvEnter: TextView? = null
        var clEnter: ConstraintLayout? = null
        var tvEnter2: TextView? = null

        fun findViews(view: View?) {
            tvTitle = view?.findViewById(R.id.tv_title)
            tvMsg = view?.findViewById(R.id.tv_msg)
            clEnterCancel = view?.findViewById(R.id.cl_enter_cancel)
            tvCancel = view?.findViewById(R.id.tv_cancel)
            tvEnter = view?.findViewById(R.id.tv_enter)
            clEnter = view?.findViewById(R.id.cl_enter)
            tvEnter2 = view?.findViewById(R.id.tv_enter_2)
        }

        fun handle() {

        }
    }
}

enum class Type {
    GENERAL,    //普通对话框
    TOAST,      //过一秒消失
    LOADING,    //加载对话框
    POP         //弹出对话框
}

interface OnBind {
    fun onBind(view: View)
}

interface OnEnterListener {
    fun onEnter(dlg: AlertDialog)
}

interface OnCancelListener {
    fun onCancel(dlg: AlertDialog)
}

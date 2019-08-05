package com.xm.lib.component.tip.dlg.core

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.xm.lib.component.R

/**
 * 相关接口
 */
interface XmDialogInterface {

    fun setOnDismissListener(listener: XmDialogInterface.OnDismissListener)

    fun setOnShowListener(listener: XmDialogInterface.OnShowListener)

    fun setOnCancelListener(listener: XmDialogInterface.OnCancelListener)

    interface OnCancelListener {
        fun onCancel(dialog: XmDialogInterface)
    }

    interface OnDismissListener {
        fun onDismiss(dialog: XmDialogInterface)
    }

    interface OnShowListener {
        fun onShow(dialog: XmDialogInterface)
    }

    interface OnClickListener {
        fun onClick(dialog: XmDialogInterface, which: Int)
    }

    interface OnMultiChoiceClickListener {
        fun onClick(dialog: XmDialogInterface, which: Int, isChecked: Boolean)
    }


    class Control(private val xmDialogInterface: XmDialogInterface, val context: Context?) {
        private var builder: AlertDialog.Builder? = null
        private var dlg: AlertDialog? = null
        private var containerView: View? = null
        private var titleContainer: FrameLayout? = null
        private var contentContainer: FrameLayout? = null

        private var progressBar: ProgressBar? = null
        private var progressDes: TextView? = null

        private var btnContainer: LinearLayout? = null
        private var btnView: View? = null
        private var btn1: TextView? = null
        private var divider1: View? = null
        private var btn2: TextView? = null
        private var divider2: View? = null
        private var btn3: TextView? = null
        private var btnClickListener = HashMap<Int, XmDialogInterface.OnClickListener>()
        private var isLoad = false

        private var cancelListener: OnCancelListener? = null
        private var showListener: OnShowListener? = null
        private var dismissListener: OnDismissListener? = null

        init {
            builder = AlertDialog.Builder(this.context!!)

        }

        fun show() {
            if (isLoad) {
                titleContainer?.visibility = View.GONE
            }
            dlg = builder?.create()

            dlg?.setOnCancelListener {
                cancelListener?.onCancel(xmDialogInterface)
            }
            dlg?.setOnShowListener {
                //设置窗口透明，保证圆角显示出来
                if (isLoad) {
                    dlg?.window?.setDimAmount(0f)  //蒙版透明
                    dlg?.window?.setLayout(300, 300) //等待框的大小
                } else {
                    dlg?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dlg?.window?.setLayout(800, ViewGroup.LayoutParams.WRAP_CONTENT) //对话框的大小
                }
                showListener?.onShow(xmDialogInterface)
            }
            dlg?.setOnDismissListener {
                dismissListener?.onDismiss(xmDialogInterface)
            }
            dlg?.show()
        }


        private fun setContentView(dlgLayoutID: Int) {
            containerView = LayoutInflater.from(context).inflate(dlgLayoutID, null)
            titleContainer = containerView?.findViewById(R.id.title_container)
            contentContainer = containerView?.findViewById(R.id.content_container)
            btnContainer = containerView?.findViewById(R.id.btn_contain)
            builder?.setView(containerView)
        }

        private fun setContentView(view: View) {
            containerView = view
            builder?.setView(containerView)
        }

        private fun setProgress(progressLayoutID: Int) {
            val progressView = LayoutInflater.from(context).inflate(progressLayoutID, null)
            progressBar = progressView.findViewById(R.id.progress)
            progressDes = progressView.findViewById(R.id.progress_des)
            contentContainer?.removeAllViews()
            contentContainer?.addView(progressView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        }

        private fun setLoading(loadingLayoutID: Int) {
            isLoad = true
            val loadingView = LayoutInflater.from(context).inflate(loadingLayoutID, null)
            val loading = loadingView.findViewById<ProgressBar>(R.id.loading)
            contentContainer?.removeAllViews()
            contentContainer?.addView(loadingView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
            contentContainer?.visibility = View.VISIBLE
        }

        private fun setListItem(listItems: Array<String>?, listLayoutID: Int) {
            val listView = LayoutInflater.from(context).inflate(listLayoutID, null)
            val rv = listView.findViewById<RecyclerView>(R.id.rv)
            contentContainer?.removeAllViews()
            contentContainer?.addView(listView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
            contentContainer?.visibility = View.VISIBLE
        }

        private fun setMessage(msg: String?, msgLayoutID: Int) {
            //val msgView = LayoutInflater.from(context).inflate(msgLayoutID, null)
            val msgTv = TextView(context)
            msgTv.text = msg
            contentContainer?.removeAllViews()
            contentContainer?.addView(msgTv, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
            contentContainer?.visibility = View.VISIBLE
        }

        private fun setInput(inputLayoutID: Int) {
            val inputView = LayoutInflater.from(context).inflate(inputLayoutID, null)
            val et = inputView.findViewById<EditText>(R.id.et)
            et.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

            })
            contentContainer?.removeAllViews()
            contentContainer?.addView(inputView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
            contentContainer?.visibility = View.VISIBLE
        }

        @SuppressLint("ResourceAsColor")
        private fun setTitle(title: String?, titleLayoutID: Int) {
            //val titleView = LayoutInflater.from(context).inflate(titleLayoutID, null)
            val titleView = TextView(context)
            titleContainer?.removeAllViews()
            titleContainer?.addView(titleView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
            titleContainer?.visibility = View.VISIBLE

            //设置样式
            titleView.text = title
            titleView.textSize = 18f
            titleView.setTextColor(R.color.textPrimary)
            val lp = RelativeLayout.LayoutParams(titleView.layoutParams)
            lp.setMargins(0, 200, 0, 0)
            lp.addRule(RelativeLayout.CENTER_IN_PARENT)

            titleView.layoutParams.width = 500

        }

        private fun hideTitleContainer() {
            titleContainer?.visibility = View.GONE
        }

        private fun hideButtonContainer() {
            btnContainer?.visibility = View.GONE
        }

        private fun setButton(i: Int, positiveBtn: String?, positiveListener: XmDialogInterface.OnClickListener?) {
            if (btnView == null) {
                btnView = LayoutInflater.from(context).inflate(R.layout.view_ios_btn, null)
                btn1 = btnView?.findViewById(R.id.btn1) as TextView
                divider1 = btnView?.findViewById(R.id.divider1) as View
                btn2 = btnView?.findViewById(R.id.btn2) as TextView
                divider2 = btnView?.findViewById(R.id.divider2) as View
                btn3 = btnView?.findViewById(R.id.btn3) as TextView
                btn1?.visibility = View.GONE
                btn2?.visibility = View.GONE
                btn3?.visibility = View.GONE
                divider1?.visibility = View.GONE
                divider2?.visibility = View.GONE
                btn1?.setOnClickListener {
                    btnClickListener[0]?.onClick(xmDialogInterface, 0)
                    dlg?.dismiss()
                }
                btn2?.setOnClickListener {
                    btnClickListener[1]?.onClick(xmDialogInterface, 1)
                    dlg?.dismiss()
                }
                btn3?.setOnClickListener {
                    btnClickListener[2]?.onClick(xmDialogInterface, 2)
                    dlg?.dismiss()
                }

                btnContainer?.removeAllViews()
                btnContainer?.addView(btnView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

            }
            when (i) {
                0 -> {
                    btn1?.text = positiveBtn
                    btn1?.visibility = View.VISIBLE
                }
                1 -> {
                    btn2?.text = positiveBtn
                    divider1?.visibility = View.VISIBLE
                    btn2?.visibility = View.VISIBLE
                }
                2 -> {
                    btn3?.text = positiveBtn
                    divider2?.visibility = View.VISIBLE
                    btn3?.visibility = View.VISIBLE
                }
            }
            btnClickListener[i] = positiveListener!!
        }

        private fun setOnCancelListener(cancelListener: XmDialogInterface.OnCancelListener?) {
            this.cancelListener = cancelListener
        }

        private fun setOnShowListener(showListener: XmDialogInterface.OnShowListener?) {
            this.showListener = showListener
        }

        private fun setOnDismissListener(dismissListener: XmDialogInterface.OnDismissListener?) {
            this.dismissListener = dismissListener
        }

        @SuppressLint("SetTextI18n")
        fun setProgressValue(progressValue: Int?) {
            progressBar?.progress = progressValue!!
            progressDes?.post {
                progressDes?.text = (progressValue * 100 / progressBar?.max!!).toString() + "%"
            }
        }

        private fun setMax(progressMax: Int?) {
            progressBar?.max = progressMax!!
        }

        /**
         * 相关参数
         */
        class P(xmDialogInterface: XmDialogInterface, context: Context?) {

            var isLoading: Boolean = false
            var iconId: Int = -1
            var title: String = ""
            var message: String = ""
            var setView: View? = null

            private var positiveBtn: String? = ""
            private var neutralBtn: String? = ""
            private var negativeBtn: String? = ""

            private var positiveListener: XmDialogInterface.OnClickListener? = null
            private var neutralListener: XmDialogInterface.OnClickListener? = null
            private var negativeListener: XmDialogInterface.OnClickListener? = null
            private var listListener: XmDialogInterface.OnClickListener? = null
            private var multiChoiceListener: XmDialogInterface.OnMultiChoiceClickListener? = null
            private var dismissListener: XmDialogInterface.OnDismissListener? = null
            private var showListener: XmDialogInterface.OnShowListener? = null
            private var cancelListener: XmDialogInterface.OnCancelListener? = null
            private var singleListener: XmDialogInterface.OnClickListener? = null

            var indeterminate: Boolean? = false
            var cancelable: Boolean? = false
            var progressValue: Int? = 0
            var progressStyle: Int? = 0
            var progressMax: Int? = 0

            /**
             * 容器页面
             */
            private var dlgLayoutID = R.layout.view_ios_dlg
            private var listLayoutID = R.layout.view_ios_content_list
            private var progressLayoutID = R.layout.view_ios_content_progress
            private var loadingLayoutID = R.layout.view_ios_content_loading
            private var inputLayoutID = R.layout.view_ios_content_edit
            private var multiLayoutID = R.layout.view_dlg
            private var singleLayoutID = R.layout.view_dlg
            private var msgLayoutID = R.layout.view_dlg
            private var titleLayoutID = R.layout.view_dlg

            private var listItems: Array<String>? = null
            private var multiChoiceItems: Array<String>? = null
            private var multiChoiceCheckedItems: BooleanArray? = null
            private var singleItems: Array<String>? = null

            fun apply(dialog: Control?) {

                //设置页面容器
                dialog?.setContentView(dlgLayoutID)

                //设置标题
                if (TextUtils.isEmpty(title)) {
                    dialog?.hideTitleContainer() //隐藏标题栏容器
                } else {
                    dialog?.setTitle(title, titleLayoutID)
                }

                //设置内容 编辑框 / 文字 / 列表(单选 多选 复选) / 加载 / 等待
                if (setView != null) {
                    if (setView is EditText) {
                        dialog?.setInput(inputLayoutID)
                    } else {
                        dialog?.setContentView(setView!!)
                    }
                } else {
                    //文字
                    if (!TextUtils.isEmpty(message)) {
                        dialog?.setMessage(message, msgLayoutID)
                    } else if (listItems != null && listItems?.isNotEmpty()!!) {
                        //列表
                        dialog?.setListItem(listItems!!, listLayoutID)
                    } else if (progressMax == 0) {
                        //加载等待
                        dialog?.setLoading(loadingLayoutID)
                    } else {
                        //进度
                        dialog?.setProgress(progressLayoutID)
                        dialog?.setMax(progressMax)
                        dialog?.setProgressValue(progressValue)
                    }
                }

                //设置按钮
                if ((!TextUtils.isEmpty(positiveBtn) && positiveListener != null)
                        && (!TextUtils.isEmpty(negativeBtn) && negativeListener != null)
                        && !TextUtils.isEmpty(neutralBtn) && neutralListener != null) {
                    //三个按钮
                    dialog?.setButton(0, positiveBtn, positiveListener)
                    dialog?.setButton(1, negativeBtn, negativeListener)
                    dialog?.setButton(2, neutralBtn, neutralListener)

                } else if ((!TextUtils.isEmpty(positiveBtn) && positiveListener != null)
                        && (!TextUtils.isEmpty(negativeBtn) && negativeListener != null)) {
                    //两个按钮
                    dialog?.setButton(0, positiveBtn, positiveListener)
                    dialog?.setButton(1, negativeBtn, negativeListener)

                } else if ((!TextUtils.isEmpty(positiveBtn) && positiveListener != null)) {
                    //一个按钮
                    dialog?.setButton(0, positiveBtn, positiveListener)

                } else {
                    //隐藏所有按钮
                    dialog?.hideButtonContainer()
                }

                // 设置监听
                dialog?.setOnDismissListener(dismissListener)
                dialog?.setOnShowListener(showListener)
                dialog?.setOnCancelListener(cancelListener)
            }

            fun setPositiveButton(btn: String?, listener: XmDialogInterface.OnClickListener?) {
                positiveBtn = btn
                positiveListener = listener
            }

            fun setNeutralButton(btn: String?, listener: XmDialogInterface.OnClickListener?) {
                neutralBtn = btn
                neutralListener = listener
            }

            fun setNegativeButton(btn: String?, listener: XmDialogInterface.OnClickListener?) {
                negativeBtn = btn
                negativeListener = listener
            }

            fun setItems(items: Array<String>, listener: XmDialogInterface.OnClickListener?) {
                listItems = items
                listListener = listener
            }

            fun setSingleChoiceItems(items: Array<String>, listener: XmDialogInterface.OnClickListener?) {
                singleItems = items
                singleListener = listener
            }

            fun setMultiChoiceItems(items: Array<String>, checkedItems: BooleanArray, listener: XmDialogInterface.OnMultiChoiceClickListener?) {
                multiChoiceItems = items
                multiChoiceCheckedItems = checkedItems
                multiChoiceListener = listener
            }

            fun setOnDismissListener(listener: XmDialogInterface.OnDismissListener) {
                dismissListener = listener
            }

            fun setOnShowListener(listener: XmDialogInterface.OnShowListener) {
                showListener = listener
            }

            fun setOnCancelListener(listener: XmDialogInterface.OnCancelListener) {
                cancelListener = listener
            }
        }
    }
}
package com.xm.lib.component.tip.dlg

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import com.xm.lib.component.R
import com.xm.lib.component.tip.dlg.core.IXmDialog
import com.xm.lib.component.tip.dlg.core.XmDialogInterface


/**
 * 苹果风格对话框
 */
class XmIOSDlg(context: Context?) : IXmDialog {

    private var p: Control.P? = null
    private var control: Control? = null

    init {
        control = Control(this, context)
        p = Control.P(this, context)
    }

    override fun setIcon(id: Int): IXmDialog {
        p?.iconId = id
        return this
    }

    override fun setTitle(title: String): IXmDialog {
        p?.title = title
        return this
    }

    override fun setMessage(msg: String): IXmDialog {
        p?.message = msg
        return this
    }

    override fun setPositiveButton(btn: String?, listener: XmDialogInterface.OnClickListener?): IXmDialog {
        p?.setPositiveButton(btn, listener)
        return this
    }

    override fun setNeutralButton(btn: String?, listener: XmDialogInterface.OnClickListener?): IXmDialog {
        p?.setNeutralButton(btn, listener)
        return this
    }

    override fun setNegativeButton(btn: String?, listener: XmDialogInterface.OnClickListener?): IXmDialog {
        p?.setNegativeButton(btn, listener)
        return this
    }

    override fun setItems(items: Array<String>, listener: XmDialogInterface.OnClickListener?): IXmDialog {
        p?.setItems(items, listener)
        return this
    }

    override fun setSingleChoiceItems(items: Array<String>, listener: XmDialogInterface.OnClickListener?): IXmDialog {
        p?.setSingleChoiceItems(items, listener)
        return this
    }

    override fun setMultiChoiceItems(items: Array<String>, checkedItems: BooleanArray, listener: XmDialogInterface.OnMultiChoiceClickListener?): IXmDialog {
        p?.setMultiChoiceItems(items, checkedItems, listener)
        return this
    }

    override fun setView(view: View?): IXmDialog {
        p?.setView = view
        return this
    }

    override fun show(): IXmDialog {
        p?.apply(control)
        control?.show()
        return this
    }

    override fun setOnDismissListener(listener: XmDialogInterface.OnDismissListener) {
        p?.setOnDismissListener(listener)
    }

    override fun setOnShowListener(listener: XmDialogInterface.OnShowListener) {
        p?.setOnShowListener(listener)
    }

    override fun setOnCancelListener(listener: XmDialogInterface.OnCancelListener) {
        p?.setOnCancelListener(listener)
    }

    class Control(private val xmDialogInterface: XmDialogInterface, val context: Context?) {
        private var builder: AlertDialog.Builder? = null
        private var containerView: View? = null
        private var titleContainer: FrameLayout? = null
        private var contentContainer: FrameLayout? = null
        private var btnContainer: LinearLayout? = null

        private var isload = false

        fun show() {
            if (isload) {
                titleContainer?.visibility = View.GONE
            }
            val dlg = builder?.create()
            dlg?.setOnCancelListener {

            }
            dlg?.setOnShowListener {

            }
            dlg?.setOnDismissListener {

            }
            dlg?.show()
        }

        init {
            builder = AlertDialog.Builder(this.context!!)
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
            contentContainer?.removeAllViews()
            contentContainer?.addView(progressView, ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        }

        private fun setLoading(loadingLayoutID: Int) {
            isload = true
            val loadingView = LayoutInflater.from(context).inflate(loadingLayoutID, null)
            val loading = loadingView.findViewById<ProgressBar>(R.id.loading)
            contentContainer?.removeAllViews()
            contentContainer?.addView(loadingView, ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
            contentContainer?.visibility = View.VISIBLE
        }

        private fun setListItem(listItems: Array<String>?, listLayoutID: Int) {
            val listView = LayoutInflater.from(context).inflate(listLayoutID, null)
            val rv = listView.findViewById<RecyclerView>(R.id.rv)
            contentContainer?.removeAllViews()
            contentContainer?.addView(listView, ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
            contentContainer?.visibility = View.VISIBLE
        }

        private fun setMessage(msg: String?, msgLayoutID: Int) {
            //val msgView = LayoutInflater.from(context).inflate(msgLayoutID, null)
            val msgTv = TextView(context)
            msgTv.text = msg
            contentContainer?.removeAllViews()
            contentContainer?.addView(msgTv, ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
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
            contentContainer?.addView(inputView, ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
            contentContainer?.visibility = View.VISIBLE
        }

        @SuppressLint("ResourceAsColor")
        private fun setTitle(title: String?, titleLayoutID: Int) {
            //val titleView = LayoutInflater.from(context).inflate(titleLayoutID, null)
            val titleView = TextView(context)
            titleView.text = title
            titleView.textSize = 18f
            titleView.setTextColor(R.color.textPrimary)
            titleContainer?.removeAllViews()

            titleContainer?.addView(titleView)
            titleContainer?.visibility = View.VISIBLE
        }

        private fun hideTitleContainer() {
            titleContainer?.visibility = View.GONE
        }

        private fun hideButtonContainer() {
            btnContainer?.visibility = View.GONE
        }

        var btnView: View? = null
        private var btn1: TextView? = null
        private var divider1: View? = null
        private var btn2: TextView? = null
        private var divider2: View? = null
        private var btn3: TextView? = null
        private var btnClickListener = HashMap<Int, XmDialogInterface.OnClickListener>()


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
                }
                btn2?.setOnClickListener {
                    btnClickListener[1]?.onClick(xmDialogInterface, 1)
                }
                btn3?.setOnClickListener {
                    btnClickListener[2]?.onClick(xmDialogInterface, 2)
                }

                btnContainer?.removeAllViews()
                btnContainer?.addView(btnView, ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT))

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
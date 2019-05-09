package com.xm.lib.component.test

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.xm.lib.common.log.BKLog
import com.xm.lib.component.*


class DialogActivity : AppCompatActivity() {

    private class ViewHolder private constructor(val btnGeneral: Button, val btnToast: Button, val btnLoading: Button, val btnPop: Button) {
        companion object {

            fun create(rootView: AppCompatActivity): ViewHolder {
                val btnGeneral = rootView.findViewById<View>(R.id.btn_general) as Button
                val btnToast = rootView.findViewById<View>(R.id.btn_toast) as Button
                val btnLoading = rootView.findViewById<View>(R.id.btn_loading) as Button
                val btnPop = rootView.findViewById<View>(R.id.btn_pop) as Button
                return ViewHolder(btnGeneral, btnToast, btnLoading, btnPop)
            }
        }
    }

    private var viewHolder: ViewHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)

        viewHolder = ViewHolder.create(this)

        viewHolder?.btnGeneral?.setOnClickListener {
            XmIOSDialog(this)
                    .setType(Type.GENERAL)
                    .setMsg("提示信息")
                    .setTitle("标题")
                    .setCancelable(true)
                    .setSize(600,400)
                    .setOnBind(object : OnBind {
                        override fun onBind(view: View) {

                        }
                    })
                    .setOnEnterListener(object : OnEnterListener {
                        override fun onEnter(dlg: AlertDialog) {
                            BKLog.d("点击了确认")
                        }
                    })
                    .setOnCancelListener(object : OnCancelListener {
                        override fun onCancel(dlg: AlertDialog) {
                            BKLog.d("点击了取消")
                        }
                    })
                    .build()
                    .show()
        }

        viewHolder?.btnToast?.setOnClickListener {

        }

        viewHolder?.btnLoading?.setOnClickListener {

        }

        viewHolder?.btnPop?.setOnClickListener {

        }
    }
}

package com.xm.lib.component.test

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import com.xm.lib.component.R
import com.xm.lib.component.tip.dlg.core.CreateDialogType
import com.xm.lib.component.tip.dlg.core.DialogFactory
import com.xm.lib.component.tip.dlg.core.IDialog
import com.xm.lib.component.tip.dlg.core.IProgressDialog

/**
 * 弹出相关组件测试
 */
class TipActivity : AppCompatActivity() {

    /**
     * 界面
     */
    private var ui: TipActivityUI? = null

    /**
     * 对话框
     */
    private lateinit var dialog: IDialog

    /**
     * 进度对话框
     */
    private lateinit var progressDialog: IProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tip)

        if (ui == null) {
            ui = TipActivityUI.create(this)
        }
        iniEvent()
        iniData()
    }

    private fun iniEvent() {
        ui?.sc?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                show("IOS弹框风格")
                dialog = DialogFactory().getDialog(this, CreateDialogType.CUSTOM_IOS_DIALOG)!!
                progressDialog = DialogFactory().getProgressDialog(this, CreateDialogType.CUSTOM_IOS_PROGRESS_DIALOG)!!
            } else {
                show("原生弹框风格")
                dialog = DialogFactory().getDialog(this, CreateDialogType.NATIVE_ALERT_DIALOG)!!
                progressDialog = DialogFactory().getProgressDialog(this, CreateDialogType.NATIVE_PROGRESS_DIALOG)!!
            }
        }

        ui?.btnDlg1?.setOnClickListener {
            showDlg1()
        }
        ui?.btnDlg2?.setOnClickListener {
            showDlg2()
        }
        ui?.btnList?.setOnClickListener {
            showList()
        }
        ui?.btnSingle?.setOnClickListener {
            showSingle()
        }
        ui?.btnMultiple?.setOnClickListener {
            showMultiple()
        }
        ui?.btnWait?.setOnClickListener {
            showWait()
        }
        ui?.btnProgress?.setOnClickListener {
            showProgress()
        }
        ui?.btnInput?.setOnClickListener {
            showInput()
        }
        ui?.btnCus?.setOnClickListener {
            showCus()
        }
    }

    private fun iniData() {
        dialog = DialogFactory().getDialog(this, CreateDialogType.NATIVE_ALERT_DIALOG)!!
        progressDialog = DialogFactory().getProgressDialog(this, CreateDialogType.NATIVE_PROGRESS_DIALOG)!!
    }

    private fun show(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showDlg1() {
        dialog.setIcon(R.drawable.ic_launcher_background)
                .setTitle("标题")
                .setMessage("内容信息")
                .setNegativeButton()
                .setPositiveButton()
                .show()
    }

    private fun showDlg2() {
        dialog.setIcon(R.drawable.ic_launcher_background)
                .setTitle("标题")
                .setMessage("内容信息")
                .setNegativeButton()
                .setNeutralButton()
                .setPositiveButton()
                .show()
    }

    private fun showList() {

    }

    private fun showSingle() {
        dialog.setIcon(R.drawable.ic_launcher_background)
                .setTitle("标题")
                .setMessage("内容信息")
                .setSingleChoiceItems()
                .show()
    }

    private fun showMultiple() {
        dialog.setIcon(R.drawable.ic_launcher_background)
                .setTitle("标题")
                .setMessage("内容信息")
                .setMultiChoiceItems()
                .show()
    }

    private fun showWait() {
        progressDialog.setTitle("标题")
                .setCancelable(true)
                .show()
    }

    private fun showProgress() {
        progressDialog.setTitle("我是一个进度条Dialog")
                .setProgress(0)
                .setProgressStyle()
                .setMax(100)
                .show()
        Thread(Runnable {
            var value = 0
            while (true) {
                Thread.sleep(100)
                progressDialog.setProgress(value++)
            }
            progressDialog.cancel()
        }).start()
    }

    private fun showInput() {
        dialog.setTitle("title")
                .setMessage("msg")
                .setView(EditText(this))
                .show()
    }

    private fun showCus() {

    }

    private class TipActivityUI private constructor(val sc: Switch, val btnDlg1: Button, val btnDlg2: Button, val btnList: Button, val btnSingle: Button, val btnMultiple: Button, val btnWait: Button, val btnProgress: Button, val btnInput: Button, val btnCus: Button) {
        companion object {

            fun create(rootView: Activity): TipActivityUI {
                val sc = rootView.findViewById<View>(R.id.sc) as Switch
                val btnDlg1 = rootView.findViewById<View>(R.id.btn_dlg_1) as Button
                val btnDlg2 = rootView.findViewById<View>(R.id.btn_dlg_2) as Button
                val btnList = rootView.findViewById<View>(R.id.btn_list) as Button
                val btnSingle = rootView.findViewById<View>(R.id.btn_single) as Button
                val btnMultiple = rootView.findViewById<View>(R.id.btn_multiple) as Button
                val btnWait = rootView.findViewById<View>(R.id.btn_wait) as Button
                val btnProgress = rootView.findViewById<View>(R.id.btn_progress) as Button
                val btnInput = rootView.findViewById<View>(R.id.btn_input) as Button
                val btnCus = rootView.findViewById<View>(R.id.btn_cus) as Button
                return TipActivityUI(sc, btnDlg1, btnDlg2, btnList, btnSingle, btnMultiple, btnWait, btnProgress, btnInput, btnCus)
            }
        }
    }
}

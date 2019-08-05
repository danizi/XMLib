package com.xm.lib.component.test

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import com.xm.lib.common.log.BKLog
import com.xm.lib.component.R
import com.xm.lib.component.tip.dlg.XmNativeDlg
import com.xm.lib.component.tip.dlg.core.*

/**
 * 弹出相关组件测试
 */
class TipActivity : AppCompatActivity() {

    companion object {
        const val TAG = "TipActivity"
    }

    private var ui: TipActivityUI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tip)

        if (ui == null) {
            ui = TipActivityUI.create(this)
        }
        iniEvent()
    }

    private var isChecked: Boolean = false
    private fun iniEvent() {
        ui?.sc?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "IOS风格", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "原生", Toast.LENGTH_SHORT).show()
            }
            this.isChecked = isChecked
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
        ui?.btnInput?.setOnClickListener {
            showInput()
        }
        ui?.btnCus?.setOnClickListener {
            showCus()
        }
        ui?.btnWait?.setOnClickListener {
            showWait()
        }
        ui?.btnProgress?.setOnClickListener {
            showProgress()
        }
        ui?.btnAd?.setOnClickListener {
            showAd()
        }
    }

    private fun showAd() {
        val url="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565007865339&di=8b7d79cc6322b953cf20baffa95baf89&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fblog%2F201508%2F03%2F20150803082359_NTGB5.jpeg"
        val adDlg =    XmDialogFactory().getAdDialog(this)!!
        adDlg.setAd(url, object : XmDialogInterface.OnClickListener {
            override fun onClick(dialog: XmDialogInterface, which: Int) {
                BKLog.d(TAG, "点击广告")
                dialog.dismiss()
            }
        }).setClose(R.mipmap.ad_close, object : XmDialogInterface.OnClickListener {
            override fun onClick(dialog: XmDialogInterface, which: Int) {
                BKLog.d(TAG, "关闭广告")
                dialog.dismiss()
            }
        }).show()
    }

    private fun createDialog(): IXmDialog {
        var type = CreateDialogType.NATIVE_ALERT_DIALOG
        if (isChecked) {
            type = CreateDialogType.CUSTOM_IOS_DIALOG
        }
        return XmDialogFactory().getDialog(this, type)!!
    }

    private fun createProgressDialog(): IXmProgressDialog {
        var type = CreateDialogType.NATIVE_PROGRESS_DIALOG
        if (isChecked) {
            type = CreateDialogType.CUSTOM_IOS_PROGRESS_DIALOG
        }
        return XmDialogFactory().getProgressDialog(this, type)!!
    }

    private fun setDialogAllListener(dialog: XmDialogInterface) {
        dialog.setOnDismissListener(object : XmDialogInterface.OnDismissListener {
            override fun onDismiss(dialog: XmDialogInterface) {
                BKLog.d(TAG, "dialog onDismiss")
            }
        })
        dialog.setOnShowListener(object : XmDialogInterface.OnShowListener {
            override fun onShow(dialog: XmDialogInterface) {
                BKLog.d(TAG, "dialog onShow")
            }
        })
        dialog.setOnCancelListener(object : XmDialogInterface.OnCancelListener {
            override fun onCancel(dialog: XmDialogInterface) {
                BKLog.d(TAG, "dialog onCancel")
            }
        })
    }

    private fun showDlg1() {
        val dialog = createDialog()
        setDialogAllListener(dialog)
        dialog.setIcon(R.drawable.ic_launcher_background)
                .setTitle("标题")
                .setMessage("内容信息")
                .setPositiveButton("确认", object : XmDialogInterface.OnClickListener {
                    override fun onClick(dialog: XmDialogInterface, which: Int) {
                        BKLog.d(TAG, "点击确认")
                    }
                })
                .setNegativeButton("取消", object : XmDialogInterface.OnClickListener {
                    override fun onClick(dialog: XmDialogInterface, which: Int) {
                        BKLog.d(TAG, "点击取消")
                    }
                })
                .show()
    }

    private fun showDlg2() {
        val dialog = createDialog()
        dialog.setIcon(R.drawable.ic_launcher_background)
                .setTitle("标题")
                .setMessage("内容信息")
                .setPositiveButton("确认", object : XmDialogInterface.OnClickListener {
                    override fun onClick(dialog: XmDialogInterface, which: Int) {
                        BKLog.d(TAG, "点击确认")
                    }
                })
                .setNegativeButton("取消", object : XmDialogInterface.OnClickListener {
                    override fun onClick(dialog: XmDialogInterface, which: Int) {
                        BKLog.d(TAG, "点击取消")
                    }
                })
                .setNeutralButton("中立", object : XmDialogInterface.OnClickListener {
                    override fun onClick(dialog: XmDialogInterface, which: Int) {
                        BKLog.d(TAG, "点击中立")
                    }
                })
                .show()
        setDialogAllListener(dialog)
    }

    private fun showList() {
        val dialog = createDialog()
        val items = arrayOf("列表 1", "列表 2", "列表 3", "列表 4", "列表 5", "列表 6", "列表 7", "列表 8", "列表 9")
        dialog.setIcon(R.drawable.ic_launcher_background)
                .setTitle("列表")
                .setItems(items, object : XmDialogInterface.OnClickListener {
                    override fun onClick(dialog: XmDialogInterface, which: Int) {
                        BKLog.d(TAG, "点击列表 ${items[which]} which:$which ")
                    }
                })
                .show()
        setDialogAllListener(dialog)
    }

    private fun showSingle() {
        val dialog = createDialog()
        val items = arrayOf("单选1", "单选2", "单选3")
        dialog.setIcon(R.drawable.ic_launcher_background)
                .setTitle("标题")
                .setSingleChoiceItems(items, object : XmDialogInterface.OnClickListener {
                    override fun onClick(dialog: XmDialogInterface, which: Int) {
                        BKLog.d(TAG, "点击列表 ${items[which]} which:$which ")
                    }
                })
                .setPositiveButton()
                .show()
        setDialogAllListener(dialog)
    }

    private fun showMultiple() {
        val dialog = createDialog()
        val items = arrayOf("多选1", "多选2", "多选3")
        val checkedItems = booleanArrayOf(false, false, false)
        dialog.setIcon(R.drawable.ic_launcher_background)
                .setTitle("标题")
                .setMultiChoiceItems(items, checkedItems, object : XmDialogInterface.OnMultiChoiceClickListener {
                    override fun onClick(dialog: XmDialogInterface, which: Int, isChecked: Boolean) {
                        BKLog.d(TAG, "多选列表 ${items[which]} which:$which isChecked:$isChecked")
                    }
                })
                .setPositiveButton()
                .show()
        setDialogAllListener(dialog)
    }

    private fun showInput() {
        val dialog = createDialog()
        createDialog().setTitle("title")
                .setMessage("msg")
                .setView(EditText(this))
                .setPositiveButton()
                .show()
        setDialogAllListener(dialog)
    }

    @SuppressLint("InflateParams")
    private fun showCus() {
        val dialog = createDialog()
        dialog.setView(LayoutInflater.from(this).inflate(R.layout.activity_pop, null))
                .show()
        val alertDialog = (dialog as XmNativeDlg).alertDialog
        alertDialog?.setCanceledOnTouchOutside(true) // Sets whether this dialog is
        val w = alertDialog?.window
        val lp = w?.attributes
        lp?.width = 400
        lp?.height = 400
        lp?.x = 0
        lp?.y = 0
        alertDialog?.onWindowAttributesChanged(lp)

        setDialogAllListener(dialog)
    }

    private fun showWait() {
        val progressDialog = createProgressDialog()
        progressDialog.setTitle("标题")
                .setTitle("我是一个进度条Dialog")
                .setIndeterminate(true)
                .setCancelable(true)
                .show()
        setDialogAllListener(progressDialog)
    }

    private fun showProgress() {
        val progressDialog = createProgressDialog()
        progressDialog
                .setTitle("下载APK")
                .setProgress(0)
                .setCancelable(true)
                .setProgressStyle()
                .setMax(100)
                .show()
        Thread(Runnable {
            var value = 0
            while (value <= 100) {
                Thread.sleep(100)
                progressDialog.setProgress(value++)
            }
            progressDialog.cancel()
        }).start()
        setDialogAllListener(progressDialog)
    }

    private class TipActivityUI private constructor(val sc: Switch, val btnDlg1: Button, val btnDlg2: Button, val btnList: Button, val btnSingle: Button, val btnMultiple: Button, val btnWait: Button, val btnProgress: Button, val btnInput: Button, val btnCus: Button, val btnAd: Button) {
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
                val btnAd = rootView.findViewById<View>(R.id.btn_ad) as Button
                return TipActivityUI(sc, btnDlg1, btnDlg2, btnList, btnSingle, btnMultiple, btnWait, btnProgress, btnInput, btnCus, btnAd)
            }
        }
    }
}

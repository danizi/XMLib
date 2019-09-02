package com.xm.lib.test.ui.act

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.ScreenUtil
import com.xm.lib.component.tip.dlg.ios.XmIOSDlg
import com.xm.lib.test.R
import com.xm.lib.test.utils.IntoTarget


/**
 *
 * Intent标志有：
 * FLAG_ACTIVITY_NEW_TASK
 * FLAG_ACTIVITY_CLEAR_TOP
 * FLAG_ACTIVITY_SINGLE_TOP
 *
 * <activity>中我们可以使用的属性如下：
 * taskAffinity
 * launchMode
 * allowTaskReparenting
 * clearTaskOnLaunch
 * alwaysRetainTaskState
 * finishOnTaskLaunch
 *
 * Activity 状态：
 * 可见 有焦点
 * 可见 无焦点
 * 不可见
 *
 * 改变状态的方式：
 * 状态的启动新的窗口组件，弹框，按键退出，横竖切屏。
 *
 * Activity 生命周期：
 * 7个
 *
 * 启动模式：
 *         四种启动方式
 *         xml 和代码方式
 *
 * 通讯
 */
class PrimaryActivity : AppCompatActivity() {

    companion object {
        const val TAG = "PrimaryActivityA"
    }

    private var ui: ViewHolder? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_primary)
        BKLog.d(TAG, "$TAG   onCreate()")
        //恢复数据
        if (savedInstanceState != null) {
            BKLog.d("恢复数据")
        }
        //查找Views
        if (ui == null) {
            ui = ViewHolder.create(this)
        }
        //初始化监听
        initEvent()
    }

    override fun onStart() {
        super.onStart()
        BKLog.d(TAG, "$TAG   onStart()")
    }

    override fun onStop() {
        super.onStop()
        BKLog.d(TAG, "$TAG   onStop()")
    }

    override fun onResume() {
        super.onResume()
        BKLog.d(TAG, "$TAG   onResume()")
    }

    override fun onPause() {
        super.onPause()
        BKLog.d(TAG, "$TAG   onPause()")
    }

    override fun onDestroy() {
        super.onDestroy()
        BKLog.d(TAG, "$TAG   onDestroy()")
    }

    override fun onRestart() {
        super.onRestart()
        BKLog.d(TAG, "$TAG   onRestart()")
    }

    /**
     * 被系统销毁时被调用
     */
    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        BKLog.d(TAG, "$TAG   onSaveInstanceState()")
    }

    /**
     * 只要窗口不设置configChanges和screenOrientation属性该重建函数就会被调用。
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        BKLog.d(TAG, "$TAG   onRestoreInstanceState()")
    }

    /**
     * 触发该方法，或者窗口不重建，在清单文件设置属性
     * android:configChanges="keyboardHidden|orientation|screenSize|navigation|keyboard"
     * 设置固定屏幕方向
     * android:screenOrientation="landscape"（landscape是横向，portrait是纵向）
     *
     * 注意：如果在清单文件设置上述属性，则onRestoreInstanceState不会被触发，反之onConfigurationChanged不触发
     * */
    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if (newConfig?.orientation == Configuration.ORIENTATION_PORTRAIT) {
            BKLog.d(TAG, "竖屏")
        }
        if (newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            BKLog.d(TAG, "横屏")
        }
    }

    /**
     * 触发该方法，即需要【窗口A】通过startActivityForResult(intent, requestCode)启动【窗口B】，并且窗口
     * B回传结果时调用setResult方法，这里值得【注意】的是调用的时机，必须是调用finish()之前。
     * @param requestCode 请求码
     * @param resultCode 返回结果码RESULT_OK
     * @param data 返回的数据
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val result = data?.getStringExtra("result")
            BKLog.d(TAG, "$TAG  requestCode : $requestCode resultCode : $resultCode result : $result")
        } else {
            BKLog.d(TAG, "$TAG   NO onActivityResult()")
        }
    }

    private fun initEvent() {
        ui?.btnStartBResult?.setOnClickListener {
            val requestCode = 0
            val intent = Intent(this, PrimaryActivityB::class.java)
            intent.putExtra("request", "PrimaryActivityA发出请求")
            startActivityForResult(intent, requestCode)
        }
        ui?.btnStartBNoResult?.setOnClickListener { IntoTarget.start(PrimaryActivityB::class.java.simpleName, this) }

        ui?.btnChangeLife1?.setOnClickListener { ScreenUtil.setLandscape(this) }
        ui?.btnChangeLife2?.setOnClickListener {
            XmIOSDlg(this)
                    .setMessage("提示")
                    .show()
        }

        ui?.btnStandard?.setOnClickListener { IntoTarget.start(PrimaryStandard::class.java.simpleName, this) }
        ui?.btnSingleTop?.setOnClickListener { IntoTarget.start(PrimarySingleTop::class.java.simpleName, this) }
        ui?.btnSingleTask?.setOnClickListener { IntoTarget.start(PrimarySingleTask::class.java.simpleName, this) }
        ui?.btnSingleInstance?.setOnClickListener { IntoTarget.start(PrimarySingleInstance::class.java.simpleName, this) }

        //启动的标志
        //属性
    }

    internal class ViewHolder(val btnStartBResult: Button, val btnStartBNoResult: Button, val btnChangeLife1: Button, val btnChangeLife2: Button, val btnStandard: Button, val btnSingleTop: Button, val btnSingleTask: Button, val btnSingleInstance: Button) {
        companion object {

            fun create(rootView: Activity): ViewHolder {
                val btnStartBResult = rootView.findViewById<View>(R.id.btn_start_b_result) as Button
                val btnStartBNoResult = rootView.findViewById<View>(R.id.btn_start_b_no_result) as Button
                val btnChangeLife1 = rootView.findViewById<View>(R.id.btn_change_life_1) as Button
                val btnChangeLife2 = rootView.findViewById<View>(R.id.btn_change_life_2) as Button
                val btnStandard = rootView.findViewById<View>(R.id.btn_standard) as Button
                val btnSingleTop = rootView.findViewById<View>(R.id.btn_single_top) as Button
                val btnSingleTask = rootView.findViewById<View>(R.id.btn_single_task) as Button
                val btnSingleInstance = rootView.findViewById<View>(R.id.btn_single_instance) as Button
                return ViewHolder(btnStartBResult, btnStartBNoResult, btnChangeLife1, btnChangeLife2, btnStandard, btnSingleTop, btnSingleTask, btnSingleInstance)
            }
        }
    }

}

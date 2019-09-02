package com.xm.lib.test.ui.act

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.xm.lib.common.log.BKLog
import com.xm.lib.test.R
import com.xm.lib.test.utils.IntoTarget

/**
 * 标准模式
 * 默认启动模式，只要是启动窗口就创建一个窗口实例。
 *
 * 返回移除栈中窗口实例
 *
 * 场景：打开新页面
 */
class PrimaryStandard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_primary_standard)

        (findViewById<Button>(R.id.btn_once)).setOnClickListener {
            IntoTarget.start(PrimaryStandard::class.java.simpleName, this)
        }
        (findViewById<Button>(R.id.btn_single_top)).setOnClickListener {
            IntoTarget.start(PrimarySingleTop::class.java.simpleName, this)
        }
        (findViewById<Button>(R.id.btn_single_task)).setOnClickListener {
            IntoTarget.start(PrimarySingleTask::class.java.simpleName, this)
        }
        BKLog.d("taskId : $taskId hashcode : ${hashCode()} PrimaryStandard : ${PrimaryStandard@ this}")
    }
}

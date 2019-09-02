package com.xm.lib.test.ui.act

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.xm.lib.common.log.BKLog
import com.xm.lib.test.R
import com.xm.lib.test.utils.IntoTarget

/**
 * 栈顶复用
 * 如果启动的窗口在栈顶，那么就不会创建窗口实例。
 * 如果启动的窗口不在栈顶，那么就会创建窗口实例。
 *
 * 返回移除栈中窗口实例
 *
 * 场景：防止多点创建多个实例，新闻端推送三个消息，进入详情页面
 */
class PrimarySingleTop : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_primary_single_top)

        (findViewById<Button>(R.id.btn_once)).setOnClickListener {
            IntoTarget.start(PrimarySingleTop::class.java.simpleName, this)
        }
        (findViewById<Button>(R.id.btn_standard)).setOnClickListener {
            IntoTarget.start(PrimaryStandard::class.java.simpleName, this)
        }
        BKLog.d("taskId : $taskId hashcode : ${hashCode()} PrimarySingleTop : ${PrimarySingleTop@ this}")
    }

    /**
     * 窗口实例被重用就会触发该方法
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        BKLog.d("onNewIntent taskId : $taskId hashcode : ${hashCode()} PrimarySingleTop : ${PrimarySingleTop@ this}")
    }
}

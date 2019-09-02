package com.xm.lib.test.ui.act

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.xm.lib.common.log.BKLog
import com.xm.lib.test.R
import com.xm.lib.test.utils.IntoTarget

/**
 * 单实例模式-从新开辟一个栈
 * 如果启动窗口在栈顶，窗口就不会创建成功。
 * 如果启动窗口不存在栈顶，窗口实例创建并且会放置在新的任务栈中。
 *
 * 点击返回依次销毁栈内实例
 *
 * 场景：应用调用拨号盘
 */
class PrimarySingleInstance : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_primary_single_instance)
        (findViewById<Button>(R.id.btn_once)).setOnClickListener {
            IntoTarget.start(PrimarySingleInstance::class.java.simpleName, this)
        }
        BKLog.d("taskId : $taskId hashcode : ${hashCode()} PrimarySingleInstance : ${PrimarySingleInstance@ this}")
    }

    /**
     * 窗口实例被重用就会触发该方法
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        BKLog.d("onNewIntent taskId : $taskId hashcode : ${hashCode()} PrimarySingleInstance : ${PrimarySingleInstance@ this}")
    }
}

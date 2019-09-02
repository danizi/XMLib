package com.xm.lib.test.ui.act

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.xm.lib.common.log.BKLog
import com.xm.lib.test.R
import com.xm.lib.test.utils.IntoTarget

/**
 * 栈内复用模式
 * 如果启动窗口在栈顶，那么就不会创建新的实例
 * 如果启动串口不在栈顶并且存在，那么不会创建新的实例，而是直接将该窗口实例之前的窗口全部清除。
 *
 * 使用场景：
 * 因为一般为一个APP的第一个页面，且长时间保留在栈中，所以最适合设置singleTask启动模式来复用
 */
class PrimarySingleTask : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_primary_single_task)

        (findViewById<Button>(R.id.btn_once)).setOnClickListener {
            IntoTarget.start(PrimarySingleTask::class.java.simpleName, this)
        }
        (findViewById<Button>(R.id.btn_standard)).setOnClickListener {
            IntoTarget.start(PrimaryStandard::class.java.simpleName, this)
        }
        BKLog.d("taskId : $taskId hashcode : ${hashCode()} PrimarySingleTask : ${PrimarySingleTask@ this}")
    }

    /**
     * 窗口实例被重用就会触发该方法
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        BKLog.d("PrimarySingleTask taskId : $taskId hashcode : ${hashCode()} PrimarySingleTask : ${PrimarySingleTask@ this}")
    }
}

package com.xm.lib.common.base

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.xm.lib.common.log.BKLog


/**
 * Activity基类
 */
abstract class BaseActivity : AppCompatActivity() {
    protected var savedInstanceState: Bundle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0 && intent.flags != Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS/* //在魅族手机上会出现 MainActivity flags 不为intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0情况，所有上面finish Maintivity则再次启动*/) {
            finish()
            // ps:不知道为什么直接使用android studio 安装没有出现问题，但是打车apk包就出现应用进入后台，点击图标重新启动主页面。（如果直接点击home 点击应用还是正常的。）
            BKLog.e("处理用户点击home，再次点击回到之前的界面")
            return
        }

        setContentViewBefore()
        if (savedInstanceState != null) {
            this.savedInstanceState = savedInstanceState
        }
        //val view = (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(getLayouId(), null, true)
        setContentView(getLayouId())
        findViews()
        initDisplay()
        iniData()
        iniEvent()
        //com.jaeger.library.StatusBarUtil.setColor(this, Color.parseColor("#FFFFFF"), 0)  //修改顶部系统栏的颜色
        //StatusBarUtil.StatusBarLightMode(this)  // 使用该方法必须在对应的窗口添加 view.fitsSystemWindows = true属性
    }

    /**
     * 在设置窗口UI之前的操作，例如去除标题栏
     */
    abstract fun setContentViewBefore()

    /**
     * 布局ID
     */
    abstract fun getLayouId(): Int

    /**
     * 查找view
     */
    abstract fun findViews()

    /**
     * 初始化控件展示样式
     */
    abstract fun initDisplay()

    /**
     * 初始化数据
     */
    abstract fun iniData()

    /**
     * 初始化监听
     */
    abstract fun iniEvent()
}
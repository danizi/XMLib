package com.xm.lib.common.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.xm.lib.common.R
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.StatusBarUtil

/**
 * Activity基类
 */
abstract class BaseActivity : AppCompatActivity(), IBaseAct, IState {

    private var control: Control? = Control()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        control?.goBackPreviousRecord(this, intent)
        setContentViewBefore()
        setContentView(control?.getView(this, getLayoutId()))
        findViews()
        initDisplay()
        iniData()
        iniEvent()
        //com.jaeger.library.StatusBarUtil.setColor(this, Color.parseColor("#FFFFFF"), 0)  //修改顶部系统栏的颜色
        //
        control?.statusBarLightMode(this)
    }

    fun back(keyCode: Int, event: KeyEvent?): Boolean {
        return control?.back(this, keyCode, event)!!
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        control?.startFadeAni(this)
    }

    override fun finish() {
        super.finish()
        control?.stopFadeAni(this)
        //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun showLoading() {
        //control?.showLoading()
    }

    override fun showError() {
        //control?.showError()
    }

    override fun showNoData() {
        //control?.showNoData()
    }

    override fun hide() {
        //control?.hide()
    }

    override fun clear() {
        control = null
    }
}

/**
 * Toolbar帮助类
 */
class ToolbarHelp {

    /**
     * 初始化
     */
    fun ini(title: String, activity: AppCompatActivity, toolbar: Toolbar) {
        toolbar.title = title
        activity.setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            activity.finish()
        }
    }

    /**
     * 创建menu
     */
    fun onCreateOptionsMenu(menuId: Int, activity: AppCompatActivity, menu: Menu) {
        activity.menuInflater.inflate(menuId, menu)
    }
}

/**
 * 控制器
 */
class Control {
    private var toolbarHelp: ToolbarHelp? = null
    private var isCustomLayout = false
    private var toolbar: Toolbar? = null
    private var appBarLayout: AppBarLayout? = null
    private var contentView: FrameLayout? = null
    private lateinit var baseView: View

    private val tip: String = "再按一次退出程序"
    private val time = 2000
    private var lastTime: Long = 0

    private var isOverridePendingTransitionFade = true

    init {
        toolbarHelp = ToolbarHelp()
    }

    /**
     * 退出
     */
    fun back(activity: AppCompatActivity, keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - lastTime > time) {
                Toast.makeText(activity, tip, Toast.LENGTH_SHORT).show()
                lastTime = System.currentTimeMillis()
            } else {
                activity.finish()
            }
            return true
        }
        return false
    }

    fun startFadeAni(activity: AppCompatActivity) {
        if (isOverridePendingTransitionFade)
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    fun stopFadeAni(activity: AppCompatActivity) {
        if (isOverridePendingTransitionFade)
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    /**
     * 问题：部分机型点击home键，再进回到应用时重新启动，而不是进入之前停留的窗口页面
     * 处理：通过flags来判断
     */
    fun goBackPreviousRecord(activity: AppCompatActivity, intent: Intent) {
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0 && intent.flags != Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS/* //在魅族手机上会出现 MainActivity flags 不为intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0情况，所有上面finish Maintivity则再次启动*/) {
            activity.finish()
            // ps:不知道为什么直接使用android studio 安装没有出现问题，但是打车apk包就出现应用进入后台，点击图标重新启动主页面。（如果直接点击home 点击应用还是正常的。）
            BKLog.e("处理用户点击home，再次点击回到之前的界面")
            return
        }
    }

    /**
     * 根据机型设置系统栏文字颜色为黑色
     */
    fun statusBarLightMode(activity: AppCompatActivity) {
        //设置系统栏为黑色文字,如果不设置则是显示白色字体
        // 使用该方法必须在对应的窗口添加 view.fitsSystemWindows = true属性
        StatusBarUtil.StatusBarLightMode(activity)
    }

    /**
     * 获取窗口VView
     */
    @SuppressLint("InflateParams")
    fun getView(context: Context, layoutID: Int): View {
        if (isCustomLayout) {
            baseView = LayoutInflater.from(context).inflate(R.layout.act_base, null, false)
            contentView = baseView.findViewById(R.id.fl_content)
            toolbar = baseView.findViewById(R.id.tb)
            appBarLayout = baseView.findViewById(R.id.appbar)
            contentView?.addView(LayoutInflater.from(context).inflate(layoutID, null, false))
            toolbarHelp?.ini("1234", context as AppCompatActivity, toolbar!!)
            return baseView
        }
        return LayoutInflater.from(context).inflate(layoutID, null, false)
    }
}

interface IState {
    /**
     * 显示加载状态
     */
    fun showLoading()

    /**
     * 显示错误状态
     */
    fun showError()

    /**
     * 显示空数据状态
     */
    fun showNoData()

    /**
     * 隐藏
     */
    fun hide()
}

interface IBaseAct {
    /**
     * 在设置窗口UI之前的操作，例如去除标题栏
     */
    fun setContentViewBefore()

    /**
     * 布局ID
     */
    fun getLayoutId(): Int

    /**
     * 查找view
     */
    fun findViews()

    /**
     * 初始化控件展示样式
     */
    fun initDisplay()

    /**
     * 初始化数据
     */
    fun iniData()

    /**
     * 初始化监听
     */
    fun iniEvent()

    /**
     * 回收
     */
    fun clear()
}
package com.xm.lib.common.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.xm.lib.common.log.BKLog
import java.util.*


/**
 * Activity统一管理类
 */
class ActManager : Application.ActivityLifecycleCallbacks, IActManager {

    companion object {
        val instance: ActManager? = ActManager()
        const val TAG = "ActManager"
    }

    private var stackActivity: Stack<Activity> = Stack()
    private var isRegisterActivityLifecycleCallbacks = false

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        addActivity(activity)
        BKLog.i(TAG, "activity ->$activity --- onActivityCreated")
    }


    override fun onActivityPaused(activity: Activity?) {
        BKLog.i(TAG, "activity ->$activity --- onActivityPaused")
    }

    override fun onActivityResumed(activity: Activity?) {
        BKLog.i(TAG, "activity ->$activity --- onActivityResumed")
    }

    override fun onActivityStarted(activity: Activity?) {
        BKLog.i(TAG, "activity ->" + activity + "onActivityStarted")
    }

    override fun onActivityDestroyed(activity: Activity?) {
        removeActivity(activity)
        BKLog.i(TAG, "activity ->$activity --- onActivityDestroyed")
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        BKLog.i(TAG, "activity ->$activity ---  onActivitySaveInstanceState")
    }

    override fun onActivityStopped(activity: Activity?) {
        BKLog.i(TAG, "activity ->$activity --- onActivityStopped")
    }

    override fun registerActivityLifecycleCallbacks(app: Application?) {
        if (!isRegisterActivityLifecycleCallbacks) {
            app?.registerActivityLifecycleCallbacks(this)
            BKLog.i(TAG, "app ->$app --- registerActivityLifecycleCallbacks")
        }
    }

    override fun unregisterActivityLifecycleCallbacks(app: Application?) {
        if (isRegisterActivityLifecycleCallbacks) {
            app?.unregisterActivityLifecycleCallbacks(this)
            BKLog.i(TAG, "app ->$app --- unregisterActivityLifecycleCallbacks")
        }
    }

    override fun addActivity(activity: Activity?) {
        stackActivity.add(activity)
    }

    override fun removeActivity(activity: Activity?) {
        stackActivity.remove(activity)
    }

    override fun finish(activity: Activity?) {
        activity?.finish()
    }

    override fun finishAll() {
        for (act in stackActivity) {
            finish(act)
        }
        stackActivity.clear()
    }

    fun finishPre(activity: Activity?) {
        val it = stackActivity.iterator()
        while (it.hasNext()) {
            val act = it.next()
            if (act == activity) {
                break
            }
            finish(act)
            it.remove()
        }
    }

    override fun isExist(activity: Activity?): Boolean {
        for (act in stackActivity) {
            if (activity == act) {
                return true
            }
        }
        return false
    }

    override fun getTopActivity(): Activity? {
        return stackActivity.lastElement()
    }

    override fun appExit(context: Context) {
        try {
            //Toast.makeText(context, "清除所有的", Toast.LENGTH_LONG).show()
            finishAll()
            //val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
            //activityManager.restartPackage(context.packageName)
            //System.exit(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

/**
 * 窗口管理接口
 */
private interface IActManager {
    /*注册*/
    fun registerActivityLifecycleCallbacks(app: Application?)

    /*取消注册*/
    fun unregisterActivityLifecycleCallbacks(app: Application?)

    /*添加窗口实例*/
    fun addActivity(activity: Activity?)

    /*移除窗口实例*/
    fun removeActivity(activity: Activity?)

    /*销毁指定窗口实例*/
    fun finish(activity: Activity?)

    /*销毁所有窗口实例*/
    fun finishAll()

    /*传入类名判断窗口是否存在*/
    fun isExist(activity: Activity?): Boolean

    /*获取顶部窗口实例*/
    fun getTopActivity(): Activity?

    /*退出应用*/
    fun appExit(context: Context)
}
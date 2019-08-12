package com.xm.lib.common.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Fragment基类
 */
abstract class BaseFragment : Fragment(), IBaseFrg, IState {

    private var act: Activity? = null
    private var v: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (v == null) {
            v = inflater.inflate(getLayoutId(), container, false)
            findViews(v!!)
            initDisplay()
            iniData()
            iniEvent()
        }
        return v
    }

//    /**
//     * 布局ID
//     */
//    abstract fun getLayoutId(): Int
//
//    /**
//     * 查找view
//     */
//    abstract fun findViews(view: View)
//
//    /**
//     * 初始化控件展示样式
//     */
//    abstract fun initDisplay()
//
//    /**
//     * 初始化监听
//     */
//    abstract fun iniEvent()
//
//    /**
//     * 初始化数据
//     */
//    abstract fun iniData()

    override fun clear() {
        if (v != null) {
            (v as ViewGroup).removeAllViews()
            v = null
        }
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showNoData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hide() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        act = context as Activity
    }

    override fun onDestroy() {
        super.onDestroy()
        act = null
    }

}

interface IBaseFrg {
    /**
     * 布局ID
     */
    fun getLayoutId(): Int

    /**
     * 查找view
     */
    fun findViews(view: View)

    /**
     * 初始化控件展示样式
     */
    fun initDisplay()

    /**
     * 初始化监听
     */
    fun iniEvent()

    /**
     * 初始化数据
     */
    fun iniData()

    /**
     * 清空资源
     */
    fun clear()
}
package com.xm.lib.common.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Fragment基类
 */
abstract class BaseFragment : Fragment() {
    private var v: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (v == null) {
            v = inflater.inflate(getLayoutId(), null, false)
            findViews(v!!)
            initDisplay()
            iniData()
            iniEvent()
        }
        return v
    }

    /**
     * 布局ID
     */
    abstract fun getLayoutId(): Int

    /**
     * 查找view
     */
    abstract fun findViews(view: View)

    /**
     * 初始化控件展示样式
     */
    abstract fun initDisplay()

    /**
     * 初始化监听
     */
    abstract fun iniEvent()

    /**
     * 初始化数据
     */
    abstract fun iniData()
}
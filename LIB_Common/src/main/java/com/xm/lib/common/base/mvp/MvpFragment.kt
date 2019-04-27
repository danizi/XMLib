package com.xm.lib.common.base.mvp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xm.lib.common.base.BaseFragment

abstract class MvpFragment<P> : BaseFragment() {
    var p: P? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        p = presenter()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    abstract fun presenter(): P
}
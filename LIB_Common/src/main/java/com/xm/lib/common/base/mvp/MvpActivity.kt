package com.xm.lib.common.base.mvp

import android.os.Bundle
import com.xm.lib.common.base.BaseActivity

/**
 * MVP模式
 */
abstract class MvpActivity<P> : BaseActivity() {

    var p: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        p = presenter()
        super.onCreate(savedInstanceState)
    }

    abstract fun presenter(): P
}


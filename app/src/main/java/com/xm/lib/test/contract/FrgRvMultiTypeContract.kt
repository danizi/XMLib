package com.xm.lib.test.contract

import android.content.Context

class FrgRvMultiTypeContract {

    interface V {}

    class M {}

    class P(val context: Context?,val v: V) {}
}
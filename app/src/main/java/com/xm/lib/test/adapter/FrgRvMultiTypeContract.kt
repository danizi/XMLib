package com.xm.lib.test.adapter

import android.content.Context

class FrgRvMultiTypeContract {

    interface V {}

    class M {}

    class P(val context: Context?,val v: V) {}
}
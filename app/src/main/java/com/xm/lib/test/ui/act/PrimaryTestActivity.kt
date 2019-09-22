package com.xm.lib.test.ui.act

import android.app.Activity
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.Button
import com.xm.lib.common.base.BaseActivity
import com.xm.lib.test.R
import com.xm.lib.test.utils.IntoTarget
import java.util.logging.Handler

class PrimaryTestActivity : BaseActivity() {
    private var ui: UI? = null

    override fun setContentViewBefore() {}

    override fun getLayoutId(): Int {
        return R.layout.activity_primary_test
    }

    override fun findViews() {
        if (ui == null) {
            ui = UI.create(this)
        }
    }

    override fun initDisplay() {
        val handler = object : android.os.Handler() {

            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                Looper.prepare()
            }
        }
        handler.sendEmptyMessage(0)
    }

    override fun iniData() {

    }

    override fun iniEvent() {
        ui?.activity?.setOnClickListener { IntoTarget.start(PrimaryActivityA::class.simpleName, this) }
        ui?.service?.setOnClickListener { IntoTarget.start(PrimaryService::class.simpleName, this) }
        ui?.broadcast?.setOnClickListener { IntoTarget.start(PrimaryBroadcast::class.simpleName, this) }
        ui?.contentprovider?.setOnClickListener { IntoTarget.start(PrimaryContentProvider::class.simpleName, this) }

        ui?.file?.setOnClickListener { IntoTarget.start(PrimaryCache::class.simpleName, this) }
        ui?.db?.setOnClickListener { IntoTarget.start(PrimaryCache::class.simpleName, this) }
        ui?.sp?.setOnClickListener { IntoTarget.start(PrimaryCache::class.simpleName, this) }
        ui?.cp?.setOnClickListener { IntoTarget.start(PrimaryCache::class.simpleName, this) }

        ui?.aniTween?.setOnClickListener { IntoTarget.start(PrimaryAniTween::class.simpleName, this) }
        ui?.aniFrame?.setOnClickListener { IntoTarget.start(PrimaryAniFrame::class.simpleName, this) }
        ui?.aniProperty?.setOnClickListener { IntoTarget.start(PrimaryAniProperty::class.simpleName, this) }

        ui?.thread?.setOnClickListener { IntoTarget.start(PrimaryThread::class.simpleName, this) }

        ui?.rv?.setOnClickListener { }
        ui?.vp?.setOnClickListener { }
        ui?.web?.setOnClickListener { }
        ui?.dl?.setOnClickListener { }
        ui?.sb?.setOnClickListener { }
        ui?.pb?.setOnClickListener { }
    }


    private class UI private constructor(val activity: Button, val service: Button, val broadcast: Button, val contentprovider: Button, val file: Button, val db: Button, val sp: Button, val cp: Button, val thread: Button, val aniTween: Button, val aniFrame: Button, val aniProperty: Button, val rv: Button, val vp: Button, val web: Button, val dl: Button, val sb: Button, val pb: Button) {
        companion object {

            fun create(rootView: Activity): UI {
                val activity = rootView.findViewById<View>(R.id.activity) as Button
                val service = rootView.findViewById<View>(R.id.service) as Button
                val broadcast = rootView.findViewById<View>(R.id.broadcast) as Button
                val contentprovider = rootView.findViewById<View>(R.id.contentprovider) as Button
                val file = rootView.findViewById<View>(R.id.file) as Button
                val db = rootView.findViewById<View>(R.id.db) as Button
                val sp = rootView.findViewById<View>(R.id.sp) as Button
                val cp = rootView.findViewById<View>(R.id.cp) as Button
                val thread = rootView.findViewById<View>(R.id.thread) as Button
                val aniTween = rootView.findViewById<View>(R.id.ani_tween) as Button
                val aniFrame = rootView.findViewById<View>(R.id.ani_frame) as Button
                val aniProperty = rootView.findViewById<View>(R.id.ani_property) as Button
                val rv = rootView.findViewById<View>(R.id.rv) as Button
                val vp = rootView.findViewById<View>(R.id.vp) as Button
                val web = rootView.findViewById<View>(R.id.web) as Button
                val dl = rootView.findViewById<View>(R.id.dl) as Button
                val sb = rootView.findViewById<View>(R.id.sb) as Button
                val pb = rootView.findViewById<View>(R.id.pb) as Button
                return UI(activity, service, broadcast, contentprovider, file, db, sp, cp, thread, aniTween, aniFrame, aniProperty, rv, vp, web, dl, sb, pb)
            }
        }
    }
}



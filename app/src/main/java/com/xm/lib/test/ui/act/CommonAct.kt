package com.xm.lib.test.ui.act

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.xm.lib.test.R
import com.xm.lib.test.adapter.VPAdapter
import com.xm.lib.test.ui.frg.FrgRvMultiType

class CommonAct : AppCompatActivity() {

    private var tb: TabLayout? = null
    private var vp: ViewPager? = null

    private fun findViews() {
        tb = findViewById(R.id.tb)
        vp = findViewById(R.id.vp)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common)
        findViews()
        vp?.adapter = getVpAdapter()
        tb?.setupWithViewPager(vp)
    }

    private fun getVpAdapter(): VPAdapter {
        return VPAdapter.Builder()
                .addTab("多类型RecyclerView", FrgRvMultiType())
                .addTab("2", FrgRvMultiType())
                .addTab("3", FrgRvMultiType())
                .addTab("4", FrgRvMultiType())
                .addTab("5", FrgRvMultiType())
                .addTab("6", FrgRvMultiType())
                .addTab("7", FrgRvMultiType())
                .addTab("8", FrgRvMultiType())
                .setFragmentManager(supportFragmentManager)
                .builder()
    }
}

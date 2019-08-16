package com.xm.lib.test.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.xm.lib.common.log.BKLog

/**
 * ViewPager适配器
 */
class VPAdapter private constructor(val builder: Builder?, val fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {

    override fun getItem(p0: Int): Fragment? {
        for (ent in builder?.tabs?.entries!!) {
            if (p0 == ent.key.first) {
                return ent.value
            }
        }
        BKLog.e("getItem fragment is null")
        return null
    }

    override fun getCount(): Int {
        return if (builder?.tabs?.isEmpty()!!) {
            0
        } else {
            builder.tabs.size
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        for (ent in builder?.tabs?.entries!!) {
            if (position == ent.key.first) {
                return ent.key.second
            }
        }
        return super.getPageTitle(position)
    }

    class Builder {
        private var addTabIndex = 0
        var tabs: HashMap<Pair<Int, String>, Fragment> = HashMap()
        var fm: FragmentManager? = null

        fun setFragmentManager(fm: FragmentManager?): Builder {
            this.fm = fm
            return this
        }

        fun addTab(title: String, frg: Fragment): Builder {
            tabs[Pair(addTabIndex++, title)] = frg
            return this
        }

        fun builder(): VPAdapter {
            return VPAdapter(this, fm)
        }
    }
}
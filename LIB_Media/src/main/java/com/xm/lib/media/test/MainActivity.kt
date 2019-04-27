package com.xm.lib.media.test

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Window
import com.xm.lib.media.R
import com.xm.lib.media.test.frg.PageFragment
import java.util.*

class MainActivity : AppCompatActivity() {
    internal var TAG = "MainActivity"
    //private val xmVideoView: XmVideoView? = null
    private var tabLayout: TabLayout? = null
    private var vp: ViewPager? = null

    private fun noTitle() {
        //设置窗体为没有标题的模式
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        noTitle()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        iniData()
        iniEvent()
    }

    private fun iniEvent() {

    }

    private fun iniData() {
        val fragments = ArrayList<Fragment>()
        val titles = ArrayList<String>()
        titles.add("title")
        titles.add("title2")
        fragments.add(PageFragment.create("播放器测试", 0))
        fragments.add(PageFragment.create("播放列表测试", 1))
        vp?.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(i: Int): Fragment {
                return fragments[i]
            }

            override fun getCount(): Int {
                return fragments.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return titles[position]
            }
        }

    }


    private fun findViews() {
        tabLayout = findViewById(R.id.tab)
        vp = findViewById(R.id.vp)

        tabLayout?.setTabTextColors(Color.BLACK, Color.RED)//第一个参
        tabLayout?.setSelectedTabIndicatorColor(Color.RED)//选中tab下划线颜色
        tabLayout?.setSelectedTabIndicatorHeight(5)//选中tab下划线高度
        tabLayout?.setupWithViewPager(vp)
    }

//    override fun onPause() {
//        super.onPause()
//        xmVideoView!!.onPause()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        xmVideoView!!.onResume()    //绑定播放服务
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        xmVideoView!!.onDestroy()
//    }


}



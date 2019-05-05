package com.xm.lib.component


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import java.util.*

/**
 * 底部菜单
 */
class BottomMenu(context: Context?, attrs: AttributeSet?) : TabLayout(context, attrs), IBottomMenu {

    companion object {
        const val STATE_SEL = 0x00
        const val STATE_UN_SEL = 0x01
        const val DEFAULT = -1
    }

    var pos: Int = 0
    private val fragmentUitls: FragmentUitls = FragmentUitls()
    private var itemLayoutId: Int = DEFAULT
    private var layoutID: Int = DEFAULT
    private var colorID: Int = DEFAULT
    private var beforeColorID: Int = DEFAULT
    private var afterColorID: Int = DEFAULT
    private var items: ArrayList<BottomMenuBean>? = ArrayList()
    private var lisenter: OnItemClickListener? = null

    override fun setContainer(layoutID: Int): BottomMenu {
        this.layoutID = layoutID
        return this
    }

    override fun setItemLayoutId(layoutId: Int): BottomMenu {
        this.itemLayoutId = layoutId
        return this
    }

    override fun setBackground(colorID: Int): BottomMenu {
        this.colorID = layoutID
        return this
    }

    override fun setTitleColor(beforeColorID: Int, afterColorID: Int): BottomMenu {
        this.beforeColorID = beforeColorID
        this.afterColorID = afterColorID
        return this
    }

    override fun addItem(fragment: Fragment?, title: String?, beforeIconID: Int, afterIconID: Int): BottomMenu {
        val bottomMenuBean = BottomMenuBean()
        bottomMenuBean.fragment = fragment
        bottomMenuBean.title = title
        bottomMenuBean.beforeIconID = beforeIconID
        bottomMenuBean.afterIconID = afterIconID
        items?.add(bottomMenuBean)
        return this
    }

    override fun setOnItemClickListener(lisenter: OnItemClickListener): BottomMenu {
        this.lisenter = lisenter
        return this
    }

    override fun build() {
        initTablayout()
    }

    override fun select(pos: Int): BottomMenu {
        if (pos < items?.size!!) {
            //触发onTabSelected监听
            this.getTabAt(pos)?.select()
        } else {
            this.pos = pos
        }
        return this
    }

    private fun initTablayout() {
        //设置底部滑块透明
        this.setSelectedTabIndicatorColor(Color.TRANSPARENT)
        //设置监听
        this.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: Tab?) {
                //重复选择相同的item
            }

            override fun onTabUnselected(p0: Tab?) {
                //上一次item的位置
                updateItemDisplay(STATE_UN_SEL, p0)
            }

            override fun onTabSelected(p0: Tab?) {
                //item选中
                //1 修改底部菜单图标和文字颜色
                //2 展示内容Fragment
                //3 默认选中第一个
                updateItemDisplay(STATE_SEL, p0)
                displayFragment(p0)
            }
        })
        //添加tab
        if (items?.isNotEmpty()!!) {
            for (index in 0..(items?.size!! - 1)) {
                try {
                    val tab = this.newTab()
                    tab.tag = index
                    (tab.view as ViewGroup).setBackgroundColor(Color.TRANSPARENT)
                    val itemView: ViewGroup = LayoutInflater.from(context).inflate(this.itemLayoutId, this, false) as ViewGroup
                    val tabImg: ImageView = itemView.getChildAt(0) as ImageView
                    val tabTv: TextView = itemView.getChildAt(1) as TextView
                    tabTv.text = items!![index].title
                    tabImg.setImageResource(items!![index].beforeIconID!!)

                    this.addTab(tab.setCustomView(itemView))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        //选择选项
        this.getTabAt(pos)?.select()
    }

    @SuppressLint("ResourceAsColor")
    private fun updateItemDisplay(state: Int, tab: TabLayout.Tab?) {
        //更新状态
        val tabImg: ImageView = (tab?.customView as ViewGroup).getChildAt(0) as ImageView
        val tabTv: TextView = (tab.customView as ViewGroup).getChildAt(1) as TextView
        val pos = tab.tag as Int
        var iconId = 0
        var colorId = 0
        when (state) {
            STATE_SEL -> {
                iconId = items!![pos].afterIconID!!
                colorId = afterColorID
            }
            STATE_UN_SEL -> {
                iconId = items!![pos].beforeIconID!!
                colorId = beforeColorID
            }
        }
        tabImg.setImageResource(iconId)
        tabTv.setTextColor(this.resources.getColor(colorId))
    }

    private fun displayFragment(tab: TabLayout.Tab?) {
        //将fragment装载到展示容器中
        val pos = tab?.tag as Int
        lisenter?.onItemClick(this, pos)
        fragmentUitls.displayFragment(context as FragmentActivity, layoutID, tab.tag.toString(), items!![pos].fragment!!)
    }

}

/**
 * Fragment操作类
 */
class FragmentUitls {

    private var preFragmentTag: String? = null
    private var fragmentManager: FragmentManager? = null

    /**
     * hide show方式
     *
     * @param object      Activity||Fragment实例
     * @param containerId 页面转载容器
     * @param tag         当前页面的tag
     * @param curFragment 当前fragment
     */
    fun displayFragment(`object`: Any, containerId: Int, tag: String, curFragment: Fragment) {

        if (!TextUtils.isEmpty(preFragmentTag)) {
            hideFragment(preFragmentTag)
        }

        val findFragment = getFragmentManager(`object`)?.findFragmentByTag(tag)

        if (findFragment == null) {
            addFragment(containerId, curFragment, tag)
        } else {
            showFragment(tag)
        }
        preFragmentTag = tag
    }

    private fun getFragmentManager(`object`: Any): FragmentManager? {
        //object Activity||Fragment实例
        if (`object` is FragmentActivity) {
            fragmentManager = `object`.supportFragmentManager
        } else if (`object` is Fragment) {
            fragmentManager = `object`.childFragmentManager
        }
        return fragmentManager
    }

    @SuppressLint("CommitTransaction")
    private fun addFragment(containerId: Int, fragment: Fragment, tag: String) {
        val fragmentTransaction: FragmentTransaction = fragmentManager?.beginTransaction()!!
        fragmentTransaction.add(containerId, fragment, tag)
        fragmentTransaction.addToBackStack(tag)
        fragmentTransaction.commit()
    }

    @SuppressLint("CommitTransaction")
    private fun hideFragment(tag: String?) {
        if (TextUtils.isEmpty(tag)) {
            return
        }
        val fragmentTransaction: FragmentTransaction = fragmentManager?.beginTransaction()!!
        val fragment = fragmentManager?.findFragmentByTag(tag) ?: return
        fragmentTransaction.hide(fragment)
        fragmentTransaction.addToBackStack("hide $tag")
        fragmentTransaction.commit()
    }

    @SuppressLint("CommitTransaction")
    private fun showFragment(tag: String) {
        if (TextUtils.isEmpty(tag)) {
            return
        }
        val fragment = fragmentManager?.findFragmentByTag(tag) ?: return
        val fragmentTransaction: FragmentTransaction = fragmentManager?.beginTransaction()!!
        fragmentTransaction.show(fragment)
        fragmentTransaction.addToBackStack("show $tag")
        fragmentTransaction.commit()
    }

    /* detach attach 切换速度慢 方式*/
    /* add replace方式 不推荐*/
}

/**
 * 底部菜单实体类
 */
private class BottomMenuBean {
    var beforeColorID: Int? = null //标题未选中颜色
    var beforeIconID: Int? = null  //标题未选中图标
    var afterColorID: Int? = null  //标题选中颜色
    var afterIconID: Int? = null   //标题选中图标
    var fragment: Fragment? = null //展示的fragment
    var title: String? = null      //标题内容
}

/**
 * 对外提供接口
 */
interface IBottomMenu {
    fun setContainer(layoutID: Int): BottomMenu   //设置内容装载布局id
    fun setItemLayoutId(layoutId: Int): BottomMenu //item布局id
    fun setBackground(colorID: Int): BottomMenu    //设置底部菜单颜色
    fun setTitleColor(beforeColorID: Int, afterColorID: Int): BottomMenu//设置标题点击前后的颜色
    fun addItem(fragment: Fragment?, title: String?, beforeIconID: Int, afterIconID: Int): BottomMenu//添加item
    fun setOnItemClickListener(listener: OnItemClickListener): BottomMenu //设置监听
    fun build() //构建
    fun select(pos: Int): BottomMenu//默认选中位置
}

/**
 * 点击监听
 */
interface OnItemClickListener {
    fun onItemClick(view: BottomMenu, pos: Int)
}

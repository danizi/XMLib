package com.xm.lib.common.base

import android.os.Build
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewTreeObserver
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.xm.lib.common.R
import com.xm.lib.common.base.rv.v1.BaseRvAdapter
import com.xm.lib.common.log.BKLog

/**
 * 刷新加载帮助类
 */
@Deprecated("")
class RefreshLoadHelp {

    var ui: ViewHolder? = null
    var adapter: BaseRvAdapter? = object : BaseRvAdapter() {}
    /**
     * 是否第一次加载
     */
    private var isFirstRefreshSucces = false

    /**
     * 分页数量
     */
    var page = 1

//    /**
//     * 禁止刷新
//     */
//    var disableRefresh = false
//
//    /**
//     * 禁止加载
//     */
//    var disableLoad = false

    fun bind(view: View) {
        ui = ViewHolder.create(view)
    }

    fun bind(act: AppCompatActivity) {
        ui = ViewHolder.create(act)
    }

    fun ini(disableRefresh: Boolean, disableLoad: Boolean) {

        if (!disableRefresh) {
            ui?.srl?.isEnableRefresh = true
        }

        if (!disableLoad) {
            //判断RecyclerView内容的长度是否可以触发上拉加载
            ui?.isCanLoad(ui?.rv, ui?.srl)
        }


    }

    /**
     * 刷新成功处理
     */
    fun refreshSuccess(data: List<Any>) {
        //判断数据是否为空
        if (!data.isNullOrEmpty() && !isFirstRefreshSucces) {
            //第一次请求数据失败，显示加载“无数据状态”页面
            showStatePage("noData")
        } else {
            isFirstRefreshSucces = true
            //清空数据
            adapter?.data?.clear()
            //添加数据到适配器中
            adapter?.data?.addAll(data)
            //设置状态
        }
        //完成刷新
        ui?.srl?.finishRefresh()
    }

    private fun showStatePage(s: String) {
        BKLog.d("statePage:$s")
    }

    /**
     * 刷新失败处理
     */
    fun refreshFailure() {
        //完成刷新
        ui?.srl?.finishRefresh()
        //显示加载数据失败页面
        showStatePage("ErrorPage")
    }

    /**
     * 加载成功
     */
    fun loadSuccess(d: List<Any>) {
        if (!d.isNullOrEmpty()) {
            adapter?.data?.addAll(d)
            val positionStart = adapter?.data?.size
            val itemCount = d.size
            adapter?.notifyItemRangeChanged(positionStart!!, itemCount)
        }
        ui?.srl?.finishLoadMore()
    }

    /**
     * 加载失败
     */
    fun loadFailure() {
        showStatePage("ErrorPage")
        ui?.srl?.finishLoadMore()
    }

    /**
     * 基类ViewHolder
     */
    class ViewHolder private constructor(val srl: SmartRefreshLayout, val rv: RecyclerView, val clContent: ConstraintLayout) {
        companion object {

            fun create(act: AppCompatActivity): ViewHolder {
                //val toolbar = act.findViewById<View>(R.id.toolbar) as Toolbar
                val srl = act.findViewById<View>(R.id.srl) as SmartRefreshLayout
                val rv = act.findViewById<View>(R.id.rv) as RecyclerView
                val clContent = act.findViewById<View>(R.id.cl_content) as ConstraintLayout
                return ViewHolder(srl, rv, clContent)
            }

            fun create(view: View): ViewHolder {
                val srl = view.findViewById<View>(R.id.srl) as SmartRefreshLayout
                val rv = view.findViewById<View>(R.id.rv) as RecyclerView
                val clContent = view.findViewById<View>(R.id.cl_content) as ConstraintLayout
                return ViewHolder(srl, rv, clContent)
            }
        }

        /**
         * 检查RecyclerView内容是否大于刷新组件的高度 ，如果小于则不能进行底部加载操作
         */
        fun isCanLoad(rv: RecyclerView?, srl: SmartRefreshLayout?) {
            srl?.isEnableLoadMore = true
            val vto = rv?.viewTreeObserver
            vto?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val heigh = rv.computeVerticalScrollExtent()
                    if (heigh < srl?.measuredHeight!!) {
                        BKLog.d("template.measuredHeight" + heigh + "srl.measuredHeight" + srl.measuredHeight)
                        srl.isEnableLoadMore = false
                    }
                    if (vto.isAlive) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            vto.removeOnGlobalLayoutListener(this)
                        }
                    }
                }
            })
        }
    }
}
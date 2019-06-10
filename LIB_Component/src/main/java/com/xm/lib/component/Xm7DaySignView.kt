package com.xm.lib.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.xm.lib.common.util.ViewUtil
import java.text.SimpleDateFormat
import java.util.*


/**
 * 7签到组件
 */
class Xm7DaySignView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    /**
     * 七天组件ui
     */
    private var ui: ViewHolder? = null
    /**
     * 签到超过七天是否，再重头开始，true代表是
     */
    private var isLoop = false
    /**
     * 签到的天数
     */
    private var index = -1
    /**
     * 积分列表
     */
    private var scores = ArrayList<Int>()
    private var tvScore = ArrayList<TextView>()
    private var viewLine = ArrayList<View>()
    private var tvDay = ArrayList<TextView>()

    init {
        val view = ViewUtil.viewById(context, R.layout.view_7_day_sign, this)
        findView(view)
        display()
        addView(view)
    }

    /**
     * findView
     */
    private fun findView(view: View?) {
        if (ui == null) {
            ui = ViewHolder.create(view)
        }

        //显示积分控件添加到集合
        tvScore.add(ui?.tvScore1!!)
        tvScore.add(ui?.tvScore2!!)
        tvScore.add(ui?.tvScore3!!)
        tvScore.add(ui?.tvScore4!!)
        tvScore.add(ui?.tvScore5!!)
        tvScore.add(ui?.tvScore6!!)
        tvScore.add(ui?.tvScore7!!)

        //显示横线控件添加到集合
        viewLine.add(ui?.viewLine1!!)
        viewLine.add(ui?.viewLine2!!)
        viewLine.add(ui?.viewLine3!!)
        viewLine.add(ui?.viewLine4!!)
        viewLine.add(ui?.viewLine5!!)
        viewLine.add(ui?.viewLine6!!)
        viewLine.add(ui?.viewLine7!!)

        //显示日期控件添加到集合
        tvDay.add(ui?.tvDay1!!)
        tvDay.add(ui?.tvDay2!!)
        tvDay.add(ui?.tvDay3!!)
        tvDay.add(ui?.tvDay4!!)
        tvDay.add(ui?.tvDay5!!)
        tvDay.add(ui?.tvDay6!!)
        tvDay.add(ui?.tvDay7!!)
    }


    /**
     * 展示
     */
    @SuppressLint("SetTextI18n")
    private fun display() {
        //显示日期
        for (i in 0..6) {
            if (i == index || (index == -1 && i == 0)) {
                tvDay[i].text = date()
                tvDay[i].text = "今日"
            } else {
                tvDay[i].text = date(i - index)
            }
        }

        //显示积分
        var flag = false
        if (!scores.isEmpty()) {
            flag = true
        }
        for (i in 0..6) {
            if (flag) {
                tvScore[i].text = "+${scores[i]}"
            } else {
                tvScore[i].text = "+${i + 1}"
                scores.add(i)
            }
        }

        //显示签到状态
        for (i in 0..6) {
            if (i <= index) {
                //选中状态
                tvScore[i].isEnabled = true
                tvScore[i].text = ""
                //viewLine[i].isEnabled = true
                viewLine[i].setBackgroundColor(Color.parseColor("#FF3542"))
                tvDay[i].isEnabled = true
            } else if (i > index) {
                //未选中状态
                tvScore[i].isEnabled = false
                // viewLine[i].isEnabled = false
                viewLine[i].setBackgroundColor(Color.parseColor("#DFE3F1"))
                tvDay[i].isEnabled = false
            }
        }
    }

    /**
     * 设置选中天数
     */
    fun setSelect(index: Int) {
        this.index = (index - 1)
        if (isLoop) {
            this.index = this.index % 7
        } else {
            if (this.index > 6) {
                this.index = 5
            }
        }
        scores.clear()
        display()
    }

    /**
     * 设置积分大小
     */
    fun setScore(scores: ArrayList<Int>) {
        if (scores.isEmpty()) {
            throw IllegalAccessException("scores is null")
        }
        if (scores.size > 7) {
            throw IllegalAccessException("scores size 大于七")
        }
        this.scores = scores
        display()
    }

    /**
     * 增加一天
     */
    fun addDay() {
        this.index++
        if (index > 6) {
            index %= 7
        }
        display()
    }

    /**
     * 减少一天
     */
    fun delDay() {
        this.index--
        if (index < 0) {
            index = 0
        }
        display()
    }

    /**
     * 日期
     * @param days 0 代表今日 1代表在今日基础上+1 -1代表在今日基础上-1
     */
    @SuppressLint("SimpleDateFormat")
    private fun date(days: Int? = 0): String {
        val sf = SimpleDateFormat("MM-dd")
        val c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_MONTH, days!!)
        return sf.format(c.time)
    }

    /**
     * 签到ui
     */
    private class ViewHolder private constructor(val tvScore1: TextView, val viewLine1: View, val tvDay1: TextView, val tvScore2: TextView, val viewLine2: View, val tvDay2: TextView, val tvScore3: TextView, val viewLine3: View, val tvDay3: TextView, val tvScore4: TextView, val viewLine4: View, val tvDay4: TextView, val tvScore5: TextView, val viewLine5: View, val tvDay5: TextView, val tvScore6: TextView, val viewLine6: View, val tvDay6: TextView, val tvScore7: TextView, val viewLine7: View, val tvDay7: TextView) {
        companion object {

            fun create(rootView: View?): ViewHolder {
                val tvScore1 = rootView?.findViewById<View>(R.id.tv_score_1) as TextView
                val viewLine1 = rootView.findViewById<View>(R.id.view_line_1) as View
                val tvDay1 = rootView.findViewById<View>(R.id.tv_day_1) as TextView
                val tvScore2 = rootView.findViewById<View>(R.id.tv_score_2) as TextView
                val viewLine2 = rootView.findViewById<View>(R.id.view_line_2) as View
                val tvDay2 = rootView.findViewById<View>(R.id.tv_day_2) as TextView
                val tvScore3 = rootView.findViewById<View>(R.id.tv_score_3) as TextView
                val viewLine3 = rootView.findViewById<View>(R.id.view_line_3) as View
                val tvDay3 = rootView.findViewById<View>(R.id.tv_day_3) as TextView
                val tvScore4 = rootView.findViewById<View>(R.id.tv_score_4) as TextView
                val viewLine4 = rootView.findViewById<View>(R.id.view_line_4) as View
                val tvDay4 = rootView.findViewById<View>(R.id.tv_day_4) as TextView
                val tvScore5 = rootView.findViewById<View>(R.id.tv_score_5) as TextView
                val viewLine5 = rootView.findViewById<View>(R.id.view_line_5) as View
                val tvDay5 = rootView.findViewById<View>(R.id.tv_day_5) as TextView
                val tvScore6 = rootView.findViewById<View>(R.id.tv_score_6) as TextView
                val viewLine6 = rootView.findViewById<View>(R.id.view_line_6) as View
                val tvDay6 = rootView.findViewById<View>(R.id.tv_day_6) as TextView
                val tvScore7 = rootView.findViewById<View>(R.id.tv_score_7) as TextView
                val viewLine7 = rootView.findViewById<View>(R.id.view_line_7) as View
                val tvDay7 = rootView.findViewById<View>(R.id.tv_day_7) as TextView
                return ViewHolder(tvScore1, viewLine1, tvDay1, tvScore2, viewLine2, tvDay2, tvScore3, viewLine3, tvDay3, tvScore4, viewLine4, tvDay4, tvScore5, viewLine5, tvDay5, tvScore6, viewLine6, tvDay6, tvScore7, viewLine7, tvDay7)
            }
        }
    }


}

//@SuppressLint("SimpleDateFormat")
//private fun date(days: Int): String {
//    val sf = SimpleDateFormat("MM-dd")
//    val c = Calendar.getInstance()
//    c.add(Calendar.DAY_OF_MONTH, days)
//    return sf.format(c.time)
//}
//
//fun main(args: Array<String>) {
//    System.out.println(date(0))
//    System.out.println(date(1))
//    System.out.println(date(2))
//    System.out.println(date(-3))
//}


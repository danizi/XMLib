package com.xm.lib.component.test

import android.content.Context
import android.graphics.Color
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.xm.lib.common.util.ScreenUtil
import com.xm.lib.component.R
import kotlinx.android.synthetic.main.activity_char.view.*


class CharActivity : AppCompatActivity() {

    private var chart: LineChart? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_char)
        chart = findViewById(R.id.chart)

        val dataObjects = arrayOf(0, 45, 0, 0, 0, 50, 600)
        val entries = ArrayList<Entry>()
        for (i in dataObjects.indices) {
            val data = dataObjects[i]
            entries.add(Entry(i.toFloat(), data.toFloat()))
        }
        val dataSet = LineDataSet(entries, "")
        dataSet.setColors(Color.parseColor("#CFA972")) // 每个点之间线的颜色，还有其他几个方法，自己看
        dataSet.setDrawHorizontalHighlightIndicator(false) //是否绘制十字指示线的 【横线】
        dataSet.setCircleColor(Color.parseColor("#CFA972"))
        dataSet.valueTextColor = Color.parseColor("#676767")
        dataSet.valueTextSize = 12f
        //dataSet.setDrawFilled(true)//设置是否开启填充，默认为false
        //dataSet.fillColor = Color.parseColor("#CFA972")//设置填充颜色
        //dataSet.fillAlpha = 85//设置填充区域透明度，默认值为85

        val lineData = LineData(dataSet)
        lineData.isHighlightEnabled = true
        chart?.data = lineData

//        dataSet.circleHoleColor = Color.WHITE
//        dataSet.circleSize = 2F
//        dataSet.cubicIntensity = 20f
//        dataSet.fillAlpha = 85
//        dataSet.form = Legend.LegendForm.CIRCLE;

        //刻度线设置
        //不绘制右边刻度线
        chart?.axisRight?.isEnabled = false
        chart?.axisLeft?.isEnabled = false

        val xAxis = chart?.xAxis    // 获取X轴
//        xAxis?.isEnabled = true             // 轴线是否可编辑,默认true
//        xAxis?.setDrawLabels(false)      // 是否绘制标签,默认true
        xAxis?.setDrawAxisLine(false)    // 是否绘制坐标轴,默认true
//        xAxis?.setDrawGridLines(false)   // 是否绘制网格线，默认true
        xAxis?.gridColor = Color.parseColor("#DDDDDD")
        xAxis?.textColor = Color.parseColor("#333333")
        xAxis?.textSize = 12f
        xAxis?.labelCount = 8
        val labels = ArrayList<String>()
        labels.add("7.13")
        labels.add("7.14")
        labels.add("7.15")
        labels.add("7.16")
        labels.add("7.17")
        labels.add("7.18")
        labels.add("今天")
        xAxis?.granularity = (1).toFloat()   //颗粒度（单位天）
        xAxis?.position = XAxis.XAxisPosition.BOTTOM
        xAxis?.valueFormatter = CustomXValueFromatter(labels)

        val yAxis = chart?.axisLeft
        yAxis?.setDrawZeroLine(false) //
        yAxis?.gridColor = Color.parseColor("#DDDDDD")
//        yAxis?.spaceBottom = 8f
//        yAxis?.spaceTop = 10f


//        xAxis?.axisLineWidth = ScreenUtil.dip2px(this,1).toFloat()
//        yAxis?.axisLineWidth = ScreenUtil.dip2px(this,1).toFloat()

//        xAxis?.gridLineWidth = 3f //设置垂直线的 宽度
//        yAxis?.enableGridDashedLine(10f, 5f, 0f)
//
//        yAxis?.isEnabled = true
//        yAxis?.setDrawZeroLine(false) //不绘制零线
//        yAxis?.setDrawLabels(true)  // 是否绘制标签,默认true
//        yAxis?.setDrawAxisLine(true)    // 是否绘制坐标轴,默认true
//        yAxis?.setDrawGridLines(false)   // 是否绘制网格线，默认true
        chart?.animateY(800)
        chart?.isScaleYEnabled = false
        chart?.isScaleXEnabled = false
        val markerView = CustomMarkerView(this, R.layout.char_marker_view)
        markerView.chartView = chart
        chart?.marker = markerView

        //chart?.legend?.form = Legend.LegendForm.CIRCLE
        chart?.legend?.formSize = ScreenUtil.dip2px(this, 0).toFloat()
        chart?.legend?.textColor = Color.parseColor("#676767")
        chart?.legend?.textSize = 13f

        //chart?.setTouchEnabled(false)
        val description = Description()
        description.text = ""
        chart?.description = description

    }

    /**
     * 自定义X轴提示
     */
    inner class CustomXValueFromatter(var labels: ArrayList<String>?) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            Log.d("CharActivity", "value:$value size: ${labels?.size}")
            return labels?.get(value.toInt() % labels?.size!!)!!
        }
    }

    /**
     * 自定义选中点提示
     */
    inner class CustomMarkerView(context: Context?, layoutResource: Int) : MarkerView(context, layoutResource) {

        var tv: TextView? = null

        init {
            tv = findViewById(R.id.tvContent)
        }

        override fun refreshContent(e: Entry?, highlight: Highlight?) {
            Log.e("CharActivity", e.toString())
            tv?.text = e?.y?.toInt().toString() + "分钟"
            super.refreshContent(e, highlight)
        }

        override fun getOffset(): MPPointF {
            return MPPointF((-(width / 2)).toFloat(), (-height - 50).toFloat())
        }

    }
}



package com.xm.lib.component

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView


/**
 * 开关
 */
class ToggleButton(context: Context?, attrs: AttributeSet?) : ConstraintLayout(context, attrs), IToggleButton {
    companion object {
        const val TAG = "ToggleButton"
    }

    private lateinit var leftButton: ImageView
    private lateinit var rightButton: ImageView
    var toggleButtonLeft: ToggleButtonBean? = null
    var toggleButtonRight: ToggleButtonBean? = null
    var listener: OnToggleButtonClickListener? = null

    override fun setLeftButton(beforeId: Int, afterId: Int, w: Int, h: Int): ToggleButton {
        toggleButtonLeft = getToggleButtonBean(beforeId, afterId, w, h)
        return this
    }

    override fun setRightButton(beforeId: Int, afterId: Int, w: Int, h: Int): ToggleButton {
        toggleButtonRight = getToggleButtonBean(beforeId, afterId, w, h)
        return this
    }

    private fun getToggleButtonBean(beforeId: Int, afterId: Int, w: Int, h: Int): ToggleButtonBean {
        val toggleButtonBean = ToggleButtonBean()
        toggleButtonBean.beforeId = beforeId
        toggleButtonBean.afterId = afterId
        toggleButtonBean.w = w
        toggleButtonBean.h = h
        return toggleButtonBean
    }

    /**
     * 重置状态
     */
    fun reset() {
        setImageResource(leftButton, rightButton, toggleButtonLeft?.afterId, toggleButtonRight?.beforeId)
        CommonUtil.clickable(leftButton, true)
        CommonUtil.clickable(rightButton, false)
    }

    /**
     * 选中左边按钮
     */
    fun selLeft() {
        CommonUtil.clickable(leftButton, false)
        CommonUtil.clickable(rightButton, true)
        setImageResource(leftButton, rightButton, toggleButtonLeft?.afterId, toggleButtonRight?.beforeId)
    }

    /**
     * 选中右边按钮
     */
    fun selRight() {
        CommonUtil.clickable(leftButton, true)
        CommonUtil.clickable(rightButton, false)
        setImageResource(leftButton, rightButton, toggleButtonLeft?.beforeId, toggleButtonRight?.afterId)
    }

    override fun build(): ToggleButton {
        //检查各个参数是否正常
        val set = ConstraintSet()
        //添加左边按钮,添加右边按钮
        leftButton = leftButton(set, 0)
        rightButton = rightButton(set, leftButton.id)
        setImageResource(leftButton, rightButton, toggleButtonLeft?.afterId, toggleButtonRight?.beforeId)    //设置默认资源
        this.addView(leftButton)
        this.addView(rightButton)
        //设置监听
        leftButton.setOnClickListener {
            setImageResource(leftButton, rightButton, toggleButtonLeft?.afterId, toggleButtonRight?.beforeId)
            listener?.onClick(false)
            CommonUtil.clickable(leftButton, false)
            CommonUtil.clickable(rightButton, true)

        }
        rightButton.setOnClickListener {
            setImageResource(leftButton, rightButton, toggleButtonLeft?.beforeId, toggleButtonRight?.afterId)
            listener?.onClick(true)
            CommonUtil.clickable(rightButton, false)
            CommonUtil.clickable(leftButton, true)
        }
        //应用约束
        set.applyTo(this)
        return this
    }

    private fun setImageResource(leftButton: ImageView, rightButton: ImageView, leftId: Int?, rightId: Int?) {
        leftButton.setImageResource(leftId!!)
        rightButton.setImageResource(rightId!!)
    }

    private fun leftButton(set: ConstraintSet?, endId: Int): ImageView {
        val img = ImageView(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            img.id = View.generateViewId()
        } else {
            Log.e(TAG, "设置id失败")
        }
        if (toggleButtonLeft?.afterId != null) {
            img.setImageResource(toggleButtonLeft?.afterId!!)
        }
        //set?.constrainWidth(img.id, ConstraintSet.WRAP_CONTENT)
        //set?.constrainHeight(img.id, ConstraintSet.WRAP_CONTENT)
        set?.constrainWidth(img.id, toggleButtonLeft?.w!!)
        set?.constrainHeight(img.id, toggleButtonLeft?.h!!)
        return img
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun rightButton(set: ConstraintSet?, endId: Int): ImageView {
        val img = ImageView(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            img.id = View.generateViewId()
        } else {
            Log.e(TAG, "设置id失败")
        }
        if (toggleButtonLeft?.afterId != null) {
            img.setImageResource(toggleButtonLeft?.afterId!!)
        }
        set?.connect(img.id, ConstraintSet.LEFT, endId, ConstraintSet.RIGHT)
        set?.constrainWidth(img.id, toggleButtonLeft?.w!!)
        set?.constrainHeight(img.id, toggleButtonLeft?.h!!)
        return img
    }

    override fun setOnToggleButtonClickListener(listener: OnToggleButtonClickListener) {
        this.listener = listener
    }

    fun dp2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    fun rightButtonPerformClick() {
        rightButton.performClick()
    }
}

/**
 * 开关实体bean
 */
class ToggleButtonBean {
    var beforeId: Int = 0  //选状态
    var afterId: Int = 0   //选中状态
    var w: Int = 0
    var h: Int = 0
}

/**
 * 对外提供接口
 */
interface IToggleButton {
    /**
     * 设置左边的资源
     */
    fun setLeftButton(beforeId: Int, afterId: Int, w: Int, h: Int): ToggleButton

    /**
     * 设置右边的资源
     */
    fun setRightButton(beforeId: Int, afterId: Int, w: Int, h: Int): ToggleButton

    /**
     * 构建
     */
    fun build(): ToggleButton

    /**
     * 设置监听
     */
    fun setOnToggleButtonClickListener(listener: OnToggleButtonClickListener)
}

/**
 * 监听
 */
interface OnToggleButtonClickListener {
    fun onClick(no_off: Boolean)
}
package com.xm.lib.test.ui.act

import android.animation.FloatEvaluator
import android.animation.IntEvaluator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.RESTART
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.LinearInterpolator
import com.xm.lib.common.log.BKLog
import com.xm.lib.test.R

/**
 * 属性动画 3.0出现的
 * what
 * 它是一个强大的动画框架，它作用于对象，可以在指定的时间内改变属性。属性动画我们可以设置如下特征：
 * Duration            ：指定动画时间，如果不指定默认是300 ms。
 * Interpolation       ：插值器，单位时间内是均速改变，还是加速改变属性值。
 * Repeat count        ：动画重复次数。
 * Animator sets       ：将动画添加到一个动画组中执行。
 * Frame refresh delay ：属性动画执行帧率 10 ms，刷新的速度也受当前系统工作情况和硬件而定的。
 *
 *
 * why
 * 因为相比之前的动画的好处在于，它单单只限于控件，只要是对象就可以使用，3.0之前的动画改变的只是显示，实际属性是没有发生改变的。
 * 而属性动画是改变的。
 *
 *
 * how
 * 属性动画API结构说明
 * ValueAnimator  ：动画的记录者，记录信息包括了动画时间、重复次数、监听值变化、插值器等...
 * ObjectAnimator ：ValueAnimator的子类，在为动画计算新值时相应地更新属性。
 * AnimatorSet    ：将单个动画关联在一起，可以顺序播放、顺序播放、或者指定延迟播放、等更多编排方式。
 *
 * 插值器限定的数据类型
 * IntEvaluator
 * FloatEvaluator
 * ArgbEvaluator
 * TypeEvaluator
 *
 * 系统提供的插值器
 * AccelerateDecelerateInterpolator
 * AccelerateInterpolator
 * AnticipateInterpolator
 * AnticipateOvershootInterpolator
 * BounceInterpolator
 * CycleInterpolator
 * DecelerateInterpolator
 * LinearInterpolator
 * OvershootInterpolator
 * TimeInterpolator
 */
class PrimaryAni : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_primary_ani)

        val valueAnimator = ValueAnimator()
        valueAnimator.addUpdateListener {
            val curValue= it.animatedValue

        }
        valueAnimator.duration = 1000
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.repeatCount = 2
        valueAnimator.repeatMode = RESTART
        valueAnimator.setEvaluator(FloatEvaluator())
        valueAnimator.start()
    }

}

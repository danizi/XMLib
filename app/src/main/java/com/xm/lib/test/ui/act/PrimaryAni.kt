package com.xm.lib.test.ui.act

import android.animation.*
import android.animation.ValueAnimator.RESTART
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.TextView
import com.xm.lib.common.log.BKLog
import com.xm.lib.test.R
import android.animation.AnimatorInflater
import android.animation.AnimatorSet


/**
 *
 * Android View移动的六种方法 https://blog.csdn.net/zxwd2015/article/details/52332538
 * 动画分为
 * 逐帧动画 https://www.jianshu.com/p/609b6d88798d
 * 补间动画 android:pivotX 和 android:pivotY 属性的含义 https://blog.csdn.net/hust_twj/article/details/78587989
 * 属性动画 例子 https://blog.csdn.net/qq1271396448/article/details/80674905
 *
 * 淡入淡出 ：alpha
 * 位移     ：translate
 * 缩放     ：scale
 * 旋转     ：rotate
 *
 *
 * https://www.jianshu.com/p/51f6576cf20e 官方文档中文解释
 *
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
 * AnimatorSet    ：将单个动画关联在一起，可以顺序播放、顺序播放、或者指定延迟播放、等更多编排方式。动画组也可以嵌套。
 * TimeAnimator   ：
 *
 * AnimatorSet
 * play（a1）.with（a2）     a1和a2同时开始
 * play（a1）.before（a2）   先a1后a2
 * play（a1）.after（a2）    先a2后a1
 *
 * <animator>
 * <objectAnimator>
 * <set>
 *
 *
 * 估值器
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
 *
 * View的属性有哪些呢 进入View 查看encodeProperties函數
 */
class PrimaryAni : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_primary_ani)

        val tv = findViewById<TextView>(R.id.tv)
        val btnValueAnimator = findViewById<TextView>(R.id.btn_value_animator)
        val btnObjectAnimator = findViewById<TextView>(R.id.btn_object_animator)
        val btnAnimatorSet = findViewById<TextView>(R.id.btn_animator_set)

        btnValueAnimator.setOnClickListener {
            valueAnimatorTest(tv)
        }

        btnObjectAnimator.setOnClickListener {
            objectAnimatorTest(tv)
        }

        btnAnimatorSet.setOnClickListener {
            animatorSetTest(tv)
        }
    }

    private fun objectAnimatorTest(view: View) {
        val anim = ObjectAnimator.ofFloat(view, "translationX", 0f, 500f)
        anim.duration = 1000
        anim.addListener(object : Animator.AnimatorListener {
            /**
             * 动画重复时调用
             */
            override fun onAnimationRepeat(animation: Animator?) {
                BKLog.d("onAnimationRepeat")
            }

            /**
             * 动画结束或者被取消时调用
             */
            override fun onAnimationEnd(animation: Animator?) {
                BKLog.d("onAnimationEnd")
            }

            /**
             * 动画取消时调用
             */
            override fun onAnimationCancel(animation: Animator?) {
                BKLog.d("onAnimationCancel")
            }

            /**
             * 动画开始时调用
             */
            override fun onAnimationStart(animation: Animator?) {
                BKLog.d("onAnimationStart")
            }
        })
        anim.start()
    }

    private fun valueAnimatorTest(view: View) {
        val valueAnimator = ValueAnimator.ofFloat(0f, 500f)
        valueAnimator.addUpdateListener {
            val curValue = it.animatedValue as Float
            BKLog.d("curValue:$curValue")
            //根据变化的值向右边滑动
            view.alpha
            view.scrollTo(-curValue.toInt(), 0)
        }
        //动画执行的时间
        valueAnimator.duration = 3000
        //差值器设置，其实就是按照某个公式变化
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        //动画重复的次数
        //valueAnimator.repeatCount = 1
        //动画重复的模式
        //valueAnimator.repeatMode = RESTART
        //设置
        valueAnimator.setEvaluator(FloatEvaluator())
        //启动动画
        valueAnimator.start()
        //取消动画
        //valueAnimator.cancel()
    }

    private fun animatorSetTest(view: View) {
        val animTranslationX = ObjectAnimator.ofFloat(view, "translationX", 0f, 500f)
        val animTranslationY = ObjectAnimator.ofFloat(view, "translationY", 0f, 500f)
        val animAlpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.5f)
        val animX = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f)
        val set = AnimatorSet()
        /**
         * 报错：Circular dependencies cannot exist in AnimatorSet
         * 原因：AnimatorSet的play、with、before、after函数不可复用同一个Animator
         */
        set.play(animTranslationX)
                .with(animTranslationY)
                .before(animAlpha)
                .after(animX)
        set.setTarget(view)
        set.duration = 10000
        set.start()
    }
}

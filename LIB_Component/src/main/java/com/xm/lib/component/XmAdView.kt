package com.xm.lib.component

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.view.*
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.ImageViewTarget
import java.lang.Exception


/**
 * 广告弹框 ps:布局根节点千万不用使用约束布局
 */
class XmAdView(context: Context?, builder: Builder) : Dialog(context!!) {

    private var activity: Activity? = null
    private var bkColor: Int? = android.R.color.transparent
    private var canceledOnTouchOutside: Boolean? = false
    private var view: View? = null
    private var ivClose: ImageView? = null
    private var ivAd: ImageView? = null

    init {
        activity = builder.activity
        bkColor = builder.bkColor
        canceledOnTouchOutside = builder.canceledOnTouchOutside
        view = builder.view

        if (view != null) {
            view = builder.view
        } else {
            view = LayoutInflater.from(getContext()).inflate(R.layout.view_ad, null, false)
            ivClose = view?.findViewById(R.id.iv_close)
            ivAd = view?.findViewById(R.id.iv_ad)
        }

        //设置对话框UI界面
        setContentView(view)
        //设置透明背景
        //设置透明背景
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        //window?.setBackgroundDrawableResource(bkColor!!)
        //设置点击屏幕dialog不消失
        setCanceledOnTouchOutside(canceledOnTouchOutside!!)
        //setDialogSize(this)
    }

    private fun setDialogSize(dg: Dialog) {
        val dialogWindow = dg.window
        val lp = dialogWindow!!.attributes
        dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialogWindow.setGravity(Gravity.CENTER)
        //显示的坐标
//        lp.y = ScreenUtil.dip2px(context, 10)
//        val width = getResources().getDimensionPixelOffset(R.dimen.d_width)
//        val height = getResources().getDimensionPixelOffset(R.dimen.d_height)
//        //dialog的大小
//        lp.width = width
//        lp.height = height
        dialogWindow.attributes = lp
    }

    /**
     * 关闭监听 ps:如果没有设置自定义view,则调用此方法
     * @param listener
     */
    fun setOnCloseListener(listener: View.OnClickListener) {
        ivClose?.setOnClickListener(listener)
    }

    /**
     * 点击广告监听 ps:如果没有设置自定义view,则调用此方法
     * @param listener
     */
    fun setOnAdListener(listener: View.OnClickListener) {
        ivAd?.setOnClickListener(listener)
    }

    /**
     * 设置宽高比例 ps:如果没有设置自定义view,则调用此方法
     * @param rate 宽高比例
     * @param space 左右的margin 单位dp
     */
    fun setAdRate(rate: Float, space: Int) {
//        val w = ScreenUtil.getNormalWH(activity)[0]
//        val para1 = ivAd?.layoutParams
//        //para1?.width = w - 2 * ScreenUtil.dip2px(context, space)
//        para1?.height = para1?.width!! / rate.toInt()
//        ivAd?.layoutParams = para1
    }

    fun setCover(picture: String?) {
        Glide.with(context)
                .load(picture)
                .asBitmap() // 制Glide返回一个Bitmap对象
                .into(TransformationUtilV2(ivAd!!, this@XmAdView))

//        Glide.with(context).load(picture)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.mipmap.ic_launcher)
//                .crossFade()
//                .into(TransformationUtilV2(ivAd!!))
//                .listener(object : RequestListener<String, GlideDrawable> {
//                    override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
//                        BKLog.e("")
//                        return false
//                    }
//
//                    override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
//                        BKLog.e("")
//                        return false
//                    }
//                })

//                .into(object : SimpleTarget<GlideDrawable>() {
//                    //ps:添加回调处理第一次不显示图片问题，但是没有动画了
//                    override fun onResourceReady(resource: GlideDrawable?, glideAnimation: GlideAnimation<in GlideDrawable>?) {
//                        ivAd?.setImageDrawable(resource)
//                    }
//                })
    }

    class Builder {
        var activity: Activity? = null
        /**
         * 上下文对象
         */
        var context: Context? = null
        /**
         * 弹框背景颜色
         */
        var bkColor: Int? = -1
        /**
         * 设置点击屏幕dialog是否消息 false表示点击窗体外不消失
         */
        var canceledOnTouchOutside: Boolean? = true
        /**
         * 窗体的自定义view
         */
        var view: View? = null

        fun activity(act: Activity): Builder {
            this.activity = act
            return this
        }

        fun context(ctx: Context): Builder {
            this.context = ctx
            return this
        }

        fun bkColor(bkColor: Int): Builder {
            this.bkColor = bkColor
            return this
        }

        fun canceledOnTouchOutside(canceledOnTouchOutside: Boolean): Builder {
            this.canceledOnTouchOutside = canceledOnTouchOutside
            return this
        }

        fun cusView(view: View): Builder {
            this.view = view
            return this
        }

        fun build(): XmAdView {
            check()
            return XmAdView(context, this)
        }

        private fun check() {
            if (context == null) {
                throw IllegalArgumentException("context is null")
            }

            if (bkColor == -1) {
                bkColor = Color.parseColor("#80000000")
            }
        }
    }
}

open class TransformationUtilV2(private val target: ImageView, private val xmAdView: XmAdView) : ImageViewTarget<Bitmap>(target) {

    override fun setResource(resource: Bitmap) {
        try {
            s(resource)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun s(resource: Bitmap) {
        view.setImageBitmap(resource)

        //获取原图的宽高
        val width = resource.width
        val height = resource.height

        //获取imageView的宽
        val imageViewWidth = target.width

        //计算缩放比例
        val sy = (imageViewWidth * 0.1).toFloat() / (width * 0.1).toFloat()

        //计算图片等比例放大后的高
        val imageViewHeight = (height * sy).toInt()
        val params = target.layoutParams
        params.height = imageViewHeight
        target.layoutParams = params

        //
        val parent = target.parent.parent.parent as ViewGroup
        parent.layoutParams.height = imageViewHeight
        xmAdView.show()
    }

    fun sy(resource: Bitmap): Float {
        val width = resource.width
        val height = resource.height
        val imageViewWidth = target.width
        return (imageViewWidth * 0.1).toFloat() / (width * 0.1).toFloat()
    }

    fun getNewBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        // 获得图片的宽高.
        val width = bitmap.width
        val height = bitmap.height
        // 计算缩放比例.
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // 取得想要缩放的matrix参数.
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        // 得到新的图片.
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }

}
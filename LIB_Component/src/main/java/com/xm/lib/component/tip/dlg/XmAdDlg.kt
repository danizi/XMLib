package com.xm.lib.component.tip.dlg

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Matrix
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.xm.lib.common.util.ScreenUtil
import com.xm.lib.component.R
import com.xm.lib.component.tip.dlg.core.XmDialogInterface
import java.lang.Exception

class XmAdDlg(private var context: Context?) : XmDialogInterface {
    private var adViewContainer: View? = null
    private var ivClose: ImageView? = null
    private var ivCover: ImageView? = null
    private var builder: AlertDialog.Builder? = null
    private var dlg: Dialog? = null
    private var dismissListener: XmDialogInterface.OnDismissListener? = null
    private var showListener: XmDialogInterface.OnShowListener? = null
    private var cancelListener: XmDialogInterface.OnCancelListener? = null

    init {
        builder = AlertDialog.Builder(context)
        adViewContainer = LayoutInflater.from(context).inflate(R.layout.view_ad, null, false)
        ivClose = adViewContainer?.findViewById(R.id.iv_close)
        ivCover = adViewContainer?.findViewById(R.id.iv_ad)
        ivClose?.visibility = View.GONE
        ivCover?.visibility = View.GONE
        builder?.setView(adViewContainer)
    }


    override fun setOnDismissListener(listener: XmDialogInterface.OnDismissListener) {
        this.dismissListener = listener
    }

    override fun setOnShowListener(listener: XmDialogInterface.OnShowListener) {
        this.showListener = listener
    }

    override fun setOnCancelListener(listener: XmDialogInterface.OnCancelListener) {
        this.cancelListener = listener
    }

    fun show(): XmAdDlg {
        dlg = builder?.create()
        dlg?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dlg?.setCanceledOnTouchOutside(false)// Sets whether this dialog is
        dlg?.show()
        dlg?.setOnDismissListener {
            dismissListener?.onDismiss(this)
        }
        dlg?.setOnCancelListener {
            cancelListener?.onCancel(this)
        }
        dlg?.setOnShowListener {
            showListener?.onShow(this)
        }
        setAd(url)
        return this
    }

    override fun cancel() {
        dlg?.cancel()
    }

    override fun dismiss() {
        dlg?.dismiss()
    }

    fun setClose(id: Int, listener: XmDialogInterface.OnClickListener): XmAdDlg {
        ivClose?.visibility = View.VISIBLE
        ivClose?.setOnClickListener {
            listener.onClick(this@XmAdDlg, 0)
        }
        ivClose?.setImageResource(R.mipmap.ad_close)
        return this
    }

    private var url: String = ""
    fun setAd(url: String, listener: XmDialogInterface.OnClickListener): XmAdDlg {
        this.url = url
        ivCover?.visibility = View.VISIBLE
        ivCover?.setOnClickListener {
            listener.onClick(this@XmAdDlg, 0)
        }
        return this!!
    }

    private fun setAd(url: String) {
//        Glide.with(context?.applicationContext).load(url)
//                .fitCenter()
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .crossFade(300)
//                .dontAnimate()
//                .into(object : SimpleTarget<GlideDrawable>() {
//                    //ps:添加回调处理第一次不显示图片问题，但是没有动画了
//                    override fun onResourceReady(resource: GlideDrawable?, glideAnimation: GlideAnimation<in GlideDrawable>?) {
//                        if (resource != null) {
//                            ivCover?.setImageDrawable(resource)
//                        }
//                    }
//                })
        Glide.with(context)
                .load(url)
                .asBitmap() // 制Glide返回一个Bitmap对象
                .into(AdaptationTransformation(ivCover!!, dlg))
    }

    /**
     * 图片设置适应尺寸
     */
    class AdaptationTransformation(private val target: ImageView, private val dia: Dialog?) : ImageViewTarget<Bitmap>(target) {

        override fun setResource(resource: Bitmap?) {
            try {
                val activity = target.context as Activity
                //获取原图的宽高
                val width = resource?.width
                val height = resource?.height
                val scale = width!!.toFloat() / height!!
                val maxWidth = Math.min(ScreenUtil.getNormalWH(activity)[0] - (2 * (ScreenUtil.getNormalWH(activity)[0] * 0.1f)).toInt(), width)
                val maxHeight = (maxWidth / scale).toInt()

                //设置展示宽高方式一
                dia?.setCanceledOnTouchOutside(true)
                val w = dia?.window
                val lp = w?.attributes
                lp?.width = maxWidth
                lp?.height = ViewGroup.LayoutParams.WRAP_CONTENT
                lp?.x = 0
                lp?.y = 0
                dia?.onWindowAttributesChanged(lp)

                //方式二 如果有广告关闭按钮使用下面方式设置宽高
                target.layoutParams.height = maxHeight

                target.setImageBitmap(resource)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
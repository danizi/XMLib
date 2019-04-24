package com.xm.lib.media.attachment

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.xm.lib.common.util.BrightnessUtil
import com.xm.lib.common.util.VolumeUtil
import com.xm.lib.media.R
import com.xm.lib.media.event.GestureObserver
import com.xm.lib.media.event.PhoneStateObserver
import com.xm.lib.media.event.PlayerObserver
import com.xm.lib.media.utils.GestureHelper


class AttachmentGesture(context: Context) : BaseAttachmentView(context) {

    private var viewHolder: ViewHolder? = null

    companion object {
        const val SEEK = "Seek"
        const val BRIGHTNESS = "Brightness"
        const val VOLUME = "Volume"
    }

    init {
        observer = object : PlayerObserver {
        }
        gestureObserver = object : GestureObserver {
            override fun onDownUp() {
                super.onDownUp()
                hide(SEEK)
                hide(BRIGHTNESS)
                hide(VOLUME)
            }
            override fun onVertical( type: String, present: Int) {
                super.onVertical( type, present)
                when (type) {
                    GestureHelper.VERTICAL_LEFT_VALUE -> {
                        show(BRIGHTNESS)
                        viewHolder?.brightnessPb?.max = 100
                        viewHolder?.brightnessPb?.progress = (BrightnessUtil.getScreenBrightness(context) * 100).toInt()
                        val brightnessPresent = BrightnessUtil.getScreenBrightness(context) + (1f - BrightnessUtil.getScreenBrightness(context) * present / 100f)
                        BrightnessUtil.setSystemBrightness(context, brightnessPresent)
                    }
                    GestureHelper.VERTICAL_RIGHT_VALUE -> {
                        show(VOLUME)
                        val curVolumePresent = VolumeUtil.getVolume(context)
                        viewHolder?.volumePb?.max = 100
                        viewHolder?.volumePb?.progress = (curVolumePresent * 100).toInt()
                        var volumePresent = curVolumePresent
                        volumePresent = if (present > 0) {
                            curVolumePresent + (1f - curVolumePresent * present) / 100f
                        } else {
                            curVolumePresent - (1f - curVolumePresent) * present / 100f
                        }
                        VolumeUtil.setVolume(context, volumePresent)
                    }
                }
            }
        }
        phoneObserver = object : PhoneStateObserver {}
    }

    override fun layoutId(): Int {
        return R.layout.attachment_gesture
    }

    override fun findViews(view: View?) {
        viewHolder = ViewHolder.create(view)
    }

    override fun initEvent() {
        super.initEvent()
    }

    private fun show(type: String) {
        when (type) {
            SEEK -> {
                viewHolder?.clSeek?.visibility = View.VISIBLE
                viewHolder?.clBrightness?.visibility = View.GONE
                viewHolder?.clVolume?.visibility = View.GONE
            }
            BRIGHTNESS -> {
                viewHolder?.clSeek?.visibility = View.GONE
                viewHolder?.clBrightness?.visibility = View.VISIBLE
                viewHolder?.clVolume?.visibility = View.GONE
            }
            VOLUME -> {
                viewHolder?.clSeek?.visibility = View.GONE
                viewHolder?.clBrightness?.visibility = View.GONE
                viewHolder?.clVolume?.visibility = View.VISIBLE
            }
        }
        xmVideoView?.bringChildToFront(this)
    }

    private fun hide(type: String) {
        when (type) {
            SEEK -> {
                viewHolder?.clSeek?.visibility = View.GONE
            }
            BRIGHTNESS -> {
                viewHolder?.clBrightness?.visibility = View.GONE
            }
            VOLUME -> {
                viewHolder?.clVolume?.visibility = View.GONE
            }
        }
    }

    private class ViewHolder private constructor(val clSeek: ConstraintLayout, val tvTime: TextView, val clVolume: ConstraintLayout, val ivVolume: ImageView, val volumePb: ProgressBar, val clBrightness: ConstraintLayout, val ivBrightness: ImageView, val brightnessPb: ProgressBar) {
        companion object {

            fun create(rootView: View?): ViewHolder {
                val clSeek = rootView?.findViewById<View>(R.id.cl_seek) as ConstraintLayout
                val tvTime = rootView.findViewById<View>(R.id.tv_time) as TextView
                val clVolume = rootView.findViewById<View>(R.id.cl_volume) as ConstraintLayout
                val ivVolume = rootView.findViewById<View>(R.id.iv_volume) as ImageView
                val volumePb = rootView.findViewById<View>(R.id.volume_pb) as ProgressBar
                val clBrightness = rootView.findViewById<View>(R.id.cl_brightness) as ConstraintLayout
                val ivBrightness = rootView.findViewById<View>(R.id.iv_brightness) as ImageView
                val brightnessPb = rootView.findViewById<View>(R.id.brightness_pb) as ProgressBar
                return ViewHolder(clSeek, tvTime, clVolume, ivVolume, volumePb, clBrightness, ivBrightness, brightnessPb)
            }
        }
    }

}
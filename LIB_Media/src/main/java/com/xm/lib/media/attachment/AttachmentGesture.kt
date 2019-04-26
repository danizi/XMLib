package com.xm.lib.media.attachment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.BrightnessUtil
import com.xm.lib.common.util.VolumeUtil
import com.xm.lib.media.R
import com.xm.lib.media.event.GestureObserver
import com.xm.lib.media.event.PhoneStateObserver
import com.xm.lib.media.event.PlayerObserver
import com.xm.lib.media.utils.GestureHelper


class AttachmentGesture(context: Context?) : BaseAttachmentView(context) {

    private var viewHolder: ViewHolder? = null
    private var rxPermissions: RxPermissions? = null

    companion object {
        const val TAG = "AttachmentGesture"
        const val SEEK = "Seek"
        const val BRIGHTNESS = "Brightness"
        const val VOLUME = "Volume"
    }

    init {
        rxPermissions = RxPermissions((context as AppCompatActivity))
        observer = object : PlayerObserver {
        }
        gestureObserver = object : GestureObserver {
            override fun onDownUp() {
                super.onDownUp()
                hide(SEEK)
                hide(BRIGHTNESS)
                hide(VOLUME)
            }

            override fun onVertical(type: String, present: Int) {
                super.onVertical(type, present)
                val damp = 0.7f
                when (type) {
                    GestureHelper.VERTICAL_LEFT_VALUE -> {
                        rxPermissions?.request(Manifest.permission.WRITE_SETTINGS)
                                ?.subscribe { granted ->
                                    if (granted!!) {
                                        BKLog.d(AttachmentGesture.TAG, "All requested permissions are granted")
                                    } else {
                                        Log.d("", "At least one permission is denied")
//                                        AlertDialog.Builder(context)
//                                                .setTitle("设置亮度")
//                                                .setMessage("设置权限")
//                                                .setNegativeButton("确认") { dialog, which ->
//                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                                        if (Settings.System.canWrite(context)) {
//                                                            // Do stuff here
//                                                        } else {
//                                                            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
//                                                            intent.data = Uri.parse("package:" + context.getPackageName())
//                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                                            context.startActivity(intent)
//                                                        }
//                                                    }
//                                                }.show()
                                    }
                                }
                        show(BRIGHTNESS)
                        val curScreenBrightness = BrightnessUtil.getScreenBrightness(context)
                        viewHolder?.brightnessPb?.max = 100
                        viewHolder?.brightnessPb?.progress = (curScreenBrightness * 100).toInt()
                        val brightnessPresent = if (present > 0) {
                            curScreenBrightness + (1f - curScreenBrightness * present * damp) / 100f
                        } else {
                            curScreenBrightness - (1f - curScreenBrightness) * present * damp / 100f
                        }
                        BrightnessUtil.setSystemBrightness(context, brightnessPresent)
                    }
                    GestureHelper.VERTICAL_RIGHT_VALUE -> {
                        show(VOLUME)
                        val curVolumePresent = VolumeUtil.getVolume(context)
                        viewHolder?.volumePb?.max = 100
                        viewHolder?.volumePb?.progress = (curVolumePresent * 100).toInt()
                        var volumePresent = curVolumePresent
                        volumePresent = if (present > 0) {
                            curVolumePresent + (1f - curVolumePresent * present * damp) / 100f
                        } else {
                            curVolumePresent - (1f - curVolumePresent) * present * damp / 100f
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
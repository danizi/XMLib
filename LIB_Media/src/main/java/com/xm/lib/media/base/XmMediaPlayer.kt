package com.xm.lib.media.base

import android.view.SurfaceHolder
import com.xm.lib.common.log.BKLog
import com.xm.lib.media.base.ijk.XmIJKPlayer

class XmMediaPlayer : IXmMediaPlayer() {
    override fun setSpeed(speed: Float) {
        mediaPlayer?.setSpeed(speed)
    }

    companion object {
        const val TAG = "XmMediaPlayer"
    }

     var mediaPlayer: IXmMediaPlayer? = null

    init {
        mediaPlayer = XmIJKPlayer()
        //mediaPlayer = XmOriginPlayer()
    }



    override fun getDuration(): Long {
        val duration = mediaPlayer?.getDuration()!!
        BKLog.i(TAG, "duration:$duration")
        return duration
    }

    override fun getCurrentPosition(): Long {
        val pos = mediaPlayer?.getCurrentPosition()!!
        BKLog.i(TAG, "currentPosition:$pos")
        return pos
    }

    override fun getVideoHeight(): Long {
        val height = mediaPlayer?.getVideoHeight()!!
        BKLog.d(TAG, "videoHeight:$height")
        return height
    }

    override fun getVideoWidth(): Long {
        val width = mediaPlayer?.getVideoWidth()!!
        BKLog.d(TAG, "videoWidth:$width")
        return width
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying()!!
    }

    override fun isLooping(): Boolean {
        return mediaPlayer?.isLooping()!!
    }

    override fun setLooping() {
        mediaPlayer?.setLooping()
    }

    override fun start() {
        mediaPlayer?.start()
    }

    override fun stop() {
        mediaPlayer?.stop()
    }

    override fun pause() {
        mediaPlayer?.pause()
    }

    override fun prepare() {
        mediaPlayer?.prepare()
    }

    override fun prepareAsync() {
        mediaPlayer?.prepareAsync()
    }

    override fun release() {
        mediaPlayer?.release()
    }

    override fun reset() {
        mediaPlayer?.reset()
    }

    override fun seekTo(msec: Int) {
        mediaPlayer?.seekTo(msec)
    }

    override fun setDataSource(path: String?) {
        mediaPlayer?.setDataSource(path)
    }

    override fun setDisplay(sh: SurfaceHolder?) {
        mediaPlayer?.setDisplay(sh)
    }

    override fun setOnVideoSizeChangedListener(listener: OnVideoSizeChangedListener) {
        mediaPlayer?.setOnVideoSizeChangedListener(listener)
    }

    override fun setOnErrorListener(listener: OnErrorListener) {
        mediaPlayer?.setOnErrorListener(listener)
    }

    override fun setOnInfoListener(listener: OnInfoListener) {
        mediaPlayer?.setOnInfoListener(listener)
    }

    override fun setOnPreparedListener(listener: OnPreparedListener) {
        mediaPlayer?.setOnPreparedListener(listener)
    }

    override fun setOnBufferingUpdateListener(listener: OnBufferingUpdateListener) {
        mediaPlayer?.setOnBufferingUpdateListener(listener)
    }

    override fun setOnSeekCompleteListener(listener: OnSeekCompleteListener) {
        mediaPlayer?.setOnSeekCompleteListener(listener)
    }

    override fun setOnCompletionListener(listener: OnCompletionListener) {
        mediaPlayer?.setOnCompletionListener(listener)
    }

    override fun setOnSubtitleDataListener(listener: OnSubtitleDataListener) {
        mediaPlayer?.setOnSubtitleDataListener(listener)
    }

    override fun setOnDrmInfoListener(listener: OnDrmInfoListener) {
        mediaPlayer?.setOnDrmInfoListener(listener)
    }

    override fun setOnMediaCodecSelectListener(listener: OnMediaCodecSelectListener) {
        mediaPlayer?.setOnMediaCodecSelectListener(listener)
    }

    override fun setOnNativeInvokeListener(listener: OnNativeInvokeListener) {
        mediaPlayer?.setOnNativeInvokeListener(listener)
    }

    override fun setOnControlMessageListener(listener: OnControlMessageListener) {
        mediaPlayer?.setOnControlMessageListener(listener)
    }
}
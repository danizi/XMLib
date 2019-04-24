package com.xm.lib.media.base.origin

import android.media.MediaPlayer
import android.os.Build
import android.view.SurfaceHolder
import com.xm.lib.media.base.*

class XmOriginPlayer : IXmMediaPlayer() {
    override fun setSpeed(speed: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var mediaPlayer = MediaPlayer() //IJKPlayer播放器

    override fun getDuration(): Long {
        return mediaPlayer.duration.toLong()
    }

    override fun getCurrentPosition(): Long {
        return mediaPlayer.currentPosition.toLong()
    }

    override fun getVideoHeight(): Long {
        return mediaPlayer.videoHeight.toLong()
    }

    override fun getVideoWidth(): Long {
        return mediaPlayer.videoWidth.toLong()
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun isLooping(): Boolean {
        return mediaPlayer.isLooping
    }

    override fun setLooping() {
        mediaPlayer.isLooping = true
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun stop() {
        mediaPlayer.stop()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun prepare() {
        mediaPlayer.prepare()
    }

    override fun prepareAsync() {
        mediaPlayer.prepareAsync()
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun reset() {
        mediaPlayer.reset()
    }

    override fun seekTo(msec: Int) {
        mediaPlayer.seekTo(msec)
    }

    override fun setDataSource(path: String?) {
        mediaPlayer.setDataSource(path)
    }

    override fun setDisplay(sh: SurfaceHolder?) {
        mediaPlayer.setDisplay(sh)
    }

    override fun setOnVideoSizeChangedListener(listener: OnVideoSizeChangedListener) {
        mediaPlayer.setOnVideoSizeChangedListener { mp, width, height ->
            listener.onVideoSizeChanged(this, width, height)
        }
    }

    override fun setOnErrorListener(listener: OnErrorListener) {
        mediaPlayer.setOnErrorListener { mp, what, extra ->
            listener.onError(this, what, extra)
        }
    }

    override fun setOnInfoListener(listener: OnInfoListener) {
        mediaPlayer.setOnInfoListener { mp, what, extra ->
            listener.onInfo(this, what, extra)
        }
    }

    override fun setOnPreparedListener(listener: OnPreparedListener) {
        mediaPlayer.setOnPreparedListener {
            listener.onPrepared(this)
        }
    }

    override fun setOnBufferingUpdateListener(listener: OnBufferingUpdateListener) {
        mediaPlayer.setOnBufferingUpdateListener { mp, percent ->
            listener.onBufferingUpdate(this, percent)
        }
    }

    override fun setOnSeekCompleteListener(listener: OnSeekCompleteListener) {
        mediaPlayer.setOnSeekCompleteListener {
            listener.onSeekComplete(this)
        }
    }

    override fun setOnCompletionListener(listener: OnCompletionListener) {
        mediaPlayer.setOnCompletionListener {
            listener.onCompletion(this)
        }
    }

    override fun setOnSubtitleDataListener(listener: OnSubtitleDataListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            mediaPlayer.setOnSubtitleDataListener { mp, data ->
                listener.onSubtitleData(this, data)
            }
        }
    }

    override fun setOnDrmInfoListener(listener: OnDrmInfoListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mediaPlayer.setOnDrmInfoListener { mp, drmInfo ->
                listener.onDrmInfo(this, drmInfo)
            }
        }
    }

    @Deprecated("原生播放器不支持")
    override fun setOnMediaCodecSelectListener(listener: OnMediaCodecSelectListener) {
    }

    @Deprecated("原生播放器不支持")
    override fun setOnNativeInvokeListener(listener: OnNativeInvokeListener) {
    }

    @Deprecated("原生播放器不支持")
    override fun setOnControlMessageListener(listener: OnControlMessageListener) {
    }
}
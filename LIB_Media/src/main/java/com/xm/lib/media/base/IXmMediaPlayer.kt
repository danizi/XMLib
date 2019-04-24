package com.xm.lib.media.base

import android.media.MediaPlayer
import android.media.SubtitleData
import android.os.Bundle
import android.view.SurfaceHolder

abstract class IXmMediaPlayer {

    companion object {
        /*-----------------------------
         * 共用回调状态码
         */
        val MEDIA_INFO_UNKNOWN = 1
        val MEDIA_INFO_STARTED_AS_NEXT = 2
        val MEDIA_INFO_VIDEO_RENDERING_START = 3
        val MEDIA_INFO_VIDEO_TRACK_LAGGING = 700
        val MEDIA_INFO_BUFFERING_START = 701
        val MEDIA_INFO_BUFFERING_END = 702
        val MEDIA_INFO_NETWORK_BANDWIDTH = 703
        val MEDIA_INFO_BAD_INTERLEAVING = 800
        val MEDIA_INFO_NOT_SEEKABLE = 801
        val MEDIA_INFO_METADATA_UPDATE = 802
        val MEDIA_INFO_TIMED_TEXT_ERROR = 900
        val MEDIA_INFO_UNSUPPORTED_SUBTITLE = 901
        val MEDIA_INFO_SUBTITLE_TIMED_OUT = 902
        const val TAG = "IXmMediaPlayer"
    }

    abstract fun getDuration(): Long
    abstract fun getCurrentPosition(): Long
    abstract fun getVideoHeight(): Long
    abstract fun getVideoWidth(): Long
    abstract fun isPlaying(): Boolean
    abstract fun isLooping(): Boolean
    abstract fun setLooping()

    abstract fun start()
    abstract fun stop()
    abstract fun pause()
    abstract fun prepare()
    abstract fun prepareAsync()
    abstract fun release()
    abstract fun reset()
    abstract fun seekTo(msec: Int)
    abstract fun setDataSource(path: String?)
    abstract fun setDisplay(sh: SurfaceHolder?)

    abstract fun setSpeed(speed: Float)

    /*-----------------------------
     * 监听
     */
    abstract fun setOnVideoSizeChangedListener(listener: OnVideoSizeChangedListener)

    abstract fun setOnErrorListener(listener: OnErrorListener)
    abstract fun setOnInfoListener(listener: OnInfoListener)
    abstract fun setOnPreparedListener(listener: OnPreparedListener)
    abstract fun setOnBufferingUpdateListener(listener: OnBufferingUpdateListener)
    abstract fun setOnSeekCompleteListener(listener: OnSeekCompleteListener)
    abstract fun setOnCompletionListener(listener: OnCompletionListener)

    /*-----------------------------
     * 原生播放器特有的监听
     */
    abstract fun setOnSubtitleDataListener(listener: OnSubtitleDataListener)

    abstract fun setOnDrmInfoListener(listener: OnDrmInfoListener)

    /*-----------------------------
     * IJKPlayer播放器特有监听
     */
    abstract fun setOnMediaCodecSelectListener(listener: OnMediaCodecSelectListener)

    abstract fun setOnNativeInvokeListener(listener: OnNativeInvokeListener)
    abstract fun setOnControlMessageListener(listener: OnControlMessageListener)

}

interface OnVideoSizeChangedListener {
    fun onVideoSizeChanged(mp: IXmMediaPlayer, width: Int, height: Int, sar_num: Int = -1, sar_den: Int = -1)
}

interface OnErrorListener {
    fun onError(mp: IXmMediaPlayer, what: Int, extra: Int): Boolean
}

interface OnInfoListener {
    fun onInfo(mp: IXmMediaPlayer, what: Int, extra: Int): Boolean
}

interface OnPreparedListener {
    fun onPrepared(mp: IXmMediaPlayer)
}

interface OnBufferingUpdateListener {
    fun onBufferingUpdate(mp: IXmMediaPlayer, percent: Int)
}

interface OnSeekCompleteListener {
    fun onSeekComplete(mp: IXmMediaPlayer)
}

interface OnCompletionListener {
    fun onCompletion(mp: IXmMediaPlayer)
}

interface OnSubtitleDataListener {
    fun onSubtitleData(mp: IXmMediaPlayer, data: SubtitleData)
}

interface OnDrmInfoListener {
    fun onDrmInfo(mp: IXmMediaPlayer, drmInfo: MediaPlayer.DrmInfo)
}

interface OnMediaCodecSelectListener {
    fun onMediaCodecSelect(mp: IXmMediaPlayer, mimeType: String, profile: Int, level: Int): String
}

interface OnNativeInvokeListener {
    fun onNativeInvoke(mp: IXmMediaPlayer, what: Int, args: Bundle): Boolean
}

interface OnControlMessageListener {
    fun onControlResolveSegmentUrl(mp: IXmMediaPlayer, segment: Int): String
}





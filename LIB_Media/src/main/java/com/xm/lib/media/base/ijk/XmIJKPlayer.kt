package com.xm.lib.media.base.ijk

import android.view.SurfaceHolder
import com.xm.lib.media.base.*
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class XmIJKPlayer : IXmMediaPlayer() {
    private var ijkMediaPlayer: IjkMediaPlayer = IjkMediaPlayer() //IJKPlayer播放器

    init {
//        ijkMediaPlayer = IjkMediaPlayer()
        IjkMediaPlayer.loadLibrariesOnce(null)
        IjkMediaPlayer.native_profileBegin("libijkplayer.so")

        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "soundtouch", 1)       //todo 加了这个才能设置倍速播放
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "libijkplayer.so", 1)
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 0)
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "start-on-prepared", 1)
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48)
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1)
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1)
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1)
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1)
//        // 跳过循环滤波
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
//        // 设置最长分析时长
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzemaxduration", 100L);
//        // 通过立即清理数据包来减少等待时长
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "flush_packets", 1L);
//        // 暂停输出直到停止后读取足够的数据包
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0L);
//        // 网络不好的情况下进行丢包
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1L);
//        // 去掉音频
//        //ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "an", 1);
//        // 不查询stream_info，直接使用
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER,"find_stream_info", 0);
//        // 等待开始之后才绘制
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "render-wait-start", 1);


//        ijkMediaPlayer.setOption(1, "analyzemaxduration", 100L)
//        ijkMediaPlayer.setOption(1, "probesize", 10240L)
//        ijkMediaPlayer.setOption(1, "flush_packets", 1L)
//        ijkMediaPlayer.setOption(4, "packet-buffering", 0L)
//        ijkMediaPlayer.setOption(4, "framedrop", 1L)
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1)
    }

    override fun setSpeed(speed: Float) {
        ijkMediaPlayer.setSpeed(speed)
    }

    override fun getDuration(): Long {
        return ijkMediaPlayer.duration
    }

    override fun getCurrentPosition(): Long {
        return ijkMediaPlayer.currentPosition
    }

    override fun getVideoHeight(): Long {
        return ijkMediaPlayer.videoHeight.toLong()
    }

    override fun getVideoWidth(): Long {
        return ijkMediaPlayer.videoWidth.toLong()
    }

    override fun isPlaying(): Boolean {
        return ijkMediaPlayer.isPlaying
    }

    override fun isLooping(): Boolean {
        return ijkMediaPlayer.isLooping
    }

    override fun setLooping() {
        ijkMediaPlayer.isLooping = true
    }

    override fun start() {
        ijkMediaPlayer.setScreenOnWhilePlaying(true)
        ijkMediaPlayer.start()
    }

    override fun stop() {
        ijkMediaPlayer.stop()
    }

    override fun pause() {
        ijkMediaPlayer.setScreenOnWhilePlaying(false)
        ijkMediaPlayer.pause()
    }

    @Deprecated("ijkPlayer不支持")
    override fun prepare() {

    }

    override fun prepareAsync() {
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "soundtouch", 1)       //todo 加了这个才能设置倍速播放
        ijkMediaPlayer.prepareAsync()
    }

    override fun release() {
        ijkMediaPlayer.release()
    }

    override fun reset() {
        ijkMediaPlayer.reset()
    }

    override fun seekTo(msec: Int) {
        ijkMediaPlayer.seekTo(msec.toLong())
    }

    override fun setDataSource(path: String?) {
        ijkMediaPlayer.dataSource = path
    }

    override fun setDisplay(sh: SurfaceHolder?) {
        ijkMediaPlayer.setDisplay(sh)
    }

    override fun setOnVideoSizeChangedListener(listener: OnVideoSizeChangedListener) {
        ijkMediaPlayer.setOnVideoSizeChangedListener { mp, width, height, sar_num, sar_den ->
            listener.onVideoSizeChanged(this, width, height, sar_num, sar_den)
        }
    }

    override fun setOnErrorListener(listener: OnErrorListener) {
        ijkMediaPlayer.setOnErrorListener { mp, what, extra ->
            listener.onError(this, what, extra)
        }
    }

    override fun setOnInfoListener(listener: OnInfoListener) {
        ijkMediaPlayer.setOnInfoListener { mp, what, extra ->
            listener.onInfo(this, what, extra)
        }
    }

    override fun setOnPreparedListener(listener: OnPreparedListener) {
        ijkMediaPlayer.setOnPreparedListener {
            listener.onPrepared(this)
        }
    }

    override fun setOnBufferingUpdateListener(listener: OnBufferingUpdateListener) {
        ijkMediaPlayer.setOnBufferingUpdateListener { mp, percent ->
            listener.onBufferingUpdate(this, percent)
        }
    }

    override fun setOnSeekCompleteListener(listener: OnSeekCompleteListener) {
        ijkMediaPlayer.setOnSeekCompleteListener {
            listener.onSeekComplete(this)
        }
    }

    override fun setOnCompletionListener(listener: OnCompletionListener) {
        ijkMediaPlayer.setOnCompletionListener {
            listener.onCompletion(this)
        }
    }

    @Deprecated("ijkPlayer不支持")
    override fun setOnSubtitleDataListener(listener: OnSubtitleDataListener) {
    }

    @Deprecated("ijkPlayer不支持")
    override fun setOnDrmInfoListener(listener: OnDrmInfoListener) {
    }

    override fun setOnMediaCodecSelectListener(listener: OnMediaCodecSelectListener) {
        ijkMediaPlayer.setOnMediaCodecSelectListener { mp, mimeType, profile, level ->
            listener.onMediaCodecSelect(this, mimeType, profile, level)
        }
    }

    override fun setOnNativeInvokeListener(listener: OnNativeInvokeListener) {
        ijkMediaPlayer.setOnNativeInvokeListener { what, args ->
            listener.onNativeInvoke(this, what, args)
        }
    }

    override fun setOnControlMessageListener(listener: OnControlMessageListener) {
        ijkMediaPlayer.setOnControlMessageListener {
            listener.onControlResolveSegmentUrl(this, it)
        }
    }
}
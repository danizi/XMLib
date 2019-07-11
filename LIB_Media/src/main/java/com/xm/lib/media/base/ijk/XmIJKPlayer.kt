package com.xm.lib.media.base.ijk

import android.view.SurfaceHolder
import com.xm.lib.media.base.*
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class XmIJKPlayer : IXmMediaPlayer() {

    private var ijkMediaPlayer: IjkMediaPlayer = IjkMediaPlayer() //IJKPlayer播放器

    init {
        ijkMediaPlayer = IjkMediaPlayer()
        IjkMediaPlayer.loadLibrariesOnce(null)
        IjkMediaPlayer.native_profileBegin("libijkplayer.so")
        // todo 配置在prepareAsync才设置成功。
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "soundtouch", 1)       //todo 加了这个才能设置倍速播放
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "libijkplayer.so", 1)
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 5L)       //todo 允许掉帧不然在低端手机点播播放高清视频会音画不同步,但还是解决不够完全
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "start-on-prepared", 1)
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48)
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1)
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1)
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1)
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1)
//
//        // 清空DNS,有时因为在APP里面要播放多种类型的视频(如:MP4,直播,直播平台保存的视频,和其他http视频), 有时会造成因为DNS的问题而报10000问题的
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", 1)
//
//        //开启硬解码
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1)
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0)
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32.toLong())
//        //设置播放前的探测时间 1,达到首屏秒开效果
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "analyzeduration", "1")
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "probsize", "4096")
//        //设置是否开启环路过滤: 0开启，画面质量高，解码开销大，48关闭，画面质量差点，解码开销小
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48L)
//        // 设置最长分析时长
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzemaxduration", 100L)
//        //播放前的探测Size
//        ijkMediaPlayer.setOption(1, "probesize", 1024L)
//        //播放重连次数
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "reconnect", 3)
//        //最大fps
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-fps", 30)
//        //某些视频在SeekTo的时候，会跳回到拖动前的位置，这是因为视频的关键帧的问题，通俗一点就是FFMPEG不兼容，视频压缩过于厉害，seek只支持关键帧，出现这个情况就是原始的视频文件中i 帧比较少
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
        //加了这个才能设置倍速播放
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "soundtouch", 1)
        //允许掉帧不然在低端手机点播播放高清视频会音画不同步,但还是解决不够完全
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 5L)
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "start-on-prepared", 1)
        //设置是否开启环路过滤: 0开启，画面质量高，解码开销大，48关闭，画面质量差点，解码开销小
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48L)
        // 清空DNS,有时因为在APP里面要播放多种类型的视频(如:MP4,直播,直播平台保存的视频,和其他http视频), 有时会造成因为DNS的问题而报10000问题的
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", 1)
        //开启硬解码
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1)
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0)
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32.toLong())
        //设置播放前的探测时间 1,达到首屏秒开效果
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "analyzeduration", "1")
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "probsize", "4096")
        //播放重连次数
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "reconnect", 3)
        //最大fps
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-fps", 30)
        //某些视频在SeekTo的时候，会跳回到拖动前的位置，这是因为视频的关键帧的问题，通俗一点就是FFMPEG不兼容，视频压缩过于厉害，seek只支持关键帧，出现这个情况就是原始的视频文件中i 帧比较少
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1)
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
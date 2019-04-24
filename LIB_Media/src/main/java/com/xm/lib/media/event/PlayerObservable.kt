package com.xm.lib.media.event

import android.media.MediaPlayer
import android.media.SubtitleData
import com.xm.lib.media.base.IXmMediaPlayer
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * 播放回调观察者
 * 手势回调观察者
 */
class PlayerObservable {

    private val observers: ConcurrentLinkedQueue<PlayerObserver>? = ConcurrentLinkedQueue()

    @Synchronized
    fun addObserver(o: PlayerObserver?) {
        observers?.add(o)
    }

    @Synchronized
    fun deleteObserver(o: PlayerObserver?) {
        observers?.remove(o)
    }

    fun notifyObserversControlResolveSegmentUrl(mp: IXmMediaPlayer, segment: Int) {
        check()
        for (observer in observers!!) {
            observer.onControlResolveSegmentUrl(mp, segment)
        }
    }

    fun notifyObserversMediaCodecSelect(mp: IXmMediaPlayer, mimeType: String, profile: Int, level: Int) {
        check()
        for (observer in observers!!) {
            observer.onMediaCodecSelect(mp, mimeType,profile,level)
        }
    }

    fun notifyObserversDrmInfo(mp: IXmMediaPlayer, drmInfo: MediaPlayer.DrmInfo) {
        check()
        for (observer in observers!!) {
            observer.onDrmInfo(mp, drmInfo)
        }
    }

    fun notifyObserversSubtitleData(mp: IXmMediaPlayer, data: SubtitleData) {
        check()
        for (observer in observers!!) {
            observer.onSubtitleData(mp,data)
        }
    }

    fun notifyObserversCompletion(mp: IXmMediaPlayer) {
        check()
        for (observer in observers!!) {
            observer.onCompletion(mp)
        }
    }

    fun notifyObserversSeekComplete(mp: IXmMediaPlayer) {
        check()
        for (observer in observers!!) {
            observer.onSeekComplete(mp)
        }
    }

    fun notifyObserversBufferingUpdate(mp: IXmMediaPlayer, percent: Int) {
        check()
        for (observer in observers!!) {
            observer.onBufferingUpdate(mp,percent)
        }
    }

    fun notifyObserversPrepared(mp: IXmMediaPlayer) {
        check()
        for (observer in observers!!) {
            observer.onPrepared(mp)
        }
    }

    fun notifyObserversInfo(mp: IXmMediaPlayer, what: Int, extra: Int) {
        check()
        for (observer in observers!!) {
            observer.onInfo(mp, what,extra)
        }
    }

    fun notifyObserversError(mp: IXmMediaPlayer, what: Int, extra: Int) {
        check()
        for (observer in observers!!) {
            observer.onError(mp, what,extra)
        }
    }

    fun notifyObserversVideoSizeChanged(mp: IXmMediaPlayer, width: Int, height: Int, sar_num: Int, sar_den: Int) {
        check()
        for (observer in observers!!) {
            observer.onVideoSizeChanged(mp, width,height,sar_num,sar_den)
        }
    }

    private fun check() {
        if (observers == null) {
            throw NullPointerException("observers is null")
        }
    }

}
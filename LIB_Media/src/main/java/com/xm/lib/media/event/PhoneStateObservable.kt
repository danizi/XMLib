package com.xm.lib.media.event

import java.util.concurrent.ConcurrentLinkedQueue

/**
 * 来电去电
 * 拔出、插上耳机
 * 网络变化
 * 息屏锁屏变化
 * 手机横竖摆放变化
 */
class PhoneStateObservable {
    private val observers: ConcurrentLinkedQueue<PhoneStateObserver>? = ConcurrentLinkedQueue()

    @Synchronized
    fun addObserver(o: PhoneStateObserver?) {
        observers?.add(o)
    }

    @Synchronized
    fun deleteObserver(o: PhoneStateObserver?) {
        observers?.remove(o)
    }

    private fun check() {
        if (observers == null) {
            throw NullPointerException("observers is null")
        }
    }

    fun notifyObserversBatteryLevel(type: String, batteryPct: Float) {
        check()
        for (observer in observers!!) {
            observer.onBatteryLevel(type, batteryPct)
        }
    }

    fun notifyObserversHeadset(type: String, state: Int?) {
        check()
        for (observer in observers!!) {
            observer.onHeadset(type, state)
        }
    }

    fun notifyObserversNetworkConnectChange(connect: Boolean, type: Int) {
        check()
        for (observer in observers!!) {
            observer.onChange(connect, type)
        }
    }

    fun notifyObserversPhoneState() {
        check()
        for (observer in observers!!) {
            observer.onPhoneState()
        }
    }

    fun notifyObserversStateRinging() {
        check()
        for (observer in observers!!) {
            observer.onStateRinging()
        }
    }

    fun notifyObserversPowerConnection(charger: Boolean, type: String) {
        check()
        for (observer in observers!!) {
            observer.onPowerConnection(charger, type)
        }
    }

}
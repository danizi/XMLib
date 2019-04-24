package com.xm.lib.media.event

import java.util.concurrent.ConcurrentLinkedQueue

class GestureObservable {
    private val observers: ConcurrentLinkedQueue<GestureObserver>? = ConcurrentLinkedQueue()

    @Synchronized
    fun addObserver(o: GestureObserver?) {
        observers?.add(o)
    }

    @Synchronized
    fun deleteObserver(o: GestureObserver?) {
        observers?.remove(o)
    }

    private fun check() {
        if (observers == null) {
            throw NullPointerException("observers is null")
        }
    }

    fun notifyObserversDown() {
        check()
        for (observer in observers!!) {
            observer.onDown()
        }
    }

    fun notifyObserversDownUp() {
        check()
        for (observer in observers!!) {
            observer.onDownUp()
        }
    }

    fun notifyObserversClick() {
        check()
        for (observer in observers!!) {
            observer.onClick()
        }
    }

    fun notifyObserversHorizontal(present: Int) {
        check()
        for (observer in observers!!) {
            observer.onHorizontal(present)
        }
    }

    fun notifyObserversVertical(type: String, present: Int) {
        check()
        for (observer in observers!!) {
            observer.onVertical(type, present)
        }
    }

    fun notifyObserversDoubleClick() {
        check()
        for (observer in observers!!) {
            observer.onDoubleClick()
        }
    }

    fun notifyObserversScaleEnd(scaleFactor: Float) {
        check()
        for (observer in observers!!) {
            observer.onScaleEnd(scaleFactor)
        }
    }
}
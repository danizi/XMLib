package com.xm.lib.media.event

import com.xm.lib.common.log.BKLog

interface GestureObserver {
    val TAG: String
        get() = "GestureObserver"

    fun onDoubleClick() {
        BKLog.d(TAG, "onDoubleClick")
    }

    fun onVertical(type: String, present: Int) {
        BKLog.d(TAG, "onVertical type:$type present:$present")
    }

    fun onHorizontal(present: Int) {
        BKLog.d(TAG, "onHorizontal present:$present")
    }

    fun onClick() {
        BKLog.d(TAG, "onClick")
    }

    fun onScaleEnd(scaleFactor: Float) {
        BKLog.d(TAG, "onScaleEnd scaleFactor:$scaleFactor")
    }

    fun onDown(){
        BKLog.d(TAG, "onDown")
    }
    fun onDownUp(){
        BKLog.d(TAG, "onDownUp")
    }
}
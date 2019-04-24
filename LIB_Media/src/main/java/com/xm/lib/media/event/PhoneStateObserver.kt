package com.xm.lib.media.event

import com.xm.lib.common.log.BKLog


interface PhoneStateObserver {

    val TAG: String?
        get() = "PhoneStateObserver"

    fun onBatteryLevel(type: String, batteryPct: Float){ BKLog.d(TAG,"onBatteryLevel type:$type batteryPct:$batteryPct") }

    fun onHeadset(type: String, state: Int?){ BKLog.d(TAG,"onHeadset type:$type state:$state") }

    fun onChange(isConnect: Boolean, type: Int){ BKLog.d(TAG,"onChange isConnect:$isConnect type:$type") }

    fun onPhoneState(){ BKLog.d(TAG,"onPhoneState") }
    fun onStateRinging(){ BKLog.d(TAG,"onStateRinging") }

    fun onPowerConnection(charger: Boolean, type: String){ BKLog.d(TAG,"onPowerConnection charger:$charger type:$type") }

    fun state(isBright: Boolean){ BKLog.d(TAG,"state isBright$isBright") }
    fun userPresent(){ BKLog.d(TAG,"userPresent") }
    fun closeSystemDialogs(){ BKLog.d(TAG,"closeSystemDialogs") }
}
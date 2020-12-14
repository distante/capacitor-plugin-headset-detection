package com.saninnsalas.capacitor.restart.models

import android.media.AudioDeviceInfo

enum class HeadphoneType(val type: Int) {
    TYPE_BLUETOOTH_A2DP(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP),
    TYPE_AUX_LINE(AudioDeviceInfo.TYPE_AUX_LINE),
    TYPE_WIRED_HEADPHONES(AudioDeviceInfo.TYPE_WIRED_HEADPHONES),
    TYPE_WIRED_HEADSET(AudioDeviceInfo.TYPE_WIRED_HEADSET);

    companion object {
        fun enumOf(value: Int): HeadphoneType? = HeadphoneType.values().find { it.type == value }
    }
}

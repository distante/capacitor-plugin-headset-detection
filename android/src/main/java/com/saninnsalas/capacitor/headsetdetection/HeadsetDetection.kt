package com.saninnsalas.capacitor.headsetdetection

import android.media.AudioDeviceCallback
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.util.Log
import com.getcapacitor.NativePlugin
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.saninnsalas.capacitor.headsetdetection.models.HeadsetDevice
import com.saninnsalas.capacitor.headsetdetection.models.HeadsetPluginResponse

@NativePlugin
class HeadsetDetection : Plugin() {
    private val TAG = "HeadsetDetectionPlugin"
    private val headphoneDeviceTypes = listOf(
            AudioDeviceInfo.TYPE_BLUETOOTH_A2DP,
            AudioDeviceInfo.TYPE_AUX_LINE,
            AudioDeviceInfo.TYPE_WIRED_HEADPHONES,
            AudioDeviceInfo.TYPE_WIRED_HEADSET,
            // AudioDeviceInfo.TYPE_USB_HEADSET Requires ApiLevel 26
    )

    private var connectedHeadset: HeadsetDevice? = null

    @PluginMethod
    fun start(_call: PluginCall) {
        val audioManager = context.getSystemService(AudioManager::class.java)
        audioManager.registerAudioDeviceCallback(deviceCallBack, null)
    }

    private fun updateHeadphonesConnected() {
        val isConnected = connectedHeadset != null
        val response = HeadsetPluginResponse(isConnected, connectedHeadset)
        val jsObject = response.toJSObject();
        Log.i(TAG, "response $jsObject")
        notifyListeners("ConnectedHeadphones", jsObject)
    }

    private val deviceCallBack: AudioDeviceCallback =  object: AudioDeviceCallback() {
        override fun onAudioDevicesAdded(addedDevices: Array<out AudioDeviceInfo>?) {
            if(addedDevices == null) return
            val headphones = addedDevices.filter { isHeadphoneDevice(it) }
            headphones.forEach {
                Log.d(TAG, "Headphones addedDevices ${it.productName}, type: ${it.type}")
                connectedHeadset = HeadsetDevice(it.id, it.type, it.productName.toString())
            }
            updateHeadphonesConnected()
        }

        override fun onAudioDevicesRemoved(removedDevices: Array<out AudioDeviceInfo>?) {
            if(removedDevices == null) return
            val headphones = removedDevices.filter { isHeadphoneDevice(it) }
            headphones.forEach {
                Log.d(TAG, "Headphones removedDevices ${it.productName}, type: ${it.type}")
                connectedHeadset = null
            }
            updateHeadphonesConnected()
        }
    }

    override fun handleOnDestroy() {
        super.handleOnDestroy()
        val audioManager = context.getSystemService(AudioManager::class.java)
        audioManager.unregisterAudioDeviceCallback(deviceCallBack)
    }


    private fun isHeadphoneDevice(device: AudioDeviceInfo ): Boolean {
        // Filter idea from https://github.com/google/talkback/blob/92eb6dd4461e53fc904052b7fbe9b77ddfbf930a/utils/src/main/java/HeadphoneStateMonitor.java#L125
        val isSink = device.isSink;
        val type = device.type
        return isSink && headphoneDeviceTypes.contains(type)
    }
}

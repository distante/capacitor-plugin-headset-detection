package com.saninnsalas.capacitor.restart

import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioDeviceCallback
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.getcapacitor.NativePlugin
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.saninnsalas.capacitor.restart.models.HeadsetDevice
import com.saninnsalas.capacitor.restart.models.HeadsetPluginResponse


@NativePlugin
class Restart : Plugin() {
    private val TAG = "HeadsetDetectionPlugin"
    private val headphoneDeviceTypes = listOf(
            AudioDeviceInfo.TYPE_BLUETOOTH_A2DP,
            AudioDeviceInfo.TYPE_AUX_LINE,
            AudioDeviceInfo.TYPE_WIRED_HEADPHONES,
            AudioDeviceInfo.TYPE_WIRED_HEADSET,
            // AudioDeviceInfo.TYPE_USB_HEADSET Requires ApiLevel 26
    )

    private val connectedHeadsets = mutableMapOf<Int, HeadsetDevice>()

    @PluginMethod
    fun start(_call: PluginCall) {
        val audioManager = context.getSystemService(AudioManager::class.java)
        audioManager.registerAudioDeviceCallback(deviceCallBack, null)
    }

    private fun updateHeadphonesConnected() {
        val isConnected = connectedHeadsets.isNotEmpty()
        val response = HeadsetPluginResponse(isConnected, connectedHeadsets.values.toList())
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
                connectedHeadsets[it.id] = HeadsetDevice(it.id, it.type, it.productName.toString())
            }
            updateHeadphonesConnected()
        }

        override fun onAudioDevicesRemoved(removedDevices: Array<out AudioDeviceInfo>?) {
            if(removedDevices == null) return
            val headphones = removedDevices.filter { isHeadphoneDevice(it) }
            headphones.forEach {
                Log.d(TAG, "Headphones removedDevices ${it.productName}, type: ${it.type}")
                connectedHeadsets.remove(it.id)
            }
            updateHeadphonesConnected()
        }
    }

    override fun handleOnDestroy() {
        super.handleOnDestroy()
        activity.unregisterReceiver(bluetoothBroadcastReceiver)
        activity.unregisterReceiver(headsetBroadcastReceiver)
    }

    private val bluetoothBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(
                context: Context,
                intent: Intent
        ) {
            val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

            val majorDeviceClass = device?.bluetoothClass?.majorDeviceClass;

            if(majorDeviceClass != BluetoothClass.Device.Major.AUDIO_VIDEO) {
                return
            }

            val name = device.name

            // BLUETOOTH AUDIO_VIDEO device
            val action = intent.action

            if (action == BluetoothDevice.ACTION_ACL_CONNECTED) {
                Toast.makeText(
                        context,
                        "$name BLUETOOTH is now Connected",
                        Toast.LENGTH_LONG
                ).show()
                Log.i(TAG, "$name Connected")
            } else if (action == BluetoothDevice.ACTION_ACL_DISCONNECTED ) {
                Toast.makeText(
                        context,
                        "$name BLUETOOTH is disconnected",
                        Toast.LENGTH_LONG
                ).show()
                Log.i(TAG, "$name BLUETOOTH Disconnected")
            }
        }
    }

    private val headsetBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(
                context: Context,
                intent: Intent
        ) {
            val extras: Bundle? = intent.extras

            val headsetState = intent.getIntExtra("state", -1)
            val headsetName = intent.getStringExtra("name")

            when(headsetState) {
                1 -> {
                    Toast.makeText(
                            context,
                            "$headsetName Headset is now Connected",
                            Toast.LENGTH_LONG
                    ).show()
                    Log.i(TAG, "$headsetName Headset Connected")
                }
                0 -> {
                    Toast.makeText(
                            context,
                            "$headsetName Headset is now Disconnected",
                            Toast.LENGTH_LONG
                    ).show()
                    Log.i(TAG, "$headsetName Headset Disconnected")
                }
                else -> {
                    Log.i(TAG, "No idea what is happening with $headsetName")
                }
            }

        }
    }

    private fun isHeadphoneDevice(device: AudioDeviceInfo ): Boolean {
        // Filter idea from https://github.com/google/talkback/blob/92eb6dd4461e53fc904052b7fbe9b77ddfbf930a/utils/src/main/java/HeadphoneStateMonitor.java#L125
        val isSink = device.isSink;
        val type = device.type
        return isSink && headphoneDeviceTypes.contains(type)
    }
}

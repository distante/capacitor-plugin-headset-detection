package com.saninnsalas.capacitor.restart.models

import com.getcapacitor.JSArray
import com.getcapacitor.JSObject

data class HeadsetPluginResponse(val isConnected: Boolean, val devices: List<HeadsetDevice>) {

    /**
     * will produce a JSObject with this structure
     * {
     * isConnected:boolean
     * devices : headsetDevice[]
     * }
     */
    fun toJSObject(): JSObject {
        val jsObject = JSObject()

        jsObject.put("isConnected", isConnected)
        jsObject.put("devices", JSArray(devices.map { it.toJSObject() }))
        return jsObject
    }
}

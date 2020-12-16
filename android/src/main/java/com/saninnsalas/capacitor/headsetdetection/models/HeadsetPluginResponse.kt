package com.saninnsalas.capacitor.headsetdetection.models

import com.getcapacitor.JSArray
import com.getcapacitor.JSObject

data class HeadsetPluginResponse(val isConnected: Boolean, val device: HeadsetDevice? ) {

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
        jsObject.put("device", device?.toJSObject())
        return jsObject
    }
}

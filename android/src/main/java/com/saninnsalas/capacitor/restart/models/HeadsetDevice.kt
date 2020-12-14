package com.saninnsalas.capacitor.restart.models

import com.getcapacitor.JSObject

data class HeadsetDevice(val id: Int, val typeCode: Int, val name: String) {
    val type = HeadphoneType.enumOf(typeCode)

    fun toJSObject(): JSObject {
        val jsonObject = JSObject()
        jsonObject.put("id", id)
        jsonObject.put("typeCode", typeCode)
        jsonObject.put("type", type)
        jsonObject.put("name", type)

        return jsonObject
    }
}

package com.saninnsalas.capacitor.restart

import android.util.Log
import com.getcapacitor.*

@NativePlugin
class Restart : Plugin() {
    @PluginMethod
    fun restart(call: PluginCall) {
        Log.e("SANINN", "RESTART CALLED")
    }
}

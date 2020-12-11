package com.saninnsalas.capacitor.restart

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.getcapacitor.NativePlugin
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import kotlin.system.exitProcess


@NativePlugin
class Restart : Plugin() {
    private val TAG = "RestartPlugin"

    @PluginMethod
    fun restart(call: PluginCall) {
        doRestartOne(context)
    }


    private fun doRestartOne(context: Context) {
        try {
            //fetch the packagemanager so we can get the default launch activity
            // (you can replace this intent with any other activity if you want
            val startActivityIntent = context.packageManager.getLaunchIntentForPackage(
                    context.packageName
            )

            if (startActivityIntent == null) {
                Log.e(TAG, "We could not build the start activity intent")
                return
            }

            startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            //create a pending intent so the application is restarted after System.exit(0) was called.
            // We use an AlarmManager to call this intent in 100ms
            val mPendingIntentId = 223344
            val mPendingIntent = PendingIntent
                    .getActivity(context, mPendingIntentId, startActivityIntent,
                            PendingIntent.FLAG_CANCEL_CURRENT)

//            val alarmService = context.getSystemService(Context.ALARM_SERVICE)[AlarmManager.RTC, System.currentTimeMillis() + 100] = mPendingIntent
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent)
            Log.e(TAG, "Restarting in 100 ms")
            //kill the application
            exitProcess(0)


        } catch (ex: Exception) {
            Log.e(TAG, "Was not able to restart application")
        }
    }

}

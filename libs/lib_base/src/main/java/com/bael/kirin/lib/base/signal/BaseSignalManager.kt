package com.bael.kirin.lib.base.signal

import android.content.Context
import android.content.Intent
import android.content.IntentFilter

/**
 * Created by ErickSumargo on 01/06/20.
 */

abstract class BaseSignalManager(private val context: Context) {
    private val broadcastReceiver = BroadcastReceiver(::receiveBroadcast)

    protected fun registerSignals(signals: List<String>) {
        val intentFilter = IntentFilter().apply { signals.forEach(::addAction) }
        context.registerReceiver(broadcastReceiver, intentFilter)
    }

    private fun receiveBroadcast(intent: Intent?) {
        if (intent == null) return
        transformSignal(intent)
    }

    abstract fun transformSignal(intent: Intent)

    fun unregisterSignalReceiver() {
        context.unregisterReceiver(broadcastReceiver)
    }
}

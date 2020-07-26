package com.bael.kirin.lib.arch.signal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by ErickSumargo on 01/06/20.
 */

class BroadcastReceiver(
    private val callback: (Intent?) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) = callback(intent)
}

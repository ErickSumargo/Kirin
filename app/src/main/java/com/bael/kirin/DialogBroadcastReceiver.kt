package com.bael.kirin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bael.kirin.constant.SUBJECT_DISMISS_DIALOG

/**
 * Created by ErickSumargo on 01/06/20.
 */

class DialogBroadcastReceiver constructor(
    private val callback: () -> Unit
) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            SUBJECT_DISMISS_DIALOG.hashCode().toString() -> callback()
        }
    }
}

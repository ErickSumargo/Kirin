package com.bael.kirin

import android.os.Handler
import android.os.Message

/**
 * Created by ErickSumargo on 01/06/20.
 */

class MessengerHandler constructor(
    private val onDialogListener: OnDialogListener
) : Handler() {
    override fun handleMessage(msg: Message) {
        when (msg.what) {
            SUBJECT_DISMISS_DIALOG -> onDialogListener.onDismissDialog()
            else -> super.handleMessage(msg)
        }
    }
}

package com.bael.kirin

import android.os.Handler
import android.os.Message
import com.bael.kirin.constant.SUBJECT_DISMISS_DIALOG

/**
 * Created by ErickSumargo on 01/06/20.
 */

class MessengerHandler constructor(
    private val onDialogListener: OnDialogListener
) : Handler() {
    override fun handleMessage(msg: Message) {
        when (msg.what) {
            SUBJECT_DISMISS_DIALOG -> onDialogListener.onDialogDismissed()
            else -> super.handleMessage(msg)
        }
    }
}

package com.bael.kirin.feature.translation.signal

import android.content.Context
import android.content.Intent
import com.bael.kirin.feature.translation.constant.SUBJECT_BACKGROUND_DISMISSED
import com.bael.kirin.feature.translation.constant.SUBJECT_BACKGROUND_SHOWN
import com.bael.kirin.feature.translation.constant.SUBJECT_DISMISS_BACKGROUND
import com.bael.kirin.feature.translation.constant.SUBJECT_EXTRA_QUERY
import com.bael.kirin.feature.translation.constant.SUBJECT_INSTANT_TRANSLATE
import com.bael.kirin.feature.translation.constant.SUBJECT_STOP_SERVICE
import com.bael.kirin.feature.translation.signal.Signal.BackgroundDismissed
import com.bael.kirin.feature.translation.signal.Signal.BackgroundShown
import com.bael.kirin.feature.translation.signal.Signal.DismissBackground
import com.bael.kirin.feature.translation.signal.Signal.InstantTranslate
import com.bael.kirin.feature.translation.signal.Signal.StopService
import com.bael.kirin.lib.arch.base.BaseSignalManager
import com.bael.kirin.lib.arch.signal.SignalReceiver

/**
 * Created by ErickSumargo on 01/06/20.
 */

class SignalManager(
    context: Context,
    receiver: SignalReceiver<Signal>
) : BaseSignalManager(context),
    SignalReceiver<Signal> by receiver {

    init {
        listOf(
            SUBJECT_BACKGROUND_SHOWN,
            SUBJECT_DISMISS_BACKGROUND,
            SUBJECT_BACKGROUND_DISMISSED,
            SUBJECT_STOP_SERVICE,
            SUBJECT_INSTANT_TRANSLATE
        ).also(::registerSignals)
    }

    override fun transformSignal(intent: Intent) {
        val signal = when (intent.action) {
            SUBJECT_BACKGROUND_SHOWN -> {
                BackgroundShown
            }
            SUBJECT_DISMISS_BACKGROUND -> {
                DismissBackground
            }
            SUBJECT_BACKGROUND_DISMISSED -> {
                BackgroundDismissed
            }
            SUBJECT_STOP_SERVICE -> {
                StopService
            }
            SUBJECT_INSTANT_TRANSLATE -> {
                val query = intent.getStringExtra(SUBJECT_EXTRA_QUERY).orEmpty()
                InstantTranslate(query)
            }
            else -> null
        }
        signal?.let(::receiveSignal)
    }
}

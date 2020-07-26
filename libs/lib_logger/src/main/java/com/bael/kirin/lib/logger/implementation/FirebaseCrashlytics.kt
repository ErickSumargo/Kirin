package com.bael.kirin.lib.logger.implementation

import com.bael.kirin.lib.logger.contract.Logger
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

/**
 * Created by ErickSumargo on 01/06/20.
 */

class FirebaseCrashlytics @Inject constructor() : Logger {
    private val crashlytics: FirebaseCrashlytics by lazy {
        FirebaseCrashlytics.getInstance()
    }

    override fun log(cause: Exception) {
        crashlytics.recordException(cause)
    }
}

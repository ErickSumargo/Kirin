package com.bael.kirin.lib.analytics.implementation

import android.os.Bundle
import com.bael.kirin.lib.analytics.contract.Tracker
import com.bael.kirin.lib.logger.contract.Logger
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

/**
 * Created by ErickSumargo on 01/06/20.
 */

class FirebaseAnalytics @Inject constructor(
    logger: Logger
) : Tracker,
    Logger by logger {

    override fun track(
        event: String,
        data: Bundle
    ) {
        try {
            Firebase.analytics.logEvent(event, data)
        } catch (cause: Exception) {
            log(cause)
        }
    }
}

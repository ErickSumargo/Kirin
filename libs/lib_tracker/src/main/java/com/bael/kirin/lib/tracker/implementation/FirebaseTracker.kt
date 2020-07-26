package com.bael.kirin.lib.tracker.implementation

import com.bael.kirin.lib.logger.contract.Logger
import com.bael.kirin.lib.tracker.contract.Tracker
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ServerValue.increment
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

/**
 * Created by ErickSumargo on 01/06/20.
 */

class FirebaseTracker @Inject constructor(
    logger: Logger
) : Tracker,
    Logger by logger {

    override fun <T> track(event: String, value: T) {
        try {
            val reference = getReference(event)
            reference.push().setValue(value)
        } catch (cause: Exception) {
            log(cause)
        }
    }

    override fun trackIncrement(event: String) {
        try {
            val reference = getReference(event)
            reference.setValue(increment(1))
        } catch (cause: Exception) {
            log(cause)
        }
    }

    private fun getReference(node: String): DatabaseReference {
        return Firebase.database.getReference("/$node")
    }
}

package com.bael.kirin.lib.messaging.base

import android.annotation.SuppressLint
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by ErickSumargo on 01/06/20.
 */

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
abstract class BaseMessagingService : FirebaseMessagingService() {
    protected abstract val messageId: String

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.data.isEmpty()) return
        if (message.notification?.channelId != messageId) return

        onPayloadReceived(message)
    }

    abstract fun onPayloadReceived(payload: RemoteMessage)
}

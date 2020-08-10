package com.bael.kirin.feature.translation.service.messaging

import android.annotation.SuppressLint
import com.bael.kirin.feature.translation.constant.SUBJECT_MESSAGING_TRANSLATION
import com.bael.kirin.feature.translation.di.entry.EntryPoint
import com.bael.kirin.feature.translation.preference.Preference
import com.bael.kirin.lib.messaging.base.BaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.EntryPointAccessors

/**
 * Created by ErickSumargo on 01/06/20.
 */

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MessagingService : BaseMessagingService() {
    override val messageId: String = SUBJECT_MESSAGING_TRANSLATION

    private lateinit var preference: Preference

    override fun onCreate() {
        super.onCreate()
        EntryPointAccessors.fromApplication(
            this,
            EntryPoint::class.java
        ).apply {
            preference = accessPreference()
        }
    }

    override fun onPayloadReceived(payload: RemoteMessage) {
        val url = payload.data["url"] ?: return
        preference.googleTranslateUrl = url
    }
}

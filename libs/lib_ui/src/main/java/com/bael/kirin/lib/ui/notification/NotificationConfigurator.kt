package com.bael.kirin.lib.ui.notification

import android.widget.RemoteViews

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface NotificationConfigurator {
    val notificationId: Int
    val isOngoing: Boolean

    fun constructLayout(): RemoteViews
}

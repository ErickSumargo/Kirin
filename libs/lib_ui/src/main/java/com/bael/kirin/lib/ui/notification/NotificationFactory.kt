package com.bael.kirin.lib.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import androidx.core.app.NotificationCompat.Builder
import androidx.core.app.NotificationCompat.DecoratedCustomViewStyle
import androidx.core.app.NotificationManagerCompat
import com.bael.kirin.lib.resource.app.AppInfo
import com.bael.kirin.lib.resource.util.Util.minMarshmallowSdk
import com.bael.kirin.lib.resource.util.Util.minOreoSdk
import com.bael.kirin.lib.ui.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by ErickSumargo on 01/06/20.
 */

class NotificationFactory @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appInfo: AppInfo
) {
    private val manager: NotificationManager by lazy {
        context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    private lateinit var configurator: NotificationConfigurator

    fun create(configurator: NotificationConfigurator) {
        this.configurator = configurator
    }

    private fun constructBuilder(): Builder {
        return Builder(context, appInfo.packageName).apply {
            setSmallIcon(R.drawable.ic_kirin_notification)
            if (::configurator.isInitialized) {
                setOngoing(configurator.isOngoing)
                setCustomContentView(configurator.constructLayout())
            } else {
                setContentTitle(appInfo.name)
                setContentText(appInfo.description)
            }
            setStyle(DecoratedCustomViewStyle())
        }
    }

    private fun constructChannel(): NotificationChannel? {
        if (!minOreoSdk) return null
        return NotificationChannel(appInfo.packageName, appInfo.description, IMPORTANCE_LOW)
    }

    fun push() {
        val builder = constructBuilder()
        val channel = constructChannel()
        channel?.let(manager::createNotificationChannel)

        if (::configurator.isInitialized) {
            manager.notify(configurator.notificationId, builder.build())
        } else {
            manager.notify(appInfo.name.hashCode(), builder.build())
        }
    }

    fun isActive(id: Int): Boolean {
        if (!minMarshmallowSdk) return false
        return manager.activeNotifications.find { it.id == id } != null
    }

    fun dismiss(id: Int) {
        NotificationManagerCompat.from(context).cancel(id)
    }
}

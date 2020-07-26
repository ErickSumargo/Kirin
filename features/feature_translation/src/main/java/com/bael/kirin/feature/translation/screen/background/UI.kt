package com.bael.kirin.feature.translation.screen.background

import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.PendingIntent.getBroadcast
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.view.Window.FEATURE_NO_TITLE
import android.view.WindowManager.LayoutParams.FLAG_DIM_BEHIND
import android.widget.RemoteViews
import com.bael.kirin.feature.translation.R
import com.bael.kirin.feature.translation.constant.SUBJECT_BACKGROUND_DISMISSED
import com.bael.kirin.feature.translation.constant.SUBJECT_BACKGROUND_SHOWN
import com.bael.kirin.feature.translation.constant.SUBJECT_EXTRA_EXIT_SCREEN
import com.bael.kirin.feature.translation.constant.SUBJECT_EXTRA_QUERY
import com.bael.kirin.feature.translation.constant.SUBJECT_INSTANT_TRANSLATE
import com.bael.kirin.feature.translation.constant.SUBJECT_STOP_SERVICE
import com.bael.kirin.feature.translation.databinding.BackgroundLayoutBinding
import com.bael.kirin.feature.translation.preference.Preference
import com.bael.kirin.feature.translation.tracker.Tracker
import com.bael.kirin.lib.arch.base.BaseActivity
import com.bael.kirin.lib.resource.app.AppInfo
import com.bael.kirin.lib.ui.notification.NotificationConfigurator
import com.bael.kirin.lib.ui.notification.NotificationFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject
import com.bael.kirin.feature.translation.screen.settings.UI as UISettings
import com.bael.kirin.feature.translation.service.floating.UI as UIFloating

/**
 * Created by ErickSumargo on 01/06/20.
 */

@AndroidEntryPoint
@FlowPreview
@ExperimentalCoroutinesApi
class UI :
    BaseActivity<BackgroundLayoutBinding, Dispatcher.Factory, ViewModel>(),
    Renderer,
    Action {
    override val hideDefaultLayout: Boolean get() = true

    @Inject
    lateinit var appInfo: AppInfo

    @Inject
    lateinit var notification: NotificationFactory

    @Inject
    lateinit var preference: Preference

    @Inject
    lateinit var tracker: Tracker

    lateinit var dispatcher: Dispatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        removeWindowTitle()
        adjustDimBackground(useDim = false)
        super.onCreate(savedInstanceState)
        dispatcher = dispatcherFactory.create(
            renderer = this,
            action = this
        ).also { it.observe(lifecycleOwner = this) }
    }

    private fun removeWindowTitle() {
        supportRequestWindowFeature(FEATURE_NO_TITLE)
    }

    private fun adjustDimBackground(useDim: Boolean) {
        if (useDim) {
            window.addFlags(FLAG_DIM_BEHIND)
        } else {
            window.clearFlags(FLAG_DIM_BEHIND)
        }
    }

    override fun init() {
        adjustDimBackground(useDim = preference.useDimBackground)
        notifyBackgroundShown()
    }

    override fun instantTranslate(query: String) {
        tracker.trackInstantTranslate(query)

        Intent(SUBJECT_INSTANT_TRANSLATE).apply {
            putExtra(SUBJECT_EXTRA_QUERY, query)
        }.run(::sendBroadcast)

        finish()
    }

    override fun startService(query: String) {
        tracker.trackToggleService(active = true)
        tracker.trackInstantTranslate(query)

        startTranslatorService(query)
        showNotification()

        finish()
    }

    override fun openMainScreen(query: String) {
        tracker.trackDeferredInstantTranslate()

        Intent(this, UISettings::class.java).apply {
            addFlags(FLAG_ACTIVITY_NEW_TASK)

            putExtra(SUBJECT_EXTRA_QUERY, query)
            putExtra(SUBJECT_EXTRA_EXIT_SCREEN, true)
        }.run(::startActivity)

        finish()
    }

    override fun dismissBackground() = onStop()

    private fun notifyBackgroundShown() {
        Intent(SUBJECT_BACKGROUND_SHOWN).run(::sendBroadcast)
    }

    private fun startTranslatorService(query: String) {
        Intent(this, UIFloating::class.java).apply {
            putExtra(SUBJECT_EXTRA_QUERY, query)
        }.run(::startService)
    }

    private fun showNotification() {
        with(notification) {
            create(object : NotificationConfigurator {
                override fun constructLayout(): RemoteViews {
                    return RemoteViews(
                        appInfo.packageName,
                        R.layout.notification_service_layout
                    ).also { remote ->
                        val intent = Intent(SUBJECT_STOP_SERVICE)
                        val pendingIntent = getBroadcast(
                            this@UI,
                            0,
                            intent,
                            FLAG_UPDATE_CURRENT
                        )
                        remote.setOnClickPendingIntent(R.id.closeServiceLabel, pendingIntent)
                    }
                }

                override val notificationId: Int = appInfo.id
                override val isOngoing: Boolean = true
            })

            push()
        }
    }

    private fun closeBackground() {
        finish()
        overridePendingTransition(0, 0)
    }

    private fun disableEditingMode() {
        Intent(SUBJECT_BACKGROUND_DISMISSED).run(::sendBroadcast)
    }

    override fun onStop() {
        closeBackground()
        disableEditingMode()
        super.onStop()
    }

    override fun onDestroy() {
        dispatcher.clear()
        super.onDestroy()
    }
}

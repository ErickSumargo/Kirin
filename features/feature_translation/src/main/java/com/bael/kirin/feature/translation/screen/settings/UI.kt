package com.bael.kirin.feature.translation.screen.settings

import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.PendingIntent.getBroadcast
import android.content.Intent
import android.net.Uri.parse
import android.os.Bundle
import android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION
import android.widget.RemoteViews
import androidx.fragment.app.DialogFragment
import com.bael.kirin.feature.translation.R
import com.bael.kirin.feature.translation.constant.SUBJECT_EXTRA_QUERY
import com.bael.kirin.feature.translation.constant.SUBJECT_STOP_SERVICE
import com.bael.kirin.feature.translation.databinding.SettingsItemLayoutBinding
import com.bael.kirin.feature.translation.databinding.SettingsLayoutBinding
import com.bael.kirin.feature.translation.preference.Preference
import com.bael.kirin.feature.translation.tracker.Tracker
import com.bael.kirin.feature.translation.util.Util.canDrawOverlays
import com.bael.kirin.lib.base.activity.BaseActivity
import com.bael.kirin.lib.resource.app.AppInfo
import com.bael.kirin.lib.resource.ext.showMessage
import com.bael.kirin.lib.resource.ext.textHtmlOf
import com.bael.kirin.lib.resource.ext.textOf
import com.bael.kirin.lib.resource.util.Util.minMarshmallowSdk
import com.bael.kirin.lib.ui.dialog.permission.PermissionDialog
import com.bael.kirin.lib.ui.dialog.permission.PermissionDialogListener
import com.bael.kirin.lib.ui.dialog.progress.ProgressDialog
import com.bael.kirin.lib.ui.notification.NotificationConfigurator
import com.bael.kirin.lib.ui.notification.NotificationFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject
import com.bael.kirin.feature.translation.service.floating.UI as UIFloating

/**
 * Created by ErickSumargo on 01/06/20.
 */

@AndroidEntryPoint
@FlowPreview
@ExperimentalCoroutinesApi
class UI :
    BaseActivity<SettingsLayoutBinding, Dispatcher.Factory, ViewModel>(),
    Renderer,
    Action,
    PermissionDialogListener {
    @Inject
    lateinit var appInfo: AppInfo

    @Inject
    lateinit var packet: Packet

    @Inject
    lateinit var preference: Preference

    @Inject
    lateinit var notification: NotificationFactory

    @Inject
    lateinit var tracker: Tracker

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dispatcherFactory.create(
            viewModel = viewModel,
            renderer = this,
            action = this
        ).observe(lifecycleOwner = this)

        viewModel.setup(
            configSetupCompleted = preference.configSetupCompleted
        )
    }

    override fun renderVersion(version: String) = launch {
        viewBinder.versionLabel.also { label ->
            label.text = version
        }
    }

    override fun renderSettings(
        settings: Map<Pair<String, Boolean>, Pair<String, String>>
    ) = launch {
        viewBinder.settingsLayout.also { layout ->
            layout.removeAllViews()

            settings.forEach { (identifier, info) ->
                val viewItemBinder = SettingsItemLayoutBinding.inflate(layoutInflater)

                viewItemBinder.toggleSwitch.also { switch ->
                    val (key, value) = identifier

                    switch.isChecked = value
                    switch.setOnCheckedChangeListener { _, checked ->
                        preference.write(key, checked)
                        tracker.trackPreferenceUpdate(key, checked)
                    }
                }

                viewItemBinder.titleLabel.also { label ->
                    label.text = info.first
                }

                viewItemBinder.descriptionLabel.also { label ->
                    label.text = info.second
                }

                layout.addView(viewItemBinder.root)
            }
        }
    }

    override fun showSetupProgress() {
        progressDialog = ProgressDialog.create(
            message = textOf(R.string.progress_dialog_configuring_description)
        ).also { dialog ->
            dialog.logger = logger
            dialog.show(supportFragmentManager, TAG_PROGRESS_DIALOG)
        }
    }

    override fun handleSetupFailed(message: String) {
        showMessage(message)
        finish()
    }

    override fun onSetupSuccess() {
        if (::progressDialog.isInitialized) {
            progressDialog.dismiss()
        }

        preference.configSetupCompleted = true
        viewModel.checkPermissionDrawOverlays()
    }

    override fun checkPermissionDrawOverlays() {
        if (!minMarshmallowSdk || canDrawOverlays) {
            tracker.trackToggleService(active = true)

            startTranslatorService()
            showNotification()

            if (packet.exitScreen) finish()
        } else {
            requestPermissionDialogDrawOverlays()
        }
    }

    private fun requestPermissionDialogDrawOverlays() {
        PermissionDialog.create(
            title = "",
            message = textHtmlOf(R.string.permission_dialog_description, appInfo.name),
            actionNegativeText = textOf(R.string.permission_dialog_action_negative_text),
            actionPositiveText = textOf(R.string.permission_dialog_action_positive_text)
        ).also { dialog ->
            dialog.logger = logger
            dialog.show(supportFragmentManager, TAG_PERMISSION_DIALOG)
        }
    }

    private fun startTranslatorService() {
        Intent(this, UIFloating::class.java).apply {
            putExtra(SUBJECT_EXTRA_QUERY, packet.query)
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

    override fun requestPermissionDrawOverlays() {
        if (minMarshmallowSdk) {
            val intent = Intent(
                ACTION_MANAGE_OVERLAY_PERMISSION,
                parse("package:${appInfo.packageName}")
            )
            startActivityForResult(intent, RC_OVERLAY_PERMISSION)
        }
    }

    override fun closeScreen() = finish()

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        viewModel.denyPermissionDrawOverlays()
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        viewModel.allowPermissionDrawOverlays()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_OVERLAY_PERMISSION -> {
                viewModel.checkPermissionDrawOverlays()
            }
        }
    }

    companion object {
        private const val RC_OVERLAY_PERMISSION: Int = 100

        private const val TAG_PROGRESS_DIALOG: String = "progress_dialog"
        private const val TAG_PERMISSION_DIALOG: String = "permission_dialog"
    }
}

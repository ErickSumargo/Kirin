package com.bael.kirin.feature.translation.di.module

import android.content.Intent.EXTRA_PROCESS_TEXT
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import com.bael.kirin.feature.translation.databinding.BackgroundLayoutBinding
import com.bael.kirin.feature.translation.screen.background.Intent
import com.bael.kirin.feature.translation.screen.background.Intent.Initialize
import com.bael.kirin.feature.translation.screen.background.Intent.InstantTranslate
import com.bael.kirin.feature.translation.screen.background.Intent.OpenMainScreen
import com.bael.kirin.feature.translation.screen.background.Intent.StartService
import com.bael.kirin.feature.translation.screen.background.Packet
import com.bael.kirin.feature.translation.screen.background.State
import com.bael.kirin.feature.translation.screen.background.ViewModel
import com.bael.kirin.feature.translation.util.Util.canDrawOverlays
import com.bael.kirin.lib.resource.app.AppInfo
import com.bael.kirin.lib.resource.util.Util.minMarshmallowSdk
import com.bael.kirin.lib.ui.notification.NotificationFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by ErickSumargo on 01/06/20.
 */

@Module
@InstallIn(ActivityComponent::class)
@ExperimentalCoroutinesApi
object BackgroundScreenModule {

    @ActivityScoped
    @Provides
    fun FragmentActivity.providePacket(): Packet {
        val selectedText = intent.getStringExtra(EXTRA_PROCESS_TEXT)
        return Packet(query = selectedText)
    }

    @ActivityScoped
    @Provides
    fun FragmentActivity.provideViewBinding(): BackgroundLayoutBinding {
        return BackgroundLayoutBinding.inflate(layoutInflater)
    }

    @ActivityScoped
    @Provides
    fun provideState(): State = State()

    @ActivityScoped
    @Provides
    fun FragmentActivity.provideIntent(
        appInfo: AppInfo,
        packet: Packet,
        notification: NotificationFactory
    ): Intent {
        if (!minMarshmallowSdk) return Initialize

        val hasSelectedQuery = packet.query.isNullOrBlank().not()
        return when {
            !hasSelectedQuery -> {
                Initialize
            }
            hasSelectedQuery && notification.isActive(appInfo.id) -> {
                InstantTranslate(packet.query.orEmpty())
            }
            hasSelectedQuery && canDrawOverlays -> {
                StartService(packet.query.orEmpty())
            }
            else -> {
                OpenMainScreen(packet.query.orEmpty())
            }
        }
    }

    @ActivityScoped
    @Provides
    fun FragmentActivity.provideViewModel(): Lazy<ViewModel> = viewModels()
}

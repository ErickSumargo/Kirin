package com.bael.kirin.feature.translation.screen.settings

import androidx.lifecycle.Observer
import com.bael.kirin.feature.translation.screen.settings.Intent.AllowPermissionDrawOverlays
import com.bael.kirin.feature.translation.screen.settings.Intent.CheckPermissionDrawOverlays
import com.bael.kirin.feature.translation.screen.settings.Intent.ConfigSetting
import com.bael.kirin.feature.translation.screen.settings.Intent.ConfigSetupFailed
import com.bael.kirin.feature.translation.screen.settings.Intent.ConfigSetupSuccess
import com.bael.kirin.feature.translation.screen.settings.Intent.DenyPermissionDrawOverlays
import com.bael.kirin.lib.base.di.assisted.DispatcherFactoryAssisted
import com.bael.kirin.lib.base.dispatcher.BaseDispatcher
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by ErickSumargo on 01/06/20.
 */

@ActivityScoped
@ExperimentalCoroutinesApi
class Dispatcher @AssistedInject constructor(
    viewModel: Lazy<ViewModel>,
    @Assisted arg0: Renderer,
    @Assisted arg1: Action
) : BaseDispatcher<State, Intent>(viewModel),
    Renderer by arg0,
    Action by arg1 {

    @AssistedInject.Factory
    interface Factory :
        DispatcherFactoryAssisted<Renderer, Action, Dispatcher>

    override fun dispatchStates(): Observer<Pair<State?, State>> {
        return Observer { (previousState, newState) ->
            if (previousState?.version != newState.version) {
                renderVersion(newState.version)
            }

            if (previousState?.settings != newState.settings) {
                renderSettings(newState.settings)
            }
        }
    }

    override fun dispatchIntent(): Observer<Intent?> {
        return Observer { intent ->
            when (intent) {
                is ConfigSetting -> {
                    showSetupProgress()
                }
                is ConfigSetupFailed -> {
                    handleSetupFailed(intent.message)
                }
                is ConfigSetupSuccess -> {
                    onSetupSuccess()
                }
                is CheckPermissionDrawOverlays -> {
                    checkPermissionDrawOverlays()
                }
                is AllowPermissionDrawOverlays -> {
                    requestPermissionDrawOverlays()
                }
                is DenyPermissionDrawOverlays -> {
                    closeScreen()
                }
            }
        }
    }
}

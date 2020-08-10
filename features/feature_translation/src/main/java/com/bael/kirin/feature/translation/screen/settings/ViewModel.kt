package com.bael.kirin.feature.translation.screen.settings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.bael.kirin.feature.translation.configurator.SetupConfigurator
import com.bael.kirin.feature.translation.screen.settings.Intent.AllowPermissionDrawOverlays
import com.bael.kirin.feature.translation.screen.settings.Intent.CheckPermissionDrawOverlays
import com.bael.kirin.feature.translation.screen.settings.Intent.ConfigSetting
import com.bael.kirin.feature.translation.screen.settings.Intent.ConfigSetupFailed
import com.bael.kirin.feature.translation.screen.settings.Intent.ConfigSetupSuccess
import com.bael.kirin.feature.translation.screen.settings.Intent.DenyPermissionDrawOverlays
import com.bael.kirin.lib.base.viewmodel.BaseViewModel
import com.bael.kirin.lib.threading.util.Util.IOThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.hilt.Assisted as HiltAssisted

/**
 * Created by ErickSumargo on 01/06/20.
 */

@ExperimentalCoroutinesApi
class ViewModel @ViewModelInject constructor(
    initState: State,
    initIntent: Intent?,
    @HiltAssisted savedStateHandle: SavedStateHandle,
    private val configurator: SetupConfigurator
) : BaseViewModel<State, Intent>(initState, initIntent, savedStateHandle) {

    fun setup() = launch(thread = IOThread) {
        configurator.setup { config ->
            val newIntent = when {
                config.isLoading() -> {
                    ConfigSetting
                }
                config.isError() -> {
                    ConfigSetupFailed(message = config.error?.message.orEmpty())
                }
                else -> {
                    ConfigSetupSuccess
                }
            }
            action(newIntent)
        }
    }

    fun checkPermissionDrawOverlays() = launch {
        val newIntent = CheckPermissionDrawOverlays
        action(newIntent)
    }

    fun allowPermissionDrawOverlays() = launch {
        val newIntent = AllowPermissionDrawOverlays
        action(newIntent)
    }

    fun denyPermissionDrawOverlays() = launch {
        val newIntent = DenyPermissionDrawOverlays
        action(newIntent)
    }
}

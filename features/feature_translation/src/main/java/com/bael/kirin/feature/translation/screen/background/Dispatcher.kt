package com.bael.kirin.feature.translation.screen.background

import android.content.Context
import androidx.lifecycle.Observer
import com.bael.kirin.feature.translation.screen.background.Intent.Initialize
import com.bael.kirin.feature.translation.screen.background.Intent.InstantTranslate
import com.bael.kirin.feature.translation.screen.background.Intent.OpenMainScreen
import com.bael.kirin.feature.translation.screen.background.Intent.StartService
import com.bael.kirin.feature.translation.signal.Signal
import com.bael.kirin.feature.translation.signal.Signal.DismissBackground
import com.bael.kirin.feature.translation.signal.SignalManager
import com.bael.kirin.lib.base.di.assisted.DispatcherFactoryAssisted
import com.bael.kirin.lib.base.dispatcher.BaseDispatcher
import com.bael.kirin.lib.base.signal.SignalReceiver
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by ErickSumargo on 01/06/20.
 */

@ActivityScoped
@ExperimentalCoroutinesApi
class Dispatcher @AssistedInject constructor(
    viewModel: Lazy<ViewModel>,
    @ActivityContext context: Context,
    @Assisted arg0: Renderer,
    @Assisted arg1: Action
) : BaseDispatcher<State, Intent>(viewModel),
    SignalReceiver<Signal>,
    Renderer by arg0,
    Action by arg1 {
    private val signalManager = SignalManager(context, receiver = this)

    @AssistedInject.Factory
    interface Factory :
        DispatcherFactoryAssisted<Renderer, Action, Dispatcher>

    override fun dispatchStates(): Observer<Pair<State?, State>> = Observer {}

    override fun dispatchIntent(): Observer<Intent?> {
        return Observer { intent ->
            when (intent) {
                is Initialize -> {
                    init()
                }
                is InstantTranslate -> {
                    instantTranslate(intent.query)
                }
                is StartService -> {
                    startService(intent.query)
                }
                is OpenMainScreen -> {
                    openMainScreen(intent.query)
                }
            }
        }
    }

    override fun receiveSignal(signal: Signal) {
        when (signal) {
            is DismissBackground -> {
                dismissBackground()
            }
        }
    }

    override fun clear() {
        signalManager.unregisterSignalReceiver()
        super.clear()
    }
}

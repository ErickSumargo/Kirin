package com.bael.kirin.feature.translation.service.floating

import android.content.Context
import androidx.lifecycle.Observer
import com.bael.kirin.feature.translation.service.floating.Intent.ActivateToggle
import com.bael.kirin.feature.translation.service.floating.Intent.DeactivateToggle
import com.bael.kirin.feature.translation.service.floating.Intent.DisplayResultDetail
import com.bael.kirin.feature.translation.service.floating.Intent.Initialize
import com.bael.kirin.feature.translation.service.floating.Intent.StartEditing
import com.bael.kirin.feature.translation.service.floating.Intent.StopEditing
import com.bael.kirin.feature.translation.signal.Signal
import com.bael.kirin.feature.translation.signal.Signal.BackgroundDismissed
import com.bael.kirin.feature.translation.signal.Signal.BackgroundShown
import com.bael.kirin.feature.translation.signal.Signal.InstantTranslate
import com.bael.kirin.feature.translation.signal.Signal.StopService
import com.bael.kirin.feature.translation.signal.SignalManager
import com.bael.kirin.lib.base.dispatcher.BaseDispatcher
import com.bael.kirin.lib.base.signal.SignalReceiver
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by ErickSumargo on 01/06/20.
 */

@ExperimentalCoroutinesApi
class Dispatcher constructor(
    context: Context,
    viewModel: ViewModel,
    renderer: Renderer,
    action: Action
) : BaseDispatcher<State, Intent>(viewModel),
    SignalReceiver<Signal>,
    Renderer by renderer,
    Action by action {
    private val signalManager = SignalManager(context, receiver = this)

    override fun dispatchStates(): Observer<Pair<State?, State>> {
        return Observer { (previousState, newState) ->
            if (previousState?.toggleActive != newState.toggleActive) {
                renderToggleLayout(newState.toggleActive)
            }

            if (previousState?.sourceLanguage != newState.sourceLanguage) {
                renderSourceLanguageSpinner(newState.sourceLanguage)
            }

            if (previousState?.sourceLanguage != newState.sourceLanguage ||
                previousState.targetLanguage != newState.targetLanguage) {
                renderSwapLanguageIcon(
                    newState.sourceLanguage,
                    newState.targetLanguage
                )
            }

            if (previousState?.targetLanguage != newState.targetLanguage) {
                renderTargetLanguageSpinner(newState.targetLanguage)
            }

            if (previousState?.sourceLanguage != newState.sourceLanguage ||
                previousState.targetLanguage != newState.targetLanguage ||
                previousState.instantQuery != newState.instantQuery) {
                renderQueryInput(
                    newState.sourceLanguage,
                    newState.targetLanguage,
                    newState.instantQuery
                )
            }

            if (previousState?.query != newState.query) {
                renderClearQueryIcon(newState.query)
            }

            if (previousState?.sourceLanguage != newState.sourceLanguage ||
                previousState.targetLanguage != newState.targetLanguage ||
                previousState.query != newState.query ||
                previousState.translationData != newState.translationData) {
                renderTranslationInput(
                    newState.sourceLanguage,
                    newState.targetLanguage,
                    newState.query,
                    newState.translationData
                )
            }

            if (previousState?.translationData != newState.translationData) {
                renderLoadingProgress(newState.translationData)
            }

            /**
             * previousState  = null ~ Init View's components
             * that need to be rendered once enough.
             */
            if (previousState == null) {
                renderTranslationLayout()
                renderSwipeLayout()
            }
        }
    }

    override fun dispatchIntent(): Observer<Intent?> {
        return Observer { intent ->
            when (intent) {
                is Initialize -> {
                    processPacket()
                    addTranslationLayout()
                    showToggleLayout()
                }
                is ActivateToggle -> {
                    showTranslationLayout(intent.editMode)
                }
                is DeactivateToggle -> {
                    hideSoftKeyboard()
                    hideTranslationLayout()
                    dismissBackground()
                }
                is StartEditing -> {
                    showSoftKeyboard()
                    showBackground()
                }
                is StopEditing -> {
                    hideSoftKeyboard()
                    dismissBackground()
                }
                is DisplayResultDetail -> {
                    openGoogleTranslate(
                        intent.sourceLanguage,
                        intent.targetLanguage,
                        intent.query
                    )
                }
            }
        }
    }

    override fun receiveSignal(signal: Signal) {
        when (signal) {
            is BackgroundShown -> {
                onBackgroundShown()
            }
            is BackgroundDismissed -> {
                onBackgroundDismissed()
            }
            is StopService -> {
                stopService()
            }
            is InstantTranslate -> {
                instantTranslate(signal.instantQuery)
            }
        }
    }

    override fun clear() {
        signalManager.unregisterSignalReceiver()
        super.clear()
    }
}

package com.bael.kirin.service.bubble

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.bael.kirin.base.BaseDispatcher
import com.bael.kirin.base.BaseViewModel
import com.bael.kirin.service.bubble.EventState.Activation
import com.bael.kirin.service.bubble.EventState.StartEditing
import com.bael.kirin.service.bubble.contract.UIEvent
import com.bael.kirin.service.bubble.contract.UIRenderer
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by ErickSumargo on 01/06/20.
 */

@ExperimentalCoroutinesApi
class Dispatcher(
    private val viewModel: BaseViewModel<UIState, EventState>,
    private val renderer: UIRenderer,
    private val event: UIEvent
) : BaseDispatcher<UIState, EventState>(viewModel) {

    fun observe(lifecycleOwner: LifecycleOwner? = null) {
        observeState(lifecycleOwner)
        observeEvent(lifecycleOwner)
    }

    private fun observeState(lifecycleOwner: LifecycleOwner?) {
        if (lifecycleOwner == null) {
            viewModel.state.observeForever(stateObserver)
        } else {
            viewModel.state.observe(lifecycleOwner, stateObserver)
        }
    }

    private fun observeEvent(lifecycleOwner: LifecycleOwner?) {
        if (lifecycleOwner == null) {
            viewModel.event.observeForever(eventObserver)
        } else {
            viewModel.event.observe(lifecycleOwner, eventObserver)
        }
    }

    override fun constructStateObserver(): Observer<Pair<UIState?, UIState>> {
        return Observer<Pair<UIState?, UIState>> { (previousState, newState) ->
            if (previousState?.active != newState.active) {
                renderer.renderToggleLayout(newState.active)
            }

            if (previousState?.reset != newState.reset) {
                renderer.renderTranslationInput(newState.reset)
            }

            if (previousState?.text != newState.text) {
                renderer.renderClearTextIcon(newState.text)
            }

            /**
             * previousState  = null ~ Init View's components
             * that need to be rendered once enough.
             */
            if (previousState == null) {
                renderer.renderTranslationLayout()
                renderer.renderSourceLanguageLabel()
                renderer.renderSwapLanguageIcon()
                renderer.renderTargetLanguageLabel()
                renderer.renderTranslatedLabel()
                renderer.renderSwipeLayout()
            }
        }
    }

    override fun constructEventObserver(): Observer<EventState?> {
        return Observer<EventState?> { eventState ->
            when (eventState) {
                is Activation -> {
                    if (eventState.active) {
                        event.showTranslationLayout()
                    } else {
                        event.hideSoftKeyboard()
                        event.hideTranslationLayout()
                        event.dismissDialog()
                    }
                }
                is StartEditing -> {
                    if (eventState.start) {
                        event.showSoftKeyboard()
                        event.showDialog()
                    } else {
                        event.hideSoftKeyboard()
                        event.dismissDialog()
                    }
                }
                else -> {
                    /**
                     * eventState  = null ~ Init ViewEvent.
                     */
                    event.showToggleLayout()
                }
            }
        }
    }
}

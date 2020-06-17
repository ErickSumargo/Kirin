package com.bael.kirin.service.bubble

import com.bael.kirin.base.BaseViewModel
import com.bael.kirin.service.bubble.EventState.Activation
import com.bael.kirin.service.bubble.EventState.Editing
import com.bael.kirin.service.bubble.EventState.StartEditing
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by ErickSumargo on 01/06/20.
 */

@ExperimentalCoroutinesApi
class ViewModel(
    state: UIState
) : BaseViewModel<UIState, EventState>(state) {

    fun setToggleActivation(active: Boolean) {
        render(Activation(active))
    }

    fun setTranslationText(text: String) {
        render(Editing(text, reset = false))
    }

    fun clearTranslationText() {
        render(Editing(text = "", reset = true))
    }

    fun startEditing() {
        render(StartEditing(start = true))
    }

    fun stopEditing() {
        render(StartEditing(start = false))
    }

    override fun reduceState(state: UIState, event: EventState): UIState {
        return when (event) {
            is Activation -> {
                state.copy(active = event.active)
            }
            is StartEditing -> {
                state.copy()
            }
            is Editing -> {
                state.copy(text = event.text, reset = event.reset)
            }
        }
    }
}

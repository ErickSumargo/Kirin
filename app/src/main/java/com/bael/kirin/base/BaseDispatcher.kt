package com.bael.kirin.base

import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by ErickSumargo on 01/06/20.
 */

@ExperimentalCoroutinesApi
abstract class BaseDispatcher<S, E>(
    private val viewModel: BaseViewModel<S, E>
) {
    protected val stateObserver: Observer<Pair<S?, S>> = constructStateObserver()
    protected val eventObserver: Observer<E?> = constructEventObserver()

    protected abstract fun constructStateObserver(): Observer<Pair<S?, S>>
    protected abstract fun constructEventObserver(): Observer<E?>

    private fun removeObservers() {
        viewModel.onCleared = {
            viewModel.state.removeObserver(stateObserver)
            viewModel.event.removeObserver(eventObserver)
        }
    }
}

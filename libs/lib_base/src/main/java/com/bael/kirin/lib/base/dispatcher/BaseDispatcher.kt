package com.bael.kirin.lib.base.dispatcher

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.bael.kirin.lib.base.viewmodel.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by ErickSumargo on 01/06/20.
 */

@ExperimentalCoroutinesApi
abstract class BaseDispatcher<S, I>(viewModel: Lazy<BaseViewModel<S, I>>) {
    private val viewModel: BaseViewModel<S, I> by lazy { viewModel.value }

    private val statesObserver: Observer<Pair<S?, S>> get() = dispatchStates()
    private val intentObserver: Observer<I?> get() = dispatchIntent()

    fun observe(lifecycleOwner: LifecycleOwner) {
        viewModel.states.observe(lifecycleOwner, statesObserver)
        viewModel.intent.observe(lifecycleOwner, intentObserver)
    }

    protected abstract fun dispatchStates(): Observer<Pair<S?, S>>
    protected abstract fun dispatchIntent(): Observer<I?>

    open fun clear(): Unit = Unit
}

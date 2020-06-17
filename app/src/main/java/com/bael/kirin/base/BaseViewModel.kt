package com.bael.kirin.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Created by ErickSumargo on 01/06/20.
 */

@ExperimentalCoroutinesApi
abstract class BaseViewModel<S, E>(
    initState: S,
    final override val coroutineContext: CoroutineContext = Main
) : ViewModel(),
    CoroutineScope {
    private val stateData: MutableLiveData<Pair<S?, S>> = MutableLiveData()
    private val eventData: MutableLiveData<E?> = MutableLiveData()
    private val eventFlow: MutableStateFlow<E?> = MutableStateFlow(null)

    val state: LiveData<Pair<S?, S>> get() = stateData
    val event: LiveData<E?> get() = eventData

    lateinit var onCleared: () -> Unit

    init {
        launch(coroutineContext) {
            eventFlow.scan(initState) { previousState, eventState ->
                val newState: S
                if (eventState == null) {
                    newState = previousState
                    stateData.value = Pair(null, previousState)
                    eventData.value = null
                } else {
                    newState = reduceState(previousState, eventState)
                    stateData.value = Pair(previousState, newState)
                    eventData.value = eventState
                }

                newState
            }.collect {}
        }
    }

    fun render(event: E) {
        eventFlow.value = event
    }

    abstract fun reduceState(state: S, event: E): S

    override fun onCleared() {
        super.onCleared()
        onCleared()
    }
}

package com.bael.kirin.lib.arch.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bael.kirin.lib.logger.contract.Logger
import com.bael.kirin.lib.threading.contract.Threading
import com.bael.kirin.lib.threading.util.Util.DefaultThread
import com.bael.kirin.lib.threading.util.Util.MainThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by ErickSumargo on 01/06/20.
 */

@ExperimentalCoroutinesApi
abstract class BaseViewModel<S, I>(
    initState: S,
    initIntent: I?,
    savedStateHandle: SavedStateHandle? = null,
    override val coroutineContext: CoroutineContext = DefaultThread
) : ViewModel(),
    Threading {
    @Inject
    protected lateinit var logger: Logger

    private val mutableStateFlow: MutableStateFlow<S> = MutableStateFlow(initState)
    private val mutableIntentFlow: MutableStateFlow<I?> = MutableStateFlow(initIntent)

    private val mutableStateData: MutableLiveData<Pair<S?, S>> = MutableLiveData()
    private val mutableIntentData: MutableLiveData<I?> = MutableLiveData()

    internal val states: LiveData<Pair<S?, S>> get() = mutableStateData
    internal val intent: LiveData<I?> get() = mutableIntentData

    protected val state: S get() = states.value?.second!!

    init {
        execute(MainThread) {
            mutableStateFlow.scan(null as S?) { previousState, newState ->
                mutableStateData.value = previousState to newState
                newState
            }.collect()
        }

        execute(MainThread) {
            mutableIntentFlow.collect { intent ->
                mutableIntentData.value = intent
            }
        }
    }

    final override fun execute(
        thread: CoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) {
        try {
            viewModelScope.launch(
                context = thread,
                block = block
            )
        } catch (cause: Exception) {
            logger.log(cause)
        }
    }

    protected fun render(newState: S) {
        mutableStateFlow.value = newState
    }

    protected fun action(newIntent: I) {
        mutableIntentFlow.value = newIntent
    }

    protected fun renderWithAction(newState: S, newIntent: I) {
        mutableStateFlow.value = newState
        mutableIntentFlow.value = newIntent
    }
}

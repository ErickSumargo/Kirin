package com.bael.kirin.lib.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bael.kirin.lib.logger.contract.Logger
import com.bael.kirin.lib.threading.contract.Threading
import com.bael.kirin.lib.threading.executor.contract.Executor
import com.bael.kirin.lib.threading.executor.schema.ExecutorSchema
import com.bael.kirin.lib.threading.executor.schema.ExecutorSchema.Concurrent
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
    private val savedStateHandle: SavedStateHandle? = null,
    override val coroutineContext: CoroutineContext = DefaultThread
) : ViewModel(),
    Threading {
    @Inject
    internal lateinit var executor: Executor

    @Inject
    internal lateinit var logger: Logger

    private val restoredState: S = savedStateHandle?.get(KEY_SAVED_STATE) ?: initState
    private val mutableStateFlow: MutableStateFlow<S> = MutableStateFlow(restoredState)

    /**
     * Here we don't want to replay the previous occurred intent
     * cached by savedStateHandle.
     *
     * It should observe for the incoming new intent instead.
     */
    private val mutableIntentFlow: MutableStateFlow<I?> = MutableStateFlow(initIntent)

    private val mutableStateData: MutableLiveData<Pair<S?, S>> = MutableLiveData()
    private val mutableIntentData: MutableLiveData<I?> = MutableLiveData()

    internal val states: LiveData<Pair<S?, S>> get() = mutableStateData
    internal val intent: LiveData<I?> get() = mutableIntentData

    protected val state: S get() = mutableStateFlow.value

    init {
        observeState()
        observeIntent()
    }

    override fun launch(
        thread: CoroutineContext,
        schema: ExecutorSchema,
        block: suspend CoroutineScope.() -> Unit
    ) {
        try {
            viewModelScope.launch(context = thread) {
                // Investigate properties executor and logger are not injected instantly
                // when this class in instantiation.
                if (::executor.isInitialized.not()) block()
                else executor.execute(schema) { block() }
            }
        } catch (cause: Exception) {
            logger.log(cause)
        }
    }

    private fun observeState() = launch(
        thread = MainThread,
        schema = Concurrent
    ) {
        mutableStateFlow.scan(null as S?) { previousState, newState ->
            mutableStateData.value = previousState to newState
            newState
        }.collect()
    }

    private fun observeIntent() = launch(
        thread = MainThread,
        schema = Concurrent
    ) {
        mutableIntentFlow.collect { intent ->
            mutableIntentData.value = intent
        }
    }

    private fun saveState(newState: S) = launch(
        thread = MainThread,
        schema = Concurrent
    ) {
        savedStateHandle?.set(KEY_SAVED_STATE, newState)
    }

    protected fun render(newState: S) {
        saveState(newState)
        mutableStateFlow.value = newState
    }

    protected fun action(newIntent: I) {
        mutableIntentFlow.value = newIntent
    }

    companion object {
        private const val KEY_SAVED_STATE: String = "saved_state"
    }
}

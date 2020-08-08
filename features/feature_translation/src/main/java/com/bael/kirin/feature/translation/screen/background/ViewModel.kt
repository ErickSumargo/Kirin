package com.bael.kirin.feature.translation.screen.background

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.bael.kirin.lib.base.viewmodel.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.hilt.Assisted as HiltAssisted

/**
 * Created by ErickSumargo on 01/06/20.
 */

@ExperimentalCoroutinesApi
class ViewModel @ViewModelInject constructor(
    initState: State,
    initIntent: Intent?,
    @HiltAssisted savedStateHandle: SavedStateHandle
) : BaseViewModel<State, Intent>(initState, initIntent, savedStateHandle)

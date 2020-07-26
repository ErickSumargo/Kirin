package com.bael.kirin.feature.translation.screen.background

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.bael.kirin.lib.arch.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by ErickSumargo on 01/06/20.
 */

@ExperimentalCoroutinesApi
class ViewModel @ViewModelInject constructor(
    @Assisted savedState: SavedStateHandle,
    initState: State,
    initIntent: Intent?
) : BaseViewModel<State, Intent>(initState, initIntent, savedState)

package com.bael.kirin.service.bubble

/**
 * Created by ErickSumargo on 01/06/20.
 */

data class UIState(
    val active: Boolean = false,
    val text: String = "",
    val reset: Boolean = false
)

sealed class EventState {
    data class Activation(val active: Boolean) : EventState()
    data class StartEditing(val start: Boolean) : EventState()
    data class Editing(val text: String, val reset: Boolean) : EventState()
}

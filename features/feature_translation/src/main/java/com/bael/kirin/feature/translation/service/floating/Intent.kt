package com.bael.kirin.feature.translation.service.floating

/**
 * Created by ErickSumargo on 01/06/20.
 */

sealed class Intent {

    object Initialize : Intent()

    data class ActivateToggle(val editMode: Boolean) : Intent()

    object DeactivateToggle : Intent()

    object StartEditing : Intent()

    object StopEditing : Intent()

    data class DisplayResultDetail(
        val sourceLanguage: String,
        val targetLanguage: String,
        val query: String
    ) : Intent()
}

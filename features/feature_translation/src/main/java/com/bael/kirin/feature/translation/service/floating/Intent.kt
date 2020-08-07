package com.bael.kirin.feature.translation.service.floating

import com.bael.kirin.lib.arch.base.BaseIntent

/**
 * Created by ErickSumargo on 01/06/20.
 */

sealed class Intent : BaseIntent() {

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

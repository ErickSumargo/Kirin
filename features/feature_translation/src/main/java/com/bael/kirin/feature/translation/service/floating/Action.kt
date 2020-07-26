package com.bael.kirin.feature.translation.service.floating

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface Action {

    fun processPacket()

    fun showToggleLayout()

    fun addTranslationLayout()

    fun showTranslationLayout(editMode: Boolean)

    fun hideTranslationLayout()

    fun showBackground()

    fun dismissBackground()

    fun showSoftKeyboard()

    fun hideSoftKeyboard()

    fun openGoogleTranslate(
        sourceLanguage: String,
        targetLanguage: String,
        query: String
    )

    fun instantTranslate(query: String)

    fun onBackgroundShown()

    fun onBackgroundDismissed()

    fun stopService()
}

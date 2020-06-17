package com.bael.kirin.service.bubble.contract

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface UIRenderer {
    fun renderToggleLayout(active: Boolean)
    fun renderTranslationLayout()
    fun renderSourceLanguageLabel()
    fun renderSwapLanguageIcon()
    fun renderTargetLanguageLabel()
    fun renderTranslationInput(reset: Boolean)
    fun renderClearTextIcon(text: String)
    fun renderTranslatedLabel()
    fun renderSwipeLayout()
}

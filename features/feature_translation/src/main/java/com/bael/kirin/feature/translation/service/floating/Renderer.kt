package com.bael.kirin.feature.translation.service.floating

import com.bael.kirin.lib.api.translation.model.entity.Translation
import com.bael.kirin.lib.data.model.Data

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface Renderer {

    fun renderToggleLayout(active: Boolean)

    fun renderTranslationLayout()

    fun renderSourceLanguageSpinner(sourceLanguage: String)

    fun renderSwapLanguageIcon(
        sourceLanguage: String,
        targetLanguage: String
    )

    fun renderTargetLanguageSpinner(targetLanguage: String)

    fun renderQueryInput(
        sourceLanguage: String,
        targetLanguage: String,
        newQuery: String?
    )

    fun renderClearQueryIcon(query: String)

    fun renderTranslationInput(
        sourceLanguage: String,
        targetLanguage: String,
        query: String,
        data: Data<Translation>
    )

    fun renderLoadingProgress(data: Data<Translation>)

    fun renderSwipeLayout()
}

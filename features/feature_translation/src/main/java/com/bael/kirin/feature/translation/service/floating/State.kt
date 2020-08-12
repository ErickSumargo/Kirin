package com.bael.kirin.feature.translation.service.floating

import com.bael.kirin.lib.api.translation.model.entity.Translation
import com.bael.kirin.lib.base.state.BaseState
import com.bael.kirin.lib.data.model.Data

/**
 * Created by ErickSumargo on 01/06/20.
 */

data class State(
    val toggleActive: Boolean = false,
    val sourceLanguage: String = "",
    val targetLanguage: String = "",
    val query: String = "",
    val instantQuery: String? = null, // query either retrieved from reset or context menu copy event
    val translationData: Data<Translation> = Data()
) : BaseState()

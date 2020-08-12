package com.bael.kirin.lib.api.translation.repository.contract

import com.bael.kirin.lib.api.translation.model.entity.Translation
import com.bael.kirin.lib.data.model.Data
import kotlinx.coroutines.flow.Flow

/**
 * Created by ErickSumargo on 15/06/20.
 */

interface TranslatorRepository {

    fun get(
        sourceLanguage: String,
        targetLanguage: String,
        query: String
    ): Flow<Data<Translation>>
}

package com.bael.kirin.lib.api.translation.service.contract

import com.bael.kirin.lib.network.model.Response
import kotlinx.coroutines.flow.Flow

/**
 * Created by ErickSumargo on 15/06/20.
 */

interface TranslatorService {

    fun translate(
        sourceLanguage: String,
        targetLanguage: String,
        query: String
    ): Flow<Response<String>>
}

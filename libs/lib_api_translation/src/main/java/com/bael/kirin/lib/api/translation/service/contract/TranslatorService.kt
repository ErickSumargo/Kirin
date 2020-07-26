package com.bael.kirin.lib.api.translation.service.contract

import com.bael.kirin.lib.network.model.Response

/**
 * Created by ErickSumargo on 15/06/20.
 */

interface TranslatorService {

    suspend fun translate(
        sourceLanguage: String,
        targetLanguage: String,
        query: String
    ): Response<String>
}

package com.bael.kirin.lib.api.translation.repository.implementation

import com.bael.kirin.lib.api.translation.model.entity.Translation
import com.bael.kirin.lib.api.translation.repository.contract.TranslatorRepository
import com.bael.kirin.lib.api.translation.service.contract.TranslatorService
import com.bael.kirin.lib.data.contract.DataTransformer
import com.bael.kirin.lib.data.model.Data
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by ErickSumargo on 15/06/20.
 */

class DefaultTranslatorRepository @Inject constructor(
    apiService: TranslatorService,
    dataTransformer: DataTransformer<String, Translation>
) : TranslatorRepository,
    TranslatorService by apiService,
    DataTransformer<String, Translation> by dataTransformer {

    override suspend fun get(
        sourceLanguage: String,
        targetLanguage: String,
        query: String
    ) = flow {
        emit(Data(loading = true))

        val response = translate(
            sourceLanguage,
            targetLanguage,
            query
        )

        if (response.isSuccess()) {
            val data = transform(response.data.orEmpty())
            emit(Data(result = data))
        } else {
            val error = response.error
            emit(Data(error = error))
        }
    }
}

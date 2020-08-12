package com.bael.kirin.lib.api.translation.repository.implementation

import com.bael.kirin.lib.api.translation.model.entity.Translation
import com.bael.kirin.lib.api.translation.repository.contract.TranslatorRepository
import com.bael.kirin.lib.api.translation.service.contract.TranslatorService
import com.bael.kirin.lib.data.contract.DataTransformer
import com.bael.kirin.lib.data.model.Data
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override fun get(
        sourceLanguage: String,
        targetLanguage: String,
        query: String
    ): Flow<Data<Translation>> {
        return translate(
            sourceLanguage,
            targetLanguage,
            query
        ).map { response ->
            if (response.isSuccess()) {
                val result = transform(response.data.orEmpty())
                Data(result = result)
            } else {
                val error = response.error
                Data(error = error)
            }
        }
    }
}

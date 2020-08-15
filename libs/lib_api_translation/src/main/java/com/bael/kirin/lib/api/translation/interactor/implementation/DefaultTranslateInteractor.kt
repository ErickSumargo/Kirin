package com.bael.kirin.lib.api.translation.interactor.implementation

import com.bael.kirin.lib.api.translation.interactor.contract.TranslateInteractor
import com.bael.kirin.lib.api.translation.model.entity.Translation
import com.bael.kirin.lib.api.translation.repository.contract.TranslatorRepository
import com.bael.kirin.lib.data.model.Data
import com.bael.kirin.lib.network.interactor.BaseInteractor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 * Created by ErickSumargo on 15/06/20.
 */

@ExperimentalCoroutinesApi
class DefaultTranslateInteractor @Inject constructor(
    private val translatorRepository: TranslatorRepository
) : BaseInteractor(),
    TranslateInteractor {

    override operator fun invoke(
        sourceLanguage: String,
        targetLanguage: String,
        query: String,
        response: (Data<Translation>) -> Unit
    ) = launch {
        val translationDataFlow = translatorRepository.get(
            sourceLanguage,
            targetLanguage,
            query
        )

        translationDataFlow
            .onStart {
                response(Data(loading = true))
            }.collect { data ->
                response(data)
            }
    }
}

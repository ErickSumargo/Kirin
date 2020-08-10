package com.bael.kirin.lib.api.translation.interactor.implementation

import com.bael.kirin.lib.api.translation.interactor.contract.TranslateInteractor
import com.bael.kirin.lib.api.translation.model.entity.Translation
import com.bael.kirin.lib.api.translation.repository.contract.TranslatorRepository
import com.bael.kirin.lib.data.model.Data
import com.bael.kirin.lib.network.interactor.BaseInteractor
import com.bael.kirin.lib.threading.executor.ExecutorSchema.Conflated
import com.bael.kirin.lib.threading.ext.subscribe
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * Created by ErickSumargo on 15/06/20.
 */

class DefaultTranslateInteractor @Inject constructor(
    private val translatorRepository: TranslatorRepository
) : BaseInteractor(),
    TranslateInteractor {

    override operator fun invoke(
        sourceLanguage: String,
        targetLanguage: String,
        query: String,
        result: (Data<Translation>) -> Unit
    ) = launch(schema = Conflated) {
        val translationDataFlow = translatorRepository.get(
            sourceLanguage,
            targetLanguage,
            query
        )

        translationDataFlow
            .onEach { result(it) }
            .subscribe()
    }
}

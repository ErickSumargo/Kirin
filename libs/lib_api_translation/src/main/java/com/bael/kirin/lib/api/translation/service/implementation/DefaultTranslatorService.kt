package com.bael.kirin.lib.api.translation.service.implementation

import com.bael.kirin.lib.api.translation.service.contract.TranslatorService
import com.bael.kirin.lib.network.model.Response
import com.bael.kirin.lib.network.service.BaseService
import com.bael.kirin.lib.threading.ext.safeSend
import com.google.cloud.translate.Translate
import com.google.cloud.translate.Translate.TranslateOption.model
import com.google.cloud.translate.Translate.TranslateOption.sourceLanguage
import com.google.cloud.translate.Translate.TranslateOption.targetLanguage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by ErickSumargo on 15/06/20.
 */

@ExperimentalCoroutinesApi
class DefaultTranslatorService @Inject constructor(
    private val translator: Provider<Translate>
) : BaseService(),
    TranslatorService {

    override fun translate(
        sourceLanguage: String,
        targetLanguage: String,
        query: String
    ): Flow<Response<String>> = channelFlow {
        val response = get {
            translator.get().translate(
                query,
                sourceLanguage(sourceLanguage),
                targetLanguage(targetLanguage),
                model("base")
            ).translatedText
        }
        safeSend(response)
    }
}

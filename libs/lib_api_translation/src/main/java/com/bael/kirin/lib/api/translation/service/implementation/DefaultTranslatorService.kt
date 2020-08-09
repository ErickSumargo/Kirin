package com.bael.kirin.lib.api.translation.service.implementation

import com.bael.kirin.lib.api.translation.service.contract.TranslatorService
import com.bael.kirin.lib.network.model.Response
import com.bael.kirin.lib.network.service.BaseService
import com.google.cloud.translate.Translate
import com.google.cloud.translate.Translate.TranslateOption.model
import com.google.cloud.translate.Translate.TranslateOption.sourceLanguage
import com.google.cloud.translate.Translate.TranslateOption.targetLanguage
import javax.inject.Inject

/**
 * Created by ErickSumargo on 15/06/20.
 */

class DefaultTranslatorService @Inject constructor(
    translator: Translate
) : BaseService(),
    TranslatorService,
    Translate by translator {

    override suspend fun translate(
        sourceLanguage: String,
        targetLanguage: String,
        query: String
    ): Response<String> = get {
        /*translate(
            query,
            sourceLanguage(sourceLanguage),
            targetLanguage(targetLanguage),
            model("base")
        ).translatedText*/
        "$sourceLanguage-$targetLanguage-$query"
    }
}

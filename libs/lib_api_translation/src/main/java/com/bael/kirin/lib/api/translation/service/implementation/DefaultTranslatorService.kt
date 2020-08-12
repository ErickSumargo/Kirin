package com.bael.kirin.lib.api.translation.service.implementation

import com.bael.kirin.lib.api.translation.service.contract.TranslatorService
import com.bael.kirin.lib.network.model.Response
import com.bael.kirin.lib.network.service.BaseService
import com.bael.kirin.lib.threading.ext.safeSend
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

/**
 * Created by ErickSumargo on 15/06/20.
 */

@ExperimentalCoroutinesApi
class DefaultTranslatorService @Inject constructor() :
    BaseService(),
    TranslatorService {

    override suspend fun translate(
        sourceLanguage: String,
        targetLanguage: String,
        query: String
    ): Flow<Response<String>> = channelFlow {
        val response = get {
            delay(1000)
            "${languages[sourceLanguage]}-${languages[targetLanguage]}-$query"
        }
        safeSend(response)
    }
}

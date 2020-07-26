package com.bael.kirin.lib.api.translation.interactor.contract

import com.bael.kirin.lib.api.translation.model.entity.Translation
import com.bael.kirin.lib.data.model.Data

/**
 * Created by ErickSumargo on 15/06/20.
 */

interface TranslateInteractor {

    suspend operator fun invoke(
        sourceLanguage: String,
        targetLanguage: String,
        query: String,
        result: (Data<Translation>) -> Unit
    )
}

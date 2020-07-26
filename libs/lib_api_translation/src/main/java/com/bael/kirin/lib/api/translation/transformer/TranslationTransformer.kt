package com.bael.kirin.lib.api.translation.transformer

import com.bael.kirin.lib.api.translation.model.entity.Translation
import com.bael.kirin.lib.data.contract.DataTransformer
import javax.inject.Inject

/**
 * Created by ErickSumargo on 15/06/20.
 */

class TranslationTransformer @Inject constructor() : DataTransformer<String, Translation> {

    override fun transform(response: String): Translation {
        return Translation(
            translatedText = response
        )
    }
}

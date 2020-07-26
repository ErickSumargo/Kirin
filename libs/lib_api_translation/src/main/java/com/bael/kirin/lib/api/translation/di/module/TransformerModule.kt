package com.bael.kirin.lib.api.translation.di.module

import com.bael.kirin.lib.api.translation.model.entity.Translation
import com.bael.kirin.lib.api.translation.transformer.TranslationTransformer
import com.bael.kirin.lib.data.contract.DataTransformer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

/**
 * Created by ErickSumargo on 01/06/20.
 */

@Module
@InstallIn(ApplicationComponent::class)
abstract class TransformerModule {

    @Singleton
    @Binds
    abstract fun bindTranslationTransformer(
        transformer: TranslationTransformer
    ): DataTransformer<String, Translation>
}

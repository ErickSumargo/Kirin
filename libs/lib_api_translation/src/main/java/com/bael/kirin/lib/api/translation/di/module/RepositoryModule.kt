package com.bael.kirin.lib.api.translation.di.module

import com.bael.kirin.lib.api.translation.repository.contract.TranslatorRepository
import com.bael.kirin.lib.api.translation.repository.implementation.DefaultTranslatorRepository
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
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindTranslatorRepository(
        repository: DefaultTranslatorRepository
    ): TranslatorRepository
}

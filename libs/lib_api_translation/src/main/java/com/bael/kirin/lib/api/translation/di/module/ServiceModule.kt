package com.bael.kirin.lib.api.translation.di.module

import com.bael.kirin.lib.api.translation.service.contract.TranslatorService
import com.bael.kirin.lib.api.translation.service.implementation.DefaultTranslatorService
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
abstract class ServiceModule {

    @Singleton
    @Binds
    abstract fun bindTranslatorService(
        service: DefaultTranslatorService
    ): TranslatorService
}

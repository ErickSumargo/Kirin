package com.bael.kirin.lib.api.translation.di.module

import com.bael.kirin.lib.api.translation.interactor.contract.TranslateInteractor
import com.bael.kirin.lib.api.translation.interactor.implementation.DefaultTranslateInteractor
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
abstract class InteractorModule {

    @Singleton
    @Binds
    abstract fun bindTranslateInteractor(
        interactor: DefaultTranslateInteractor
    ): TranslateInteractor
}

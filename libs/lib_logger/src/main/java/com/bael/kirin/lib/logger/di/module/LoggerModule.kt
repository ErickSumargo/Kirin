package com.bael.kirin.lib.logger.di.module

import com.bael.kirin.lib.logger.contract.Logger
import com.bael.kirin.lib.logger.implementation.FirebaseCrashlytics
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
abstract class LoggerModule {

    @Singleton
    @Binds
    abstract fun bindLogger(crashlytics: FirebaseCrashlytics): Logger
}

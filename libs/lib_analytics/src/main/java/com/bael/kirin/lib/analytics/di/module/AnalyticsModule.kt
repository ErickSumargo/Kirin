package com.bael.kirin.lib.analytics.di.module

import com.bael.kirin.lib.analytics.contract.Tracker
import com.bael.kirin.lib.analytics.implementation.FirebaseAnalytics
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
abstract class AnalyticsModule {

    @Singleton
    @Binds
    abstract fun bindTracker(analytics: FirebaseAnalytics): Tracker
}

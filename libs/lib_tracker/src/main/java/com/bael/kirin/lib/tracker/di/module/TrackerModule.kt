package com.bael.kirin.lib.tracker.di.module

import com.bael.kirin.lib.tracker.contract.Tracker
import com.bael.kirin.lib.tracker.implementation.FirebaseTracker
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
abstract class TrackerModule {

    @Singleton
    @Binds
    abstract fun bindTracker(database: FirebaseTracker): Tracker
}

package com.bael.kirin.feature.translation.di.module

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * Created by ErickSumargo on 01/06/20.
 */

@AssistedModule
@Module(includes = [AssistedInject_ActivityAssistedModule::class])
@InstallIn(ActivityComponent::class)
abstract class ActivityAssistedModule

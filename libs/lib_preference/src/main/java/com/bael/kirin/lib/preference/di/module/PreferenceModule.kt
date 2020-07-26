package com.bael.kirin.lib.preference.di.module

import com.bael.kirin.lib.preference.contract.Preference
import com.bael.kirin.lib.preference.implementation.DefaultPreference
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
abstract class PreferenceModule {

    @Singleton
    @Binds
    abstract fun bindPreference(preference: DefaultPreference): Preference
}

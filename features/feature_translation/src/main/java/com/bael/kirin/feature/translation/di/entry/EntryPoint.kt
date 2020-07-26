package com.bael.kirin.feature.translation.di.entry

import com.bael.kirin.feature.translation.preference.Preference
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

/**
 * Created by ErickSumargo on 01/06/20.
 */

@EntryPoint
@InstallIn(ApplicationComponent::class)
interface EntryPoint {

    fun accessPreference(): Preference
}

package com.bael.kirin.feature.translation.di.module

import android.content.Context
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import com.bael.kirin.feature.translation.R
import com.bael.kirin.feature.translation.constant.SUBJECT_EXTRA_EXIT_SCREEN
import com.bael.kirin.feature.translation.constant.SUBJECT_EXTRA_QUERY
import com.bael.kirin.feature.translation.databinding.SettingsLayoutBinding
import com.bael.kirin.feature.translation.preference.Preference
import com.bael.kirin.feature.translation.preference.Preference.Companion.PREFERENCE_AUTO_CLEAR_HISTORY
import com.bael.kirin.feature.translation.preference.Preference.Companion.PREFERENCE_AUTO_EDITING_MODE
import com.bael.kirin.feature.translation.preference.Preference.Companion.PREFERENCE_DIM_BACKGROUND
import com.bael.kirin.feature.translation.preference.Preference.Companion.PREFERENCE_RESPONSIVE_TRANSLATOR
import com.bael.kirin.feature.translation.screen.settings.Intent
import com.bael.kirin.feature.translation.screen.settings.Packet
import com.bael.kirin.feature.translation.screen.settings.State
import com.bael.kirin.feature.translation.screen.settings.ViewModel
import com.bael.kirin.lib.resource.app.AppInfo
import com.bael.kirin.lib.resource.ext.textOf
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by ErickSumargo on 01/06/20.
 */

@Module
@InstallIn(ActivityComponent::class)
@ExperimentalCoroutinesApi
object SettingsScreenModule {

    @ActivityScoped
    @Provides
    fun FragmentActivity.providePacket(): Packet {
        val query = intent.getStringExtra(SUBJECT_EXTRA_QUERY)
        val exitScreen = intent.getBooleanExtra(SUBJECT_EXTRA_EXIT_SCREEN, false)
        return Packet(query, exitScreen)
    }

    @ActivityScoped
    @Provides
    fun FragmentActivity.provideViewBinding(): SettingsLayoutBinding {
        return SettingsLayoutBinding.inflate(layoutInflater)
    }

    @ActivityScoped
    @Provides
    fun @receiver:ActivityContext Context.provideState(
        appInfo: AppInfo,
        preference: Preference
    ): State {
        return State(
            version = appInfo.version,
            settings = mapOf(
                Pair(
                    PREFERENCE_RESPONSIVE_TRANSLATOR,
                    preference.useResponsiveTranslator
                ) to Pair(
                    textOf(R.string.preference_responsive_translator_label),
                    textOf(R.string.preference_responsive_translator_description)
                ),
                Pair(
                    PREFERENCE_AUTO_EDITING_MODE,
                    preference.useAutoEditingMode
                ) to Pair(
                    textOf(R.string.preference_auto_editing_mode_label),
                    textOf(R.string.preference_auto_editing_mode_description)
                ),
                Pair(
                    PREFERENCE_AUTO_CLEAR_HISTORY,
                    preference.autoClearHistory
                ) to Pair(
                    textOf(R.string.preference_clear_history_label),
                    textOf(R.string.preference_clear_history_description)
                ),
                Pair(
                    PREFERENCE_DIM_BACKGROUND,
                    preference.useDimBackground
                ) to Pair(
                    textOf(R.string.preference_dim_background_label),
                    textOf(R.string.preference_dim_background_description)
                )
            )
        )
    }

    @ActivityScoped
    @Provides
    fun provideIntent(): Intent? = null

    @ActivityScoped
    @Provides
    fun FragmentActivity.provideViewModel(): Lazy<ViewModel> = viewModels()
}

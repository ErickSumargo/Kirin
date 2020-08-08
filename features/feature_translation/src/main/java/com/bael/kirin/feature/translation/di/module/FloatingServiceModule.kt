package com.bael.kirin.feature.translation.di.module

import android.content.Context
import android.view.LayoutInflater
import com.bael.kirin.feature.translation.constant.LANGUAGE_AUTO
import com.bael.kirin.feature.translation.constant.languages
import com.bael.kirin.feature.translation.databinding.ToggleLayoutBinding
import com.bael.kirin.feature.translation.databinding.TranslationLayoutBinding
import com.bael.kirin.feature.translation.preference.Preference
import com.bael.kirin.feature.translation.service.floating.Intent
import com.bael.kirin.feature.translation.service.floating.Intent.Initialize
import com.bael.kirin.feature.translation.service.floating.State
import com.bael.kirin.feature.translation.service.floating.ViewModel
import com.bael.kirin.lib.base.wrapper.LazyWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.Locale

/**
 * Created by ErickSumargo on 01/06/20.
 */

@Module
@InstallIn(ServiceComponent::class)
@ExperimentalCoroutinesApi
object FloatingServiceModule {

    @ServiceScoped
    @Provides
    fun @receiver:ApplicationContext Context.provideLayoutInflater(): LayoutInflater {
        return LayoutInflater.from(this)
    }

    @ServiceScoped
    @Provides
    fun provideToggleViewBinding(layoutInflater: LayoutInflater): ToggleLayoutBinding {
        return ToggleLayoutBinding.inflate(layoutInflater)
    }

    @ServiceScoped
    @Provides
    fun provideTranslationViewBinding(layoutInflater: LayoutInflater): TranslationLayoutBinding {
        return TranslationLayoutBinding.inflate(layoutInflater)
    }

    @ServiceScoped
    @Provides
    fun provideState(preference: Preference): State {
        return State(
            sourceLanguage = preference.sourceLanguage.ifBlank {
                LANGUAGE_AUTO.takeIf { languages.containsKey(it) }.orEmpty()
            },
            targetLanguage = preference.targetLanguage.ifBlank {
                Locale.getDefault().language.takeIf { languages.containsKey(it) } ?: "en"
            }
        )
    }

    @ServiceScoped
    @Provides
    fun provideIntent(): Intent = Initialize

    @ServiceScoped
    @Provides
    fun provideViewModel(viewModel: ViewModel): LazyWrapper<ViewModel> {
        return LazyWrapper(viewModel)
    }
}

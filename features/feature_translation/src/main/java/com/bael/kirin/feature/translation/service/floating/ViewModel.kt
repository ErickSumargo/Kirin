package com.bael.kirin.feature.translation.service.floating

import com.bael.kirin.feature.translation.constant.LANGUAGE_AUTO
import com.bael.kirin.feature.translation.preference.Preference
import com.bael.kirin.feature.translation.service.floating.Intent.ActivateToggle
import com.bael.kirin.feature.translation.service.floating.Intent.DeactivateToggle
import com.bael.kirin.feature.translation.service.floating.Intent.DisplayResultDetail
import com.bael.kirin.feature.translation.service.floating.Intent.StartEditing
import com.bael.kirin.feature.translation.service.floating.Intent.StopEditing
import com.bael.kirin.lib.api.translation.interactor.contract.TranslateInteractor
import com.bael.kirin.lib.api.translation.model.entity.Translation
import com.bael.kirin.lib.base.viewmodel.BaseViewModel
import com.bael.kirin.lib.data.ext.orDefault
import com.bael.kirin.lib.data.model.Data
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * Created by ErickSumargo on 01/06/20.
 */

@ServiceScoped
@ExperimentalCoroutinesApi
class ViewModel @Inject constructor(
    initState: State,
    initIntent: Intent?,
    private val preference: Preference,
    private val translateInteractor: TranslateInteractor
) : BaseViewModel<State, Intent>(initState, initIntent) {

    fun setToggleActivation(
        active: Boolean,
        editMode: Boolean
    ) = execute {
        val newState = if (active || (!active && !preference.autoClearHistory)) {
            state.copy(toggleActive = active)
        } else {
            state.copy(
                toggleActive = active,
                newQuery = "",
                query = "",
                queryFixed = "",
                translationData = Data()
            )
        }
        val newIntent = ActivateToggle(editMode).takeIf { active } ?: DeactivateToggle

        render(newState)
        action(newIntent)
    }

    fun setSourceLanguage(language: String) = execute {
        val newState = state.copy(sourceLanguage = language)
        render(newState)
    }

    fun setTargetLanguage(language: String) = execute {
        val newState = state.copy(targetLanguage = language)
        render(newState)
    }

    fun swapLanguage(
        sourceLanguage: String,
        targetLanguage: String
    ) = execute {
        val newState = state.copy(
            sourceLanguage = sourceLanguage,
            targetLanguage = targetLanguage
        )
        render(newState)
    }

    fun setQuery(query: String) = execute {
        val newState = state.copy(
            newQuery = null,
            query = query,
            queryFixed = "",
            translationData = state.translationData.takeIf {
                query.isNotBlank()
            }.orDefault()
        )
        render(newState)
    }

    fun clearQuery() = execute {
        val newState = state.copy(
            newQuery = "",
            query = "",
            queryFixed = "",
            translationData = Data()
        )
        render(newState)
    }

    fun startEditing() = execute {
        val newIntent = StartEditing
        action(newIntent)
    }

    fun stopEditing() = execute {
        val newIntent = StopEditing
        action(newIntent)
    }

    fun translate(
        toggleActive: Boolean = state.toggleActive,
        sourceLanguage: String = state.sourceLanguage,
        targetLanguage: String = state.targetLanguage,
        newQuery: String? = state.newQuery,
        query: String = state.queryFixed
    ) = execute {
        if (query.isBlank()) return@execute

        translateInteractor(
            sourceLanguage = sourceLanguage.takeIf { language ->
                language != LANGUAGE_AUTO
            }.orEmpty(),
            targetLanguage = targetLanguage,
            query = query,
            result = ::onTranslationResult
        )

        val newState = state.copy(
            toggleActive = toggleActive,
            sourceLanguage = sourceLanguage,
            targetLanguage = targetLanguage,
            newQuery = newQuery,
            query = query,
            queryFixed = query
        )
        render(newState)
    }

    fun onTranslationResult(data: Data<Translation>) = execute {
        val newState = state.copy(translationData = data)
        render(newState)
    }

    fun displayResultDetail() = execute {
        val newIntent = DisplayResultDetail(
            sourceLanguage = state.sourceLanguage,
            targetLanguage = state.targetLanguage,
            query = state.query
        )
        action(newIntent)
    }
}

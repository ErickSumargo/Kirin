package com.bael.kirin.feature.translation.service.floating

import com.bael.kirin.feature.translation.constant.LANGUAGE_AUTO
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
    private val translateInteractor: TranslateInteractor
) : BaseViewModel<State, Intent>(initState, initIntent) {

    fun setToggleActivation(
        active: Boolean,
        editMode: Boolean,
        autoClearHistory: Boolean
    ) = launch {
        val newState = if (active || (!active && !autoClearHistory)) {
            state.copy(toggleActive = active)
        } else {
            state.copy(
                toggleActive = active,
                query = "",
                instantQuery = "",
                translationData = Data()
            )
        }
        val newIntent = ActivateToggle(editMode).takeIf { active } ?: DeactivateToggle

        render(newState)
        action(newIntent)
    }

    fun startEditing() = launch {
        val newIntent = StartEditing
        action(newIntent)
    }

    fun stopEditing() = launch {
        val newIntent = StopEditing
        action(newIntent)
    }

    fun setSourceLanguage(language: String) = launch {
        val newState = state.copy(
            sourceLanguage = language,
            query = "",
            instantQuery = "",
            translationData = Data()
        )
        render(newState)
    }

    fun setTargetLanguage(language: String) = launch {
        val newState = state.copy(targetLanguage = language)
        render(newState)
    }

    fun swapLanguage(
        sourceLanguage: String,
        targetLanguage: String
    ) = launch {
        val newState = state.copy(
            sourceLanguage = targetLanguage,
            targetLanguage = sourceLanguage,
            query = "",
            instantQuery = "",
            translationData = Data()
        )
        render(newState)
    }

    fun setQuery(query: String) = launch {
        val newState = state.copy(
            query = query,
            instantQuery = null,
            translationData = state.translationData.takeIf {
                query.isNotBlank()
            }.orDefault()
        )
        render(newState)
    }

    fun setInstantQuery(instantQuery: String) = launch {
        val newState = state.copy(
            query = instantQuery,
            instantQuery = instantQuery
        )
        render(newState)
    }

    fun clearQuery() = launch {
        val newState = state.copy(
            query = "",
            instantQuery = "",
            translationData = Data()
        )
        render(newState)
    }

    fun translate(
        sourceLanguage: String = state.sourceLanguage,
        targetLanguage: String = state.targetLanguage,
        query: String = state.query
    ) = launch {
        if (query.isBlank()) return@launch

        translateInteractor(
            sourceLanguage = sourceLanguage.takeIf { language ->
                language != LANGUAGE_AUTO
            }.orEmpty(),
            targetLanguage = targetLanguage,
            query = query,
            response = ::onTranslationResponse
        )
    }

    private fun onTranslationResponse(data: Data<Translation>) = launch {
        if (data.isSuccess() && !state.translationData.isLoading()) return@launch

        val newState = state.copy(translationData = data)
        render(newState)
    }

    fun displayResultDetail() = launch {
        val newIntent = DisplayResultDetail(
            sourceLanguage = state.sourceLanguage,
            targetLanguage = state.targetLanguage,
            query = state.query
        )
        action(newIntent)
    }
}

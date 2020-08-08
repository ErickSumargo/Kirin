package com.bael.kirin.feature.translation.tracker

import androidx.core.os.bundleOf
import com.bael.kirin.lib.tracker.contract.Tracker
import javax.inject.Inject

/**
 * Created by ErickSumargo on 01/06/20.
 */

class Tracker @Inject constructor(tracker: Tracker) : Tracker by tracker {

    fun trackToggleService(active: Boolean) {
        track(
            event = TRACK_TOGGLE_SERVICE,
            data = bundleOf(
                "active" to active
            )
        )
    }

    fun trackPreferenceUpdate(key: String, active: Boolean) {
        track(
            event = TRACK_PREFERENCE_UPDATE,
            data = bundleOf(
                "key" to key,
                "active" to active
            )
        )
    }

    fun trackToggleActivation(active: Boolean) {
        track(
            event = TRACK_TOGGLE_ACTIVATION,
            data = bundleOf(
                "active" to active
            )
        )
    }

    fun trackSwapLanguage(sourceLanguage: String, targetLanguage: String) {
        track(
            event = TRACK_SWAP_LANGUAGE,
            data = bundleOf(
                "sl" to sourceLanguage,
                "tl" to targetLanguage
            )
        )
    }

    fun trackClearQuery() {
        track(event = TRACK_CLEAR_QUERY)
    }

    fun trackTranslationData(
        sourceLanguage: String,
        targetLanguage: String,
        query: String
    ) {
        track(
            event = TRACK_TRANSLATION,
            data = bundleOf(
                "sl" to sourceLanguage,
                "tl" to targetLanguage,
                "query" to query
            )
        )
    }

    fun trackStopEditing(by: String = "") {
        track(
            event = TRACK_STOP_EDITING,
            data = bundleOf(
                "by" to by
            )
        )
    }

    fun trackDisplayResultDetail(
        sourceLanguage: String,
        targetLanguage: String,
        query: String,
        result: String
    ) {
        track(
            event = TRACK_DISPLAY_RESULT_DETAIL,
            data = bundleOf(
                "sl" to sourceLanguage,
                "tl" to targetLanguage,
                "query" to query,
                "result" to result
            )
        )
    }

    fun trackInstantTranslate(query: String) {
        track(
            event = TRACK_INSTANT_TRANSLATE,
            data = bundleOf(
                "query" to query
            )
        )
    }

    fun trackDeferredInstantTranslate() {
        track(event = TRACK_DEFERRED_INSTANT_TRANSLATE)
    }

    fun trackShowContextMenu() {
        track(event = TRACK_SHOW_CONTEXT_MENU)
    }

    companion object {
        private const val TRACK_TOGGLE_SERVICE: String = "toggle_service"
        private const val TRACK_PREFERENCE_UPDATE: String = "preference_update"
        private const val TRACK_TOGGLE_ACTIVATION: String = "toggle_activation"
        private const val TRACK_SWAP_LANGUAGE: String = "swap_language"
        private const val TRACK_CLEAR_QUERY: String = "clear_query"
        private const val TRACK_TRANSLATION: String = "translation"
        private const val TRACK_STOP_EDITING: String = "stop_editing"
        private const val TRACK_DISPLAY_RESULT_DETAIL: String = "display_result_detail"
        private const val TRACK_INSTANT_TRANSLATE: String = "instant_translate"
        private const val TRACK_DEFERRED_INSTANT_TRANSLATE: String = "deferred_instant_translate"
        private const val TRACK_SHOW_CONTEXT_MENU: String = "show_context_menu"
    }
}

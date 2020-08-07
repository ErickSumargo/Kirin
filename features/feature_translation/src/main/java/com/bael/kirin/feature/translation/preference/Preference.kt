package com.bael.kirin.feature.translation.preference

import com.bael.kirin.feature.translation.constant.URL_GOOGLE_TRANSLATE
import com.bael.kirin.lib.preference.contract.Preference
import javax.inject.Inject

/**
 * Created by ErickSumargo on 01/06/20.
 */

class Preference @Inject constructor(preference: Preference) : Preference by preference {
    val configSetupCompleted: Boolean
        get() = read(PREFERENCE_CONFIG_SETUP_COMPLETED, false)

    val sourceLanguage: String
        get() = read(PREFERENCE_SOURCE_LANGUAGE, "")

    val targetLanguage: String
        get() = read(PREFERENCE_TARGET_LANGUAGE, "")

    val googleTranslateUrl: String
        get() = read(PREFERENCE_GOOGLE_TRANSLATE_URL, URL_GOOGLE_TRANSLATE)

    val useResponsiveTranslator: Boolean
        get() = read(PREFERENCE_RESPONSIVE_TRANSLATOR, true)

    val useAutoEditingMode: Boolean
        get() = read(PREFERENCE_AUTO_EDITING_MODE, true)

    val autoClearHistory: Boolean
        get() = read(PREFERENCE_AUTO_CLEAR_HISTORY, false)

    val useDimBackground: Boolean
        get() = read(PREFERENCE_DIM_BACKGROUND, true)

    fun setConfigSetupCompleted() {
        write(PREFERENCE_CONFIG_SETUP_COMPLETED, true)
    }

    fun setSourceLanguage(language: String) {
        write(PREFERENCE_SOURCE_LANGUAGE, language)
    }

    fun setTargetLanguage(language: String) {
        write(PREFERENCE_TARGET_LANGUAGE, language)
    }

    fun setGoogleTranslateUrl(url: String) {
        write(PREFERENCE_GOOGLE_TRANSLATE_URL, url)
    }

    companion object {
        private const val PREFERENCE_CONFIG_SETUP_COMPLETED: String = "config_setup_completed"
        private const val PREFERENCE_SOURCE_LANGUAGE: String = "source_language"
        private const val PREFERENCE_TARGET_LANGUAGE: String = "target_language"
        private const val PREFERENCE_GOOGLE_TRANSLATE_URL: String = "google_translate_url"

        const val PREFERENCE_RESPONSIVE_TRANSLATOR: String = "responsive_translator"
        const val PREFERENCE_AUTO_EDITING_MODE: String = "auto_editing_mode"
        const val PREFERENCE_AUTO_CLEAR_HISTORY: String = "auto_clear_history"
        const val PREFERENCE_DIM_BACKGROUND: String = "dim_background"
    }
}

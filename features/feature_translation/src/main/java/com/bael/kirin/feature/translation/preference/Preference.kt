package com.bael.kirin.feature.translation.preference

import com.bael.kirin.feature.translation.constant.URL_GOOGLE_TRANSLATE
import com.bael.kirin.lib.preference.contract.Preference
import javax.inject.Inject

/**
 * Created by ErickSumargo on 01/06/20.
 */

class Preference @Inject constructor(preference: Preference) : Preference by preference {
    var configSetupCompleted: Boolean
        get() = read(
            key = PREFERENCE_CONFIG_SETUP_COMPLETED,
            defaultValue = false
        )
        set(value) = write(
            key = PREFERENCE_CONFIG_SETUP_COMPLETED,
            value = value
        )

    var sourceLanguage: String
        get() = read(
            key = PREFERENCE_SOURCE_LANGUAGE,
            defaultValue = ""
        )
        set(value) = write(
            key = PREFERENCE_SOURCE_LANGUAGE,
            value = value
        )

    var targetLanguage: String
        get() = read(
            key = PREFERENCE_TARGET_LANGUAGE,
            defaultValue = ""
        )
        set(value) = write(
            key = PREFERENCE_TARGET_LANGUAGE,
            value = value
        )

    var googleTranslateUrl: String
        get() = read(
            key = PREFERENCE_GOOGLE_TRANSLATE_URL,
            defaultValue = URL_GOOGLE_TRANSLATE
        )
        set(value) = write(
            key = PREFERENCE_GOOGLE_TRANSLATE_URL,
            value = value
        )

    var useResponsiveTranslator: Boolean
        get() = read(
            key = PREFERENCE_RESPONSIVE_TRANSLATOR,
            defaultValue = true
        )
        set(value) = write(
            key = PREFERENCE_RESPONSIVE_TRANSLATOR,
            value = value
        )

    var useAutoEditingMode: Boolean
        get() = read(
            key = PREFERENCE_AUTO_EDITING_MODE,
            defaultValue = true
        )
        set(value) = write(
            key = PREFERENCE_AUTO_EDITING_MODE,
            value = value
        )

    var autoClearHistory: Boolean
        get() = read(
            key = PREFERENCE_AUTO_CLEAR_HISTORY,
            defaultValue = false
        )
        set(value) = write(
            key = PREFERENCE_AUTO_CLEAR_HISTORY,
            value = value
        )

    var useDimBackground: Boolean
        get() = read(
            key = PREFERENCE_DIM_BACKGROUND,
            defaultValue = true
        )
        set(value) = write(
            key = PREFERENCE_DIM_BACKGROUND,
            value = value
        )

    companion object {
        const val PREFERENCE_CONFIG_SETUP_COMPLETED: String = "config_setup_completed"
        const val PREFERENCE_SOURCE_LANGUAGE: String = "source_language"
        const val PREFERENCE_TARGET_LANGUAGE: String = "target_language"
        const val PREFERENCE_GOOGLE_TRANSLATE_URL: String = "google_translate_url"
        const val PREFERENCE_RESPONSIVE_TRANSLATOR: String = "responsive_translator"
        const val PREFERENCE_AUTO_EDITING_MODE: String = "auto_editing_mode"
        const val PREFERENCE_AUTO_CLEAR_HISTORY: String = "auto_clear_history"
        const val PREFERENCE_DIM_BACKGROUND: String = "dim_background"
    }
}

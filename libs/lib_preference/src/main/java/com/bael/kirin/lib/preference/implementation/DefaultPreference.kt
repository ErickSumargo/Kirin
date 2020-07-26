package com.bael.kirin.lib.preference.implementation

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.bael.kirin.lib.logger.contract.Logger
import com.bael.kirin.lib.preference.contract.Preference
import com.bael.kirin.lib.resource.app.AppInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by ErickSumargo on 01/06/20.
 */

@Suppress("UNCHECKED_CAST")
class DefaultPreference @Inject constructor(
    @ApplicationContext context: Context,
    appInfo: AppInfo,
    logger: Logger
) : Preference,
    Logger by logger {
    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(appInfo.name, MODE_PRIVATE)
    }

    private val editor: Editor by lazy { preferences.edit() }

    override fun <T> read(key: String, defaultValue: T): T {
        return try {
            when (defaultValue) {
                is String -> preferences.getString(key, defaultValue)
                is Boolean -> preferences.getBoolean(key, defaultValue)
                else -> throw UnsupportedOperationException()
            } as T
        } catch (cause: Exception) {
            log(cause)
            defaultValue
        }
    }

    override fun <T> write(key: String, value: T) {
        try {
            when (value) {
                is String -> editor.putString(key, value).commit()
                is Boolean -> editor.putBoolean(key, value).commit()
                else -> throw UnsupportedOperationException()
            }
        } catch (cause: Exception) {
            log(cause)
        }
    }
}

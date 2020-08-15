package com.bael.kirin.lib.preference.implementation

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV
import androidx.security.crypto.EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys.AES256_GCM_SPEC
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
        val masterKey = MasterKey.Builder(context)
            .setKeyGenParameterSpec(AES256_GCM_SPEC)
            .build()

        EncryptedSharedPreferences.create(
            context,
            appInfo.name,
            masterKey,
            AES256_SIV,
            AES256_GCM
        )
    }

    override fun <T> read(key: String, defaultValue: T): T {
        return try {
            when (defaultValue) {
                is String -> {
                    preferences.getString(key, defaultValue)
                }
                is Boolean -> {
                    preferences.getBoolean(key, defaultValue)
                }
                else -> {
                    throw UnsupportedOperationException()
                }
            } as T
        } catch (cause: Exception) {
            log(cause)
            defaultValue
        }
    }

    override fun <T> write(key: String, value: T) {
        try {
            when (value) {
                is String -> {
                    preferences.edit(commit = true) {
                        putString(key, value)
                    }
                }
                is Boolean -> {
                    preferences.edit(commit = true) {
                        putBoolean(key, value)
                    }
                }
                else -> {
                    throw UnsupportedOperationException()
                }
            }
        } catch (cause: Exception) {
            log(cause)
        }
    }
}

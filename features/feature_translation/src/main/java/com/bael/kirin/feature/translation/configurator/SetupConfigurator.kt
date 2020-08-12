package com.bael.kirin.feature.translation.configurator

import android.content.Context
import com.bael.kirin.feature.translation.R
import com.bael.kirin.feature.translation.constant.KEY_PRIVATE
import com.bael.kirin.lib.api.translation.constant.FILE_CONFIG
import com.bael.kirin.lib.data.model.Data
import com.bael.kirin.lib.network.model.Error
import com.bael.kirin.lib.resource.ext.textOf
import com.bael.kirin.lib.security.contract.Cipher
import com.bael.kirin.lib.security.contract.Editor
import com.bael.kirin.lib.storage.contract.Storage
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * Created by ErickSumargo on 01/06/20.
 */

/**
 * Delegated class for handling feature setup configuration.
 */

@ActivityRetainedScoped
class SetupConfigurator @Inject constructor(
    storage: Storage,
    cipher: Cipher,
    editor: Editor,
    @ApplicationContext private val context: Context
) : Storage by storage,
    Cipher by cipher,
    Editor by editor {

    suspend fun setup(callback: (Data<Boolean>) -> Unit) {
        callback(Data(loading = true))

        download(fileName = FILE_CONFIG)
            .collect { response ->
                if (response.isError()) {
                    val result = Data(
                        result = false,
                        error = response.error
                    )
                    callback(result)
                } else {
                    val plainData = decryptData(
                        data = response.data,
                        key = KEY_PRIVATE
                    )

                    saveFile(
                        fileName = FILE_CONFIG,
                        data = plainData,
                        callback = callback
                    )
                }
            }
    }

    private fun decryptData(
        data: ByteArray?,
        key: String
    ): String? {
        if (data == null) return null
        return decrypt(
            cipherData = String(data),
            key = key
        )
    }

    private fun saveFile(
        fileName: String,
        data: String?,
        callback: (Data<Boolean>) -> Unit
    ) {
        writeFile(fileName, data) { success ->
            val response = if (success) {
                Data(result = success)
            } else {
                val error = Error(message = context.textOf(R.string.configuration_setup_failed))
                Data(error = error)
            }
            callback(response)
        }
    }
}

package com.bael.kirin.lib.security.implementation

import android.content.Context
import android.util.Base64.DEFAULT
import android.util.Base64.decode
import android.util.Base64.encode
import com.bael.kirin.lib.logger.contract.Logger
import com.bael.kirin.lib.security.base.BaseSecurity
import com.bael.kirin.lib.security.contract.Cipher
import com.bael.kirin.lib.security.contract.Editor
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.FileInputStream
import javax.crypto.Cipher.DECRYPT_MODE
import javax.crypto.Cipher.ENCRYPT_MODE
import javax.inject.Inject

/**
 * Created by ErickSumargo on 01/06/20.
 */

class DefaultSecurity @Inject constructor(
    logger: Logger,
    @ApplicationContext private val context: Context
) : BaseSecurity(),
    Cipher,
    Editor,
    Logger by logger {

    override fun encrypt(plainData: String, key: String): String? {
        return try {
            val secretKey = generateSecretKey(key)
            cipher.init(ENCRYPT_MODE, secretKey)

            val cipherByte = cipher.doFinal(plainData.toByteArray())
            val encodedData = encode(cipherByte, DEFAULT)

            val cipherData = String(encodedData)
            cipherData
        } catch (cause: Exception) {
            log(cause)
            null
        }
    }

    override fun decrypt(cipherData: String, key: String): String? {
        return try {
            val secretKey = generateSecretKey(key)
            cipher.init(DECRYPT_MODE, secretKey)

            val decodedByte = decode(cipherData.toByteArray(), DEFAULT)
            val plainByte = cipher.doFinal(decodedByte)

            val plainData = String(plainByte)
            plainData
        } catch (cause: Exception) {
            log(cause)
            null
        }
    }

    override fun readFile(fileName: String): FileInputStream? {
        return try {
            getEncryptedFile(
                context,
                fileName,
                readMode = true
            ).openFileInput()
        } catch (cause: Exception) {
            log(cause)
            null
        }
    }

    override fun writeFile(
        fileName: String,
        data: String?,
        callback: (Boolean) -> Unit
    ) {
        try {
            if (data == null) {
                callback(false)
            } else {
                getEncryptedFile(
                    context,
                    fileName,
                    readMode = false
                ).openFileOutput().use { stream ->
                    stream.write(data.toByteArray())
                }.let { callback(true) }
            }
        } catch (cause: Exception) {
            log(cause)
            callback(false)
        }
    }
}

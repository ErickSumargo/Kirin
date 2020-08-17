package com.bael.kirin.lib.security.base

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.EncryptedFile.FileEncryptionScheme
import androidx.security.crypto.EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKeys.AES256_GCM_SPEC
import com.bael.kirin.lib.security.constant.SCHEME_AES
import com.bael.kirin.lib.security.contract.Scheme
import java.io.File
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Created by ErickSumargo on 01/06/20.
 */

abstract class BaseSecurity(
    override val cipherScheme: String = SCHEME_AES
) : Scheme {
    protected val cipher: Cipher by lazy {
        Cipher.getInstance(cipherScheme)
    }

    protected fun generateSecretKey(key: String): SecretKeySpec {
        return SecretKeySpec(key.toByteArray(), cipherScheme)
    }

    protected fun getEncryptedFile(
        context: Context,
        fileName: String,
        readMode: Boolean,
        fileEncryptionScheme: FileEncryptionScheme = AES256_GCM_HKDF_4KB
    ): EncryptedFile {
        val file = getFile(context.filesDir, fileName, readMode)
        val masterKey = generateMasterKey(context)

        return EncryptedFile.Builder(
            context,
            file,
            masterKey,
            fileEncryptionScheme
        ).build()
    }

    private fun getFile(
        directory: File,
        fileName: String,
        readMode: Boolean
    ): File {
        return File(directory, fileName).also { file ->
            if (readMode || !file.exists()) return@also
            file.delete()
        }
    }

    private fun generateMasterKey(
        context: Context,
        keyGenParameterSpec: KeyGenParameterSpec = AES256_GCM_SPEC
    ): MasterKey {
        return MasterKey.Builder(context)
            .setKeyGenParameterSpec(keyGenParameterSpec)
            .build()
    }
}

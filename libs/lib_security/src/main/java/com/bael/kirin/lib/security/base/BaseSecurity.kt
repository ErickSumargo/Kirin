package com.bael.kirin.lib.security.base

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
import androidx.security.crypto.MasterKeys.AES256_GCM_SPEC
import androidx.security.crypto.MasterKeys.getOrCreate
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

    protected fun getEncryptedFile(
        context: Context,
        fileName: String,
        readMode: Boolean
    ): EncryptedFile {
        val file = File(
            context.filesDir,
            fileName
        ).also { file ->
            if (!readMode && file.exists()) {
                file.delete()
            }
        }

        return EncryptedFile.Builder(
            file,
            context,
            getOrCreate(AES256_GCM_SPEC),
            AES256_GCM_HKDF_4KB
        ).build()
    }

    protected fun generateSecretKey(key: String): SecretKeySpec {
        return SecretKeySpec(key.toByteArray(), cipherScheme)
    }
}

package com.bael.kirin.lib.security.contract

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface Cipher {

    fun encrypt(
        plainData: String,
        key: String
    ): String?

    fun decrypt(
        cipherData: String,
        key: String
    ): String?
}

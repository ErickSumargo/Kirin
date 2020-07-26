package com.bael.kirin.lib.storage.contract

import com.bael.kirin.lib.network.model.Error

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface Storage {

    suspend fun download(
        fileName: String,
        response: (ByteArray?, Error?) -> Unit
    )
}

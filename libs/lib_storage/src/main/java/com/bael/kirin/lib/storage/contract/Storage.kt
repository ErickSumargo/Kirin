package com.bael.kirin.lib.storage.contract

import com.bael.kirin.lib.network.model.Response
import kotlinx.coroutines.flow.Flow

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface Storage {

    suspend fun download(fileName: String): Flow<Response<ByteArray>>
}

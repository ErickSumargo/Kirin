package com.bael.kirin.lib.storage.implementation

import com.bael.kirin.lib.logger.contract.Logger
import com.bael.kirin.lib.network.model.Error
import com.bael.kirin.lib.network.model.Response
import com.bael.kirin.lib.storage.constant.SIZE_MAX_FILE
import com.bael.kirin.lib.storage.contract.Storage
import com.bael.kirin.lib.threading.ext.safeOffer
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

/**
 * Created by ErickSumargo on 01/06/20.
 */

@ExperimentalCoroutinesApi
class FirebaseStorage @Inject constructor(
    logger: Logger
) : Storage,
    Logger by logger {

    override suspend fun download(
        fileName: String
    ): Flow<Response<ByteArray>> = channelFlow {
        Firebase.storage
            .getReference(fileName)
            .getBytes(SIZE_MAX_FILE)
            .addOnSuccessListener { data ->
                safeOffer(Response(data = data))
            }.addOnFailureListener { cause ->
                log(cause)

                val error = Error(message = cause.message.toString())
                safeOffer(Response(error = error))
            }

        awaitClose {}
    }
}

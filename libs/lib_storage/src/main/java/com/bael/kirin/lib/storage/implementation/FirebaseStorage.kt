package com.bael.kirin.lib.storage.implementation

import com.bael.kirin.lib.logger.contract.Logger
import com.bael.kirin.lib.network.model.Error
import com.bael.kirin.lib.storage.constant.SIZE_MAX_FILE
import com.bael.kirin.lib.storage.contract.Storage
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import javax.inject.Inject

/**
 * Created by ErickSumargo on 01/06/20.
 */

class FirebaseStorage @Inject constructor(
    logger: Logger
) : Storage,
    Logger by logger {

    override suspend fun download(
        fileName: String,
        response: (ByteArray?, Error?) -> Unit
    ) {
        Firebase.storage
            .getReference(fileName)
            .getBytes(SIZE_MAX_FILE)
            .addOnSuccessListener { data ->
                response(data, null)
            }.addOnFailureListener { cause ->
                log(cause)

                val error = Error(message = cause.message.toString())
                response(null, error)
            }
    }
}

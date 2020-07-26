package com.bael.kirin.lib.network.model

/**
 * Created by ErickSumargo on 15/06/20.
 */

class Response<T>(
    val data: T? = null,
    val error: Error? = null
) {

    fun isSuccess() = data != null

    fun isError() = error != null
}

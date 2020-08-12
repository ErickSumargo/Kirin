package com.bael.kirin.lib.data.model

/**
 * Created by ErickSumargo on 15/06/20.
 */

data class Data<T>(
    val loading: Boolean = false,
    val result: T? = null,
    val error: Exception? = null
) {

    fun isLoading() = loading

    fun isSuccess() = result != null

    fun isError() = error != null
}

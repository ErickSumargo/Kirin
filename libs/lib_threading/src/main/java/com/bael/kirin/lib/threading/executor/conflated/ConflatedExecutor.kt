package com.bael.kirin.lib.threading.executor.conflated

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface ConflatedExecutor {

    suspend fun conflate(block: suspend () -> Unit)
}

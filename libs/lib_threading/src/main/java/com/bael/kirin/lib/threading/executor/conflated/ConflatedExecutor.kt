package com.bael.kirin.lib.threading.executor.conflated

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface ConflatedExecutor {

    /**
     * @param block is executed after all previous tasks are canceled.
     *
     * When several coroutines conflate at the time, only one will run and
     * the others will be cancelled.
     */
    suspend fun conflate(block: suspend () -> Unit)
}

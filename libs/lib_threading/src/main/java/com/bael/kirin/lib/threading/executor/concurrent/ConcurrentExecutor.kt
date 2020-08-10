package com.bael.kirin.lib.threading.executor.concurrent

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface ConcurrentExecutor {

    /**
     * @param block is executed along with other running tasks.
     *
     * When several coroutines run at the same time, they will execute
     * each task concurrently.
     */
    suspend fun run(block: suspend () -> Unit)
}

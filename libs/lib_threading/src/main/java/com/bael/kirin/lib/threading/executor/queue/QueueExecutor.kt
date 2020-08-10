package com.bael.kirin.lib.threading.executor.queue

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface QueueExecutor {

    suspend fun enqueue(block: suspend () -> Unit)
}

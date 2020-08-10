package com.bael.kirin.lib.threading.executor.queue

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface QueueExecutor {

    /**
     * @param block is executed after all previous tasks have completed.
     *
     * When several coroutines enqueue at the same time, they will queue up in the order.
     * Then, one coroutine will enter the block at a time.
     */
    suspend fun enqueue(block: suspend () -> Unit)
}

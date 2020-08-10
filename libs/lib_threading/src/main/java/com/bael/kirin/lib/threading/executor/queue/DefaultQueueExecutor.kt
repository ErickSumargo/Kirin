package com.bael.kirin.lib.threading.executor.queue

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

/**
 * Created by ErickSumargo on 01/06/20.
 */

class DefaultQueueExecutor @Inject constructor() : QueueExecutor {
    private val mutex: Mutex = Mutex()

    override suspend fun enqueue(block: suspend () -> Unit) {
        mutex.withLock { block() }
    }
}

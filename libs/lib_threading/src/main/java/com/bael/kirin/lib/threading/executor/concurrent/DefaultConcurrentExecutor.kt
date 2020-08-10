package com.bael.kirin.lib.threading.executor.concurrent

import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject

/**
 * Created by ErickSumargo on 01/06/20.
 */

class DefaultConcurrentExecutor @Inject constructor() : ConcurrentExecutor {
    private val mutex: Mutex = Mutex()

    override suspend fun run(block: suspend () -> Unit) {
        block()
    }
}

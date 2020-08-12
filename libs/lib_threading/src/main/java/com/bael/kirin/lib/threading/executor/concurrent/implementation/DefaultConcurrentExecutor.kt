package com.bael.kirin.lib.threading.executor.concurrent.implementation

import com.bael.kirin.lib.threading.executor.concurrent.contract.ConcurrentExecutor
import javax.inject.Inject

/**
 * Created by ErickSumargo on 01/06/20.
 */

class DefaultConcurrentExecutor @Inject constructor() : ConcurrentExecutor {

    override suspend fun run(block: suspend () -> Unit) {
        block()
    }
}

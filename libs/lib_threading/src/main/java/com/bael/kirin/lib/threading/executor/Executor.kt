package com.bael.kirin.lib.threading.executor

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface Executor {

    suspend fun execute(
        schema: ExecutorSchema,
        block: suspend () -> Unit
    )
}

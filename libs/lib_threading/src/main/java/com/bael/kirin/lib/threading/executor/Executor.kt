package com.bael.kirin.lib.threading.executor

import com.bael.kirin.lib.threading.executor.schema.ExecutorSchema

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface Executor {

    suspend fun execute(
        schema: ExecutorSchema,
        block: suspend () -> Unit
    )
}

package com.bael.kirin.lib.threading.contract

import com.bael.kirin.lib.threading.executor.schema.ExecutorSchema
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * Created by ErickSumargo on 15/06/20.
 */

interface Threading : CoroutineScope {
    val executorSchema: ExecutorSchema

    fun launch(
        thread: CoroutineContext = coroutineContext,
        schema: ExecutorSchema = executorSchema,
        block: suspend CoroutineScope.() -> Unit
    )
}

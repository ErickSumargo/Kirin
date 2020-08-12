package com.bael.kirin.lib.threading.contract

import com.bael.kirin.lib.threading.executor.schema.ExecutorSchema
import com.bael.kirin.lib.threading.executor.schema.ExecutorSchema.Queue
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * Created by ErickSumargo on 15/06/20.
 */

interface Threading : CoroutineScope {

    fun launch(
        thread: CoroutineContext = coroutineContext,
        schema: ExecutorSchema = Queue,
        block: suspend CoroutineScope.() -> Unit
    )
}

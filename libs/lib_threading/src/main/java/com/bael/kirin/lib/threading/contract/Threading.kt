package com.bael.kirin.lib.threading.contract

import com.bael.kirin.lib.threading.executor.ExecutorSchema
import com.bael.kirin.lib.threading.executor.ExecutorSchema.Concurrent
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * Created by ErickSumargo on 15/06/20.
 */

interface Threading : CoroutineScope {

    fun launch(
        thread: CoroutineContext = coroutineContext,
        schema: ExecutorSchema = Concurrent,
        block: suspend CoroutineScope.() -> Unit
    )
}

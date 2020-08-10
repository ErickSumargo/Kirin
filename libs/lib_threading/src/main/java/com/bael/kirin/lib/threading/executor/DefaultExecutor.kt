package com.bael.kirin.lib.threading.executor

import com.bael.kirin.lib.threading.executor.conflated.ConflatedExecutor
import com.bael.kirin.lib.threading.executor.queue.QueueExecutor
import com.bael.kirin.lib.threading.executor.schema.ExecutorSchema
import com.bael.kirin.lib.threading.executor.schema.ExecutorSchema.Conflated
import com.bael.kirin.lib.threading.executor.schema.ExecutorSchema.Default
import com.bael.kirin.lib.threading.executor.schema.ExecutorSchema.Queue
import javax.inject.Inject

/**
 * Created by ErickSumargo on 01/06/20.
 */

class DefaultExecutor @Inject constructor(
    private val queueExecutor: QueueExecutor,
    private val conflatedExecutor: ConflatedExecutor
) : Executor,
    QueueExecutor by queueExecutor,
    ConflatedExecutor by conflatedExecutor {

    override suspend fun execute(
        schema: ExecutorSchema,
        block: suspend () -> Unit
    ) {
        when (schema) {
            is Default -> {
                block()
            }
            is Queue -> {
                enqueue(block)
            }
            is Conflated -> {
                conflate(block)
            }
        }
    }
}

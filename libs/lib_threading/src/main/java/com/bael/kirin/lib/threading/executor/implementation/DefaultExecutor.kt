package com.bael.kirin.lib.threading.executor.implementation

import com.bael.kirin.lib.threading.executor.concurrent.contract.ConcurrentExecutor
import com.bael.kirin.lib.threading.executor.conflated.contract.ConflatedExecutor
import com.bael.kirin.lib.threading.executor.contract.Executor
import com.bael.kirin.lib.threading.executor.queue.contract.QueueExecutor
import com.bael.kirin.lib.threading.executor.schema.ExecutorSchema
import com.bael.kirin.lib.threading.executor.schema.ExecutorSchema.Concurrent
import com.bael.kirin.lib.threading.executor.schema.ExecutorSchema.Conflated
import com.bael.kirin.lib.threading.executor.schema.ExecutorSchema.Queue
import javax.inject.Inject

/**
 * Created by ErickSumargo on 01/06/20.
 */

/**
 * Facade class to decide which executor to be used to execute task based on schema.
 *
 * @property queueExecutor is the default one.
 */
class DefaultExecutor @Inject constructor(
    private val queueExecutor: QueueExecutor,
    private val conflatedExecutor: ConflatedExecutor,
    private val concurrentExecutor: ConcurrentExecutor
) : Executor,
    QueueExecutor by queueExecutor,
    ConflatedExecutor by conflatedExecutor,
    ConcurrentExecutor by concurrentExecutor {

    override suspend fun execute(
        schema: ExecutorSchema,
        block: suspend () -> Unit
    ) {
        when (schema) {
            is Queue -> {
                enqueue(block)
            }
            is Conflated -> {
                conflate(block)
            }
            is Concurrent -> {
                run(block)
            }
        }
    }
}

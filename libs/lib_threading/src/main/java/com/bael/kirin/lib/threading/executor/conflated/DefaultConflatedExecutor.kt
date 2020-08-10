package com.bael.kirin.lib.threading.executor.conflated

import kotlinx.coroutines.CoroutineStart.LAZY
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.yield
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

/**
 * Created by ErickSumargo on 01/06/20.
 */

class DefaultConflatedExecutor @Inject constructor() : ConflatedExecutor {
    private val activeJob: AtomicReference<Deferred<Unit>?> = AtomicReference(null)

    override suspend fun conflate(block: suspend () -> Unit) {
        activeJob.get()?.cancelAndJoin()

        coroutineScope {
            val newJob = async(start = LAZY) { block() }
            newJob.invokeOnCompletion {
                activeJob.compareAndSet(newJob, null)
            }

            while (true) {
                if (!activeJob.compareAndSet(null, newJob)) {
                    activeJob.get()?.cancelAndJoin()
                    yield()
                } else {
                    newJob.await()
                    break
                }
            }
        }
    }
}

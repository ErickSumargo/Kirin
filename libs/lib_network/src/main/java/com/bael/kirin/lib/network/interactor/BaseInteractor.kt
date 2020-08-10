package com.bael.kirin.lib.network.interactor

import com.bael.kirin.lib.logger.contract.Logger
import com.bael.kirin.lib.threading.contract.Threading
import com.bael.kirin.lib.threading.executor.Executor
import com.bael.kirin.lib.threading.executor.ExecutorSchema
import com.bael.kirin.lib.threading.util.Util.IOThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by ErickSumargo on 15/06/20.
 */

abstract class BaseInteractor(
    override val coroutineContext: CoroutineContext = IOThread
) : Threading {
    @Inject
    internal lateinit var executor: Executor

    @Inject
    internal lateinit var logger: Logger

    override fun launch(
        thread: CoroutineContext,
        schema: ExecutorSchema,
        block: suspend CoroutineScope.() -> Unit
    ) {
        try {
            launch(context = thread) {
                executor.execute(schema) { block() }
            }
        } catch (cause: Exception) {
            logger.log(cause)
        }
    }
}

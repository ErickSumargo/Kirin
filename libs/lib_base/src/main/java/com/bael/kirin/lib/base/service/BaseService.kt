package com.bael.kirin.lib.base.service

import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.bael.kirin.lib.logger.contract.Logger
import com.bael.kirin.lib.threading.contract.Threading
import com.bael.kirin.lib.threading.executor.contract.Executor
import com.bael.kirin.lib.threading.executor.schema.ExecutorSchema
import com.bael.kirin.lib.threading.util.Util.MainThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by ErickSumargo on 01/06/20.
 */

abstract class BaseService :
    LifecycleService(),
    Threading {
    override val coroutineContext: CoroutineContext = MainThread

    @Inject
    internal lateinit var executor: Executor

    @Inject
    protected lateinit var logger: Logger

    override fun launch(
        thread: CoroutineContext,
        schema: ExecutorSchema,
        block: suspend CoroutineScope.() -> Unit
    ) {
        try {
            lifecycleScope.launch(context = thread) {
                executor.execute(schema) { block() }
            }
        } catch (cause: Exception) {
            logger.log(cause)
        }
    }
}

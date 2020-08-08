package com.bael.kirin.lib.base.service

import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.bael.kirin.lib.logger.contract.Logger
import com.bael.kirin.lib.threading.contract.Threading
import com.bael.kirin.lib.threading.util.Util.MainThread
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by ErickSumargo on 01/06/20.
 */

abstract class BaseService :
    LifecycleService(),
    Threading {
    override val coroutineContext: CoroutineContext get() = MainThread

    @Inject
    protected lateinit var logger: Logger

    override fun execute(
        thread: CoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) {
        try {
            lifecycleScope.launchWhenCreated(block)
        } catch (cause: Exception) {
            logger.log(cause)
        }
    }
}

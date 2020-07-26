package com.bael.kirin.lib.threading.contract

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * Created by ErickSumargo on 15/06/20.
 */

interface Threading : CoroutineScope {

    fun execute(
        thread: CoroutineContext = coroutineContext,
        block: suspend CoroutineScope.() -> Unit
    )
}

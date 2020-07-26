package com.bael.kirin.lib.threading.util

import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

/**
 * Created by ErickSumargo on 15/06/20.
 */

object Util {
    val MainThread: CoroutineContext
        get() = Main + SupervisorJob()

    val DefaultThread: CoroutineContext
        get() = Default + SupervisorJob()

    val IOThread: CoroutineContext
        get() = IO + SupervisorJob()
}

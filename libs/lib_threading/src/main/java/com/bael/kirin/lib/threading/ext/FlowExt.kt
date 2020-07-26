package com.bael.kirin.lib.threading.ext

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext

/**
 * Created by ErickSumargo on 15/06/20.
 */

suspend fun <T> Flow<T>.subscribe(
    thread: CoroutineContext = IO
) = flowOn(thread).collect()

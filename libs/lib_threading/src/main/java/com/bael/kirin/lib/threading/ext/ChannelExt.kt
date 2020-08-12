package com.bael.kirin.lib.threading.ext

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel

/**
 * Created by ErickSumargo on 15/06/20.
 */

@ExperimentalCoroutinesApi
fun <T> SendChannel<T>.safeOffer(value: T) {
    if (!isClosedForSend) {
        try { offer(value) }
        catch (cause: Exception) {}
    }
}

@ExperimentalCoroutinesApi
suspend fun <T> SendChannel<T>.safeSend(value: T) {
    if (!isClosedForSend) {
        try { send(value) }
        catch (cause: Exception) {}
    }
}

package com.bael.kirin.lib.threading.ext

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import java.util.concurrent.CancellationException

/**
 * Created by ErickSumargo on 15/06/20.
 */

@ExperimentalCoroutinesApi
fun <T> SendChannel<T>.safeOffer(value: T): Boolean {
    return !isClosedForSend && try {
        offer(value)
    } catch (e: CancellationException) {
        false
    }
}

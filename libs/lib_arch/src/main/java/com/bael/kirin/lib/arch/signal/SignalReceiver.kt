package com.bael.kirin.lib.arch.signal

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface SignalReceiver<in T> {

    fun receiveSignal(signal: T)
}

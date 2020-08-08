package com.bael.kirin.lib.base.signal

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface SignalReceiver<in T> {

    fun receiveSignal(signal: T)
}

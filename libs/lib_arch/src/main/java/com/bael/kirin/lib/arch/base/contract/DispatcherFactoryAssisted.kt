package com.bael.kirin.lib.arch.base.contract

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface DispatcherFactoryAssisted<in R, in A, out D> {

    fun create(renderer: R, action: A): D
}

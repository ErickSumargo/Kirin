package com.bael.kirin.lib.base.di.assisted

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface DispatcherFactoryAssisted<in R, in A, out D> {

    fun create(renderer: R, action: A): D
}

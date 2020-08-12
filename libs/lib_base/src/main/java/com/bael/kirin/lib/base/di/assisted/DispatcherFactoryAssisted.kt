package com.bael.kirin.lib.base.di.assisted

/**
 * Created by ErickSumargo on 01/06/20.
 */

interface DispatcherFactoryAssisted<in VM, in R, in A, out D> {

    fun create(viewModel: VM, renderer: R, action: A): D
}

package com.bael.kirin.lib.threading.di.module

import com.bael.kirin.lib.threading.executor.concurrent.contract.ConcurrentExecutor
import com.bael.kirin.lib.threading.executor.concurrent.implementation.DefaultConcurrentExecutor
import com.bael.kirin.lib.threading.executor.conflated.contract.ConflatedExecutor
import com.bael.kirin.lib.threading.executor.conflated.implementation.DefaultConflatedExecutor
import com.bael.kirin.lib.threading.executor.contract.Executor
import com.bael.kirin.lib.threading.executor.implementation.DefaultExecutor
import com.bael.kirin.lib.threading.executor.queue.contract.QueueExecutor
import com.bael.kirin.lib.threading.executor.queue.implementation.DefaultQueueExecutor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

/**
 * Created by ErickSumargo on 01/06/20.
 */

@Module
@InstallIn(ApplicationComponent::class)
abstract class ThreadingModule {

    @Binds
    abstract fun bindExecutor(executor: DefaultExecutor): Executor

    @Binds
    abstract fun bindConcurrentExecutor(executor: DefaultConcurrentExecutor): ConcurrentExecutor

    @Binds
    abstract fun bindQueueExecutor(executor: DefaultQueueExecutor): QueueExecutor

    @Binds
    abstract fun bindConflatedExecutor(executor: DefaultConflatedExecutor): ConflatedExecutor
}

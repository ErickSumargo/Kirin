package com.bael.kirin.lib.threading.di.module

import com.bael.kirin.lib.threading.executor.DefaultExecutor
import com.bael.kirin.lib.threading.executor.Executor
import com.bael.kirin.lib.threading.executor.conflated.ConflatedExecutor
import com.bael.kirin.lib.threading.executor.conflated.DefaultConflatedExecutor
import com.bael.kirin.lib.threading.executor.queue.DefaultQueueExecutor
import com.bael.kirin.lib.threading.executor.queue.QueueExecutor
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
    abstract fun bindQueueExecutor(executor: DefaultQueueExecutor): QueueExecutor

    @Binds
    abstract fun bindConflatedExecutor(executor: DefaultConflatedExecutor): ConflatedExecutor
}

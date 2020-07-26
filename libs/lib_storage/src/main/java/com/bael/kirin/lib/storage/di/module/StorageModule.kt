package com.bael.kirin.lib.storage.di.module

import com.bael.kirin.lib.storage.contract.Storage
import com.bael.kirin.lib.storage.implementation.FirebaseStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

/**
 * Created by ErickSumargo on 01/06/20.
 */

@Module
@InstallIn(ApplicationComponent::class)
abstract class StorageModule {

    @Singleton
    @Binds
    abstract fun bindStorage(storage: FirebaseStorage): Storage
}

package com.bael.kirin.lib.security.di.module

import com.bael.kirin.lib.security.contract.Cipher
import com.bael.kirin.lib.security.contract.Editor
import com.bael.kirin.lib.security.implementation.DefaultSecurity
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
abstract class SecurityModule {

    @Singleton
    @Binds
    abstract fun bindCipher(cipher: DefaultSecurity): Cipher

    @Singleton
    @Binds
    abstract fun bindEditor(editor: DefaultSecurity): Editor
}

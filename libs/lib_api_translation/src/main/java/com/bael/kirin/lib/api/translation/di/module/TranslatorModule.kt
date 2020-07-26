package com.bael.kirin.lib.api.translation.di.module

import com.bael.kirin.lib.api.translation.constant.FILE_CONFIG
import com.bael.kirin.lib.security.contract.Editor
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.GoogleCredentials.fromStream
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

/**
 * Created by ErickSumargo on 15/06/20.
 */

@Module
@InstallIn(ApplicationComponent::class)
object TranslatorModule {

    @Singleton
    @Provides
    fun provideCredentials(editor: Editor): GoogleCredentials {
        return editor.readFile(fileName = FILE_CONFIG).use { stream ->
            fromStream(stream)
        }
    }

    @Singleton
    @Provides
    fun provideTranslator(credentials: GoogleCredentials): Translate {
        return TranslateOptions.newBuilder()
            .setCredentials(credentials)
            .build()
            .service
    }
}

package com.bael.kirin.lib.resource.di.module

import android.content.Context
import com.bael.kirin.lib.resource.R
import com.bael.kirin.lib.resource.app.AppInfo
import com.bael.kirin.lib.resource.ext.textOf
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

/**
 * Created by ErickSumargo on 01/06/20.
 */

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun @receiver:ApplicationContext Context.provideAppInfo(): AppInfo {
        return AppInfo(
            id = packageName.hashCode(),
            packageName = packageName,
            name = textOf(R.string.app_name),
            description = textOf(R.string.app_description),
            version = packageManager.getPackageInfo(packageName, 0).versionName
        )
    }
}

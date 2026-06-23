package com.ngoctientnt.template.di

import com.ngoctientnt.template.BuildConfig
import com.ngoctientnt.template.core.appinfo.config.AppInfoConfig
import com.ngoctientnt.template.core.config.NetworkConfig
import com.ngoctientnt.template.core.config.SocialAuthConfig
import com.ngoctientnt.template.ui.component.image.AppImageLoaderConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConfigModule {

    @Provides
    @Singleton
    fun provideNetworkConfig(): NetworkConfig = NetworkConfig(
        enableHttpLogging = BuildConfig.DEBUG,
        attachAppInfoHeaders = true,
    )

    @Provides
    @Singleton
    fun provideAppInfoConfig(): AppInfoConfig = AppInfoConfig(
        includeAndroidId = false,
        includeScreenMetrics = true,
        includeHttpHeaders = true,
        includeUdidInHeaders = true,
    )

    @Provides
    @Singleton
    fun provideAppImageLoaderConfig(): AppImageLoaderConfig = AppImageLoaderConfig(
        crossfadeEnabled = true,
        crossfadeDurationMillis = 300,
        allowHardware = true,
        respectCacheHeaders = true,
        memoryCacheMaxSizePercent = 0.25,
        diskCacheMaxSizePercent = 0.02,
        diskCacheDirectoryName = com.ngoctientnt.template.core.config.AppConfig.IMAGE_DISK_CACHE_DIR,
    )

    @Provides
    @Singleton
    fun provideSocialAuthConfig(): SocialAuthConfig = SocialAuthConfig(
        googleWebClientId = BuildConfig.GOOGLE_WEB_CLIENT_ID,
        facebookAppId = BuildConfig.FACEBOOK_APP_ID,
        facebookClientToken = BuildConfig.FACEBOOK_CLIENT_TOKEN,
    )
}

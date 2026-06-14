package com.ngoctientnt.template.di

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import com.ngoctientnt.template.BuildConfig
import com.ngoctientnt.template.ui.component.image.AppImageLoaderConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object ImageModule {

    @Provides
    @Singleton
    fun provideImageLoader(
        @ApplicationContext context: Context,
        @PublicOkHttpClient okHttpClient: OkHttpClient,
        config: AppImageLoaderConfig,
    ): ImageLoader {
        return ImageLoader.Builder(context)
            .okHttpClient(okHttpClient)
            .crossfade(config.crossfadeEnabled)
            .crossfade(config.crossfadeDurationMillis)
            .allowHardware(config.allowHardware)
            .respectCacheHeaders(config.respectCacheHeaders)
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(config.memoryCacheMaxSizePercent)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve(config.diskCacheDirectoryName))
                    .maxSizePercent(config.diskCacheMaxSizePercent)
                    .build()
            }
            .apply {
                if (BuildConfig.DEBUG) {
                    logger(DebugLogger())
                }
            }
            .build()
    }
}

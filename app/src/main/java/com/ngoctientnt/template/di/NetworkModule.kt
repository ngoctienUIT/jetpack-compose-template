package com.ngoctientnt.template.di

import com.ngoctientnt.template.BuildConfig
import com.ngoctientnt.template.core.appinfo.network.AppInfoInterceptor
import com.ngoctientnt.template.core.config.NetworkConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        isLenient = true
        encodeDefaults = true
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        networkConfig: NetworkConfig,
        loggingInterceptor: HttpLoggingInterceptor,
        appInfoInterceptor: AppInfoInterceptor,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(networkConfig.connectTimeoutSeconds, TimeUnit.SECONDS)
            .readTimeout(networkConfig.readTimeoutSeconds, TimeUnit.SECONDS)
            .writeTimeout(networkConfig.writeTimeoutSeconds, TimeUnit.SECONDS)
            .callTimeout(networkConfig.callTimeoutSeconds, TimeUnit.SECONDS)

        if (networkConfig.attachAppInfoHeaders) {
            builder.addInterceptor(appInfoInterceptor)
        }
        if (networkConfig.enableHttpLogging) {
            builder.addInterceptor(loggingInterceptor)
        }

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
}

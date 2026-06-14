package com.ngoctientnt.template.di

import com.ngoctientnt.template.BuildConfig
import com.ngoctientnt.template.core.appinfo.network.AppInfoInterceptor
import com.ngoctientnt.template.core.auth.data.remote.AuthApiService
import com.ngoctientnt.template.core.auth.network.AuthInterceptor
import com.ngoctientnt.template.core.auth.network.ForbiddenInterceptor
import com.ngoctientnt.template.core.auth.network.TokenAuthenticator
import com.ngoctientnt.template.core.config.NetworkConfig
import com.ngoctientnt.template.feature.explore.data.remote.ExploreApiService
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
    fun provideLoggingInterceptor(networkConfig: NetworkConfig): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (networkConfig.enableHttpLogging) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
            redactHeader(AuthInterceptor.AUTHORIZATION_HEADER)
        }

    @Provides
    @Singleton
    @PublicOkHttpClient
    fun providePublicOkHttpClient(
        networkConfig: NetworkConfig,
        loggingInterceptor: HttpLoggingInterceptor,
        appInfoInterceptor: AppInfoInterceptor,
    ): OkHttpClient = buildBaseClient(
        networkConfig = networkConfig,
        loggingInterceptor = loggingInterceptor,
        appInfoInterceptor = appInfoInterceptor,
    ).build()

    @Provides
    @Singleton
    @AuthenticatedOkHttpClient
    fun provideAuthenticatedOkHttpClient(
        networkConfig: NetworkConfig,
        loggingInterceptor: HttpLoggingInterceptor,
        appInfoInterceptor: AppInfoInterceptor,
        authInterceptor: AuthInterceptor,
        forbiddenInterceptor: ForbiddenInterceptor,
        tokenAuthenticator: TokenAuthenticator,
    ): OkHttpClient = buildBaseClient(
        networkConfig = networkConfig,
        loggingInterceptor = loggingInterceptor,
        appInfoInterceptor = appInfoInterceptor,
    )
        .addInterceptor(authInterceptor)
        .addNetworkInterceptor(forbiddenInterceptor)
        .authenticator(tokenAuthenticator)
        .build()

    @Provides
    @Singleton
    @PublicRetrofit
    fun providePublicRetrofit(
        @PublicOkHttpClient okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit = createRetrofit(okHttpClient, json)

    @Provides
    @Singleton
    @AuthenticatedRetrofit
    fun provideAuthenticatedRetrofit(
        @AuthenticatedOkHttpClient okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit = createRetrofit(okHttpClient, json)

    @Provides
    @Singleton
    fun provideAuthApiService(
        @PublicRetrofit retrofit: Retrofit,
    ): AuthApiService = retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideExploreApiService(
        @AuthenticatedRetrofit retrofit: Retrofit,
    ): ExploreApiService = retrofit.create(ExploreApiService::class.java)

    private fun buildBaseClient(
        networkConfig: NetworkConfig,
        loggingInterceptor: HttpLoggingInterceptor,
        appInfoInterceptor: AppInfoInterceptor,
    ): OkHttpClient.Builder {
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

        return builder
    }

    private fun createRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
}

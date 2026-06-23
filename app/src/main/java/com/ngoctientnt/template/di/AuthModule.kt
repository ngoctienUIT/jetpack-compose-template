package com.ngoctientnt.template.di

import com.ngoctientnt.template.core.auth.data.AuthRepositoryImpl
import com.ngoctientnt.template.core.auth.data.remote.AuthRemoteDataSource
import com.ngoctientnt.template.core.auth.data.remote.RetrofitAuthRemoteDataSource
import com.ngoctientnt.template.core.auth.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl,
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(
        retrofitAuthRemoteDataSource: RetrofitAuthRemoteDataSource,
    ): AuthRemoteDataSource
}

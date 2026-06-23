package com.ngoctientnt.template.core.auth.data

import com.ngoctientnt.template.core.auth.data.local.SecureTokenStore
import com.ngoctientnt.template.core.auth.data.remote.AuthRemoteDataSource
import com.ngoctientnt.template.core.auth.domain.model.AuthTokens
import com.ngoctientnt.template.core.auth.domain.model.SocialAuthIntent
import com.ngoctientnt.template.core.auth.domain.model.SocialIdentity
import com.ngoctientnt.template.core.auth.domain.repository.AuthRepository
import com.ngoctientnt.template.core.network.NetworkManager
import com.ngoctientnt.template.core.network.result.ApiResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val secureTokenStore: SecureTokenStore,
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val networkManager: NetworkManager,
) : AuthRepository {

    override fun observeTokens(): Flow<AuthTokens?> = secureTokenStore.tokens

    override suspend fun getAccessToken(): String? {
        return secureTokenStore.readTokensSync()?.accessToken
    }

    override suspend fun saveTokens(tokens: AuthTokens) {
        secureTokenStore.saveTokens(tokens)
    }

    override suspend fun clearTokens() {
        secureTokenStore.clearTokens()
    }

    override suspend fun login(email: String, password: String): ApiResult<AuthTokens> {
        return networkManager.safeApiCall {
            authRemoteDataSource.login(email, password)
        }.also { result ->
            if (result is ApiResult.Success) {
                secureTokenStore.saveTokens(result.data)
            }
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        displayName: String?,
    ): ApiResult<AuthTokens> {
        return networkManager.safeApiCall {
            authRemoteDataSource.register(
                email = email,
                password = password,
                displayName = displayName,
            )
        }.also { result ->
            if (result is ApiResult.Success) {
                secureTokenStore.saveTokens(result.data)
            }
        }
    }

    override suspend fun socialAuth(
        identity: SocialIdentity,
        intent: SocialAuthIntent,
    ): ApiResult<AuthTokens> {
        return networkManager.safeApiCall {
            authRemoteDataSource.socialAuth(
                identity = identity,
                intent = intent,
            )
        }.also { result ->
            if (result is ApiResult.Success) {
                secureTokenStore.saveTokens(result.data)
            }
        }
    }

    override suspend fun refreshToken(refreshToken: String): ApiResult<AuthTokens> {
        return networkManager.safeApiCall {
            authRemoteDataSource.refreshToken(refreshToken)
        }.also { result ->
            if (result is ApiResult.Success) {
                secureTokenStore.saveTokens(result.data)
            }
        }
    }
}

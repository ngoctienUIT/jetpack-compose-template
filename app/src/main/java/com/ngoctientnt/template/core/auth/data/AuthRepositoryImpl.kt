package com.ngoctientnt.template.core.auth.data

import com.ngoctientnt.template.core.auth.data.local.SecureTokenStore
import com.ngoctientnt.template.core.auth.data.remote.AuthApiService
import com.ngoctientnt.template.core.auth.data.remote.dto.LoginRequestDto
import com.ngoctientnt.template.core.auth.data.remote.dto.RefreshTokenRequestDto
import com.ngoctientnt.template.core.auth.data.remote.mapper.toDomain
import com.ngoctientnt.template.core.auth.domain.model.AuthTokens
import com.ngoctientnt.template.core.auth.domain.repository.AuthRepository
import com.ngoctientnt.template.core.network.NetworkManager
import com.ngoctientnt.template.core.network.result.ApiResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val secureTokenStore: SecureTokenStore,
    private val authApiService: AuthApiService,
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
            authApiService.login(
                LoginRequestDto(
                    email = email,
                    password = password,
                ),
            ).toDomain()
        }.also { result ->
            if (result is ApiResult.Success) {
                secureTokenStore.saveTokens(result.data)
            }
        }
    }

    override suspend fun refreshToken(refreshToken: String): ApiResult<AuthTokens> {
        return networkManager.safeApiCall {
            authApiService.refreshToken(
                RefreshTokenRequestDto(refreshToken = refreshToken),
            ).toDomain()
        }.also { result ->
            if (result is ApiResult.Success) {
                secureTokenStore.saveTokens(result.data)
            }
        }
    }
}

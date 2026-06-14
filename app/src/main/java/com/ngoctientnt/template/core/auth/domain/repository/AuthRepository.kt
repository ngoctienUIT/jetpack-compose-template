package com.ngoctientnt.template.core.auth.domain.repository

import com.ngoctientnt.template.core.auth.domain.model.AuthTokens
import com.ngoctientnt.template.core.network.result.ApiResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun observeTokens(): Flow<AuthTokens?>

    suspend fun getAccessToken(): String?

    suspend fun saveTokens(tokens: AuthTokens)

    suspend fun clearTokens()

    suspend fun login(email: String, password: String): ApiResult<AuthTokens>

    suspend fun refreshToken(refreshToken: String): ApiResult<AuthTokens>
}

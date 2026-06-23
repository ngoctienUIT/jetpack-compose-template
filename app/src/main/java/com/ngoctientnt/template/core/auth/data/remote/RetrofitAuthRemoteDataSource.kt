package com.ngoctientnt.template.core.auth.data.remote

import com.ngoctientnt.template.core.auth.data.remote.dto.LoginRequestDto
import com.ngoctientnt.template.core.auth.data.remote.dto.RefreshTokenRequestDto
import com.ngoctientnt.template.core.auth.data.remote.dto.RegisterRequestDto
import com.ngoctientnt.template.core.auth.data.remote.mapper.toDomain
import com.ngoctientnt.template.core.auth.data.remote.mapper.toRequestDto
import com.ngoctientnt.template.core.auth.domain.model.AuthTokens
import com.ngoctientnt.template.core.auth.domain.model.SocialAuthIntent
import com.ngoctientnt.template.core.auth.domain.model.SocialIdentity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitAuthRemoteDataSource @Inject constructor(
    private val authApiService: AuthApiService,
) : AuthRemoteDataSource {

    override suspend fun login(email: String, password: String): AuthTokens {
        return authApiService.login(
            LoginRequestDto(
                email = email,
                password = password,
            ),
        ).toDomain()
    }

    override suspend fun register(
        email: String,
        password: String,
        displayName: String?,
    ): AuthTokens {
        return authApiService.register(
            RegisterRequestDto(
                email = email,
                password = password,
                displayName = displayName?.takeIf { it.isNotBlank() },
            ),
        ).toDomain()
    }

    override suspend fun socialAuth(
        identity: SocialIdentity,
        intent: SocialAuthIntent,
    ): AuthTokens {
        return authApiService.socialAuth(
            identity.toRequestDto(intent),
        ).toDomain()
    }

    override suspend fun refreshToken(refreshToken: String): AuthTokens {
        return authApiService.refreshToken(
            RefreshTokenRequestDto(refreshToken = refreshToken),
        ).toDomain()
    }
}

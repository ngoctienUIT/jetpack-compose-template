package com.ngoctientnt.template.core.auth.data.remote

import com.ngoctientnt.template.core.auth.domain.model.AuthTokens
import com.ngoctientnt.template.core.auth.domain.model.SocialAuthIntent
import com.ngoctientnt.template.core.auth.domain.model.SocialIdentity

interface AuthRemoteDataSource {
    suspend fun login(email: String, password: String): AuthTokens

    suspend fun register(
        email: String,
        password: String,
        displayName: String?,
    ): AuthTokens

    suspend fun socialAuth(
        identity: SocialIdentity,
        intent: SocialAuthIntent,
    ): AuthTokens

    suspend fun refreshToken(refreshToken: String): AuthTokens
}

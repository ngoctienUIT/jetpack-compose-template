package com.ngoctientnt.template.core.auth.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String,
)

@Serializable
data class RegisterRequestDto(
    val email: String,
    val password: String,
    @SerialName("displayName")
    val displayName: String? = null,
)

@Serializable
data class SocialAuthRequestDto(
    val provider: String,
    @SerialName("idToken")
    val idToken: String? = null,
    @SerialName("accessToken")
    val accessToken: String? = null,
    val intent: String,
)

@Serializable
data class RefreshTokenRequestDto(
    @SerialName("refreshToken")
    val refreshToken: String,
)

@Serializable
data class AuthTokenResponseDto(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String,
)

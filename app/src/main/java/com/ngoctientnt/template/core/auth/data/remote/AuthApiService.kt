package com.ngoctientnt.template.core.auth.data.remote

import com.ngoctientnt.template.core.auth.data.remote.dto.AuthTokenResponseDto
import com.ngoctientnt.template.core.auth.data.remote.dto.LoginRequestDto
import com.ngoctientnt.template.core.auth.data.remote.dto.RefreshTokenRequestDto
import com.ngoctientnt.template.core.auth.data.remote.dto.RegisterRequestDto
import com.ngoctientnt.template.core.auth.data.remote.dto.SocialAuthRequestDto
import com.ngoctientnt.template.core.auth.network.annotation.NoAuth
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @NoAuth
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): AuthTokenResponseDto

    @NoAuth
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequestDto): AuthTokenResponseDto

    @NoAuth
    @POST("auth/social")
    suspend fun socialAuth(@Body request: SocialAuthRequestDto): AuthTokenResponseDto

    @NoAuth
    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequestDto): AuthTokenResponseDto
}

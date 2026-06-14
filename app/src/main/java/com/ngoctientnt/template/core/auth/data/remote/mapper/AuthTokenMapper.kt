package com.ngoctientnt.template.core.auth.data.remote.mapper

import com.ngoctientnt.template.core.auth.data.remote.dto.AuthTokenResponseDto
import com.ngoctientnt.template.core.auth.domain.model.AuthTokens

fun AuthTokenResponseDto.toDomain(): AuthTokens = AuthTokens(
    accessToken = accessToken,
    refreshToken = refreshToken,
)

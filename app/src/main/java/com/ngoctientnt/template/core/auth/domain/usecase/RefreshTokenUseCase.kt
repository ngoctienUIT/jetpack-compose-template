package com.ngoctientnt.template.core.auth.domain.usecase

import com.ngoctientnt.template.core.auth.domain.model.AuthTokens
import com.ngoctientnt.template.core.auth.domain.repository.AuthRepository
import com.ngoctientnt.template.core.network.result.ApiResult
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(refreshToken: String): ApiResult<AuthTokens> {
        return authRepository.refreshToken(refreshToken)
    }
}

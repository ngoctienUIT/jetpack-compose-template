package com.ngoctientnt.template.core.auth.domain.usecase

import com.ngoctientnt.template.core.auth.domain.model.AuthTokens
import com.ngoctientnt.template.core.auth.domain.repository.AuthRepository
import com.ngoctientnt.template.core.network.result.ApiResult
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String): ApiResult<AuthTokens> {
        return authRepository.login(email, password)
    }
}

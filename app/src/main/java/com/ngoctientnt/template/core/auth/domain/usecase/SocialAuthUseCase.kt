package com.ngoctientnt.template.core.auth.domain.usecase

import com.ngoctientnt.template.core.auth.domain.model.AuthTokens
import com.ngoctientnt.template.core.auth.domain.model.SocialAuthIntent
import com.ngoctientnt.template.core.auth.domain.model.SocialIdentity
import com.ngoctientnt.template.core.auth.domain.repository.AuthRepository
import com.ngoctientnt.template.core.network.result.ApiResult
import javax.inject.Inject

class SocialAuthUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        identity: SocialIdentity,
        intent: SocialAuthIntent,
    ): ApiResult<AuthTokens> {
        return authRepository.socialAuth(
            identity = identity,
            intent = intent,
        )
    }
}

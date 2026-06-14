package com.ngoctientnt.template.core.auth.domain.usecase

import com.ngoctientnt.template.core.auth.session.SessionManager
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val sessionManager: SessionManager,
) {
    suspend operator fun invoke() {
        sessionManager.logout()
    }
}

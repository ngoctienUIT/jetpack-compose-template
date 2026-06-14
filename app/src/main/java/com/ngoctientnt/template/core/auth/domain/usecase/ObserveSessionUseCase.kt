package com.ngoctientnt.template.core.auth.domain.usecase

import com.ngoctientnt.template.core.auth.domain.model.SessionState
import com.ngoctientnt.template.core.auth.session.SessionManager
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveSessionUseCase @Inject constructor(
    private val sessionManager: SessionManager,
) {
    operator fun invoke(): Flow<SessionState> = sessionManager.observeSession()
}

package com.ngoctientnt.template.core.auth.domain.model

sealed interface SessionState {
    data object Loading : SessionState

    data object Authenticated : SessionState

    data object Unauthenticated : SessionState
}

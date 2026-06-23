package com.ngoctientnt.template.ui.component.auth

import com.ngoctientnt.template.core.auth.domain.model.SocialIdentity

sealed interface SocialAuthLaunchResult {
    data class Success(
        val identity: SocialIdentity,
    ) : SocialAuthLaunchResult

    data object Cancelled : SocialAuthLaunchResult

    data class Error(
        val errorCode: String,
    ) : SocialAuthLaunchResult
}

object SocialAuthErrorCodes {
    const val NOT_CONFIGURED = "social_not_configured"
    const val FAILED = "social_failed"
}

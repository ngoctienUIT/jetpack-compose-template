package com.ngoctientnt.template.core.auth.social

sealed class SocialAuthException(
    message: String,
) : Exception(message) {
    data object Cancelled : SocialAuthException("cancelled")

    data object NotConfigured : SocialAuthException("not_configured")

    class Failed(
        message: String,
    ) : SocialAuthException(message)
}

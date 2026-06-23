package com.ngoctientnt.template.feature.signup

import com.ngoctientnt.template.core.auth.domain.model.SocialIdentity
import com.ngoctientnt.template.core.auth.domain.model.SocialProvider

data class SignUpUiState(
    val displayName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val socialLoadingProvider: SocialProvider? = null,
    val isGoogleEnabled: Boolean = false,
    val isFacebookEnabled: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface SignUpIntent {
    data class DisplayNameChanged(val value: String) : SignUpIntent

    data class EmailChanged(val value: String) : SignUpIntent

    data class PasswordChanged(val value: String) : SignUpIntent

    data class ConfirmPasswordChanged(val value: String) : SignUpIntent

    data object SignUpClicked : SignUpIntent

    data object GoogleSignInClicked : SignUpIntent

    data object FacebookSignInClicked : SignUpIntent

    data class SocialAuthCompleted(val identity: SocialIdentity) : SignUpIntent

    data class SocialAuthFailed(val errorCode: String?) : SignUpIntent

    data object NavigateToLoginClicked : SignUpIntent
}

sealed interface SignUpEffect {
    data object LaunchGoogleSignIn : SignUpEffect

    data object LaunchFacebookSignIn : SignUpEffect

    data object NavigateToLogin : SignUpEffect

    data object NavigateToMain : SignUpEffect
}

object SignUpErrors {
    const val EMPTY_FIELDS = "empty_fields"
    const val PASSWORD_MISMATCH = "password_mismatch"
    const val PASSWORD_TOO_SHORT = "password_too_short"
}

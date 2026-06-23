package com.ngoctientnt.template.feature.login

import com.ngoctientnt.template.core.auth.domain.model.SocialIdentity
import com.ngoctientnt.template.core.auth.domain.model.SocialProvider
import com.ngoctientnt.template.ui.component.toast.AppToastType

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val socialLoadingProvider: SocialProvider? = null,
    val isGoogleEnabled: Boolean = false,
    val isFacebookEnabled: Boolean = false,
)

sealed interface LoginIntent {
    data class EmailChanged(val value: String) : LoginIntent
    data class PasswordChanged(val value: String) : LoginIntent
    data object SignInClicked : LoginIntent
    data object GoogleSignInClicked : LoginIntent
    data object FacebookSignInClicked : LoginIntent
    data class SocialAuthCompleted(val identity: SocialIdentity) : LoginIntent
    data class SocialAuthFailed(val errorCode: String?) : LoginIntent
    data object NavigateToSignUpClicked : LoginIntent
}

sealed interface LoginEffect {
    data object LaunchGoogleSignIn : LoginEffect
    data object LaunchFacebookSignIn : LoginEffect
    data object NavigateToSignUp : LoginEffect
    data object NavigateToMain : LoginEffect

    /** Request the UI to display a toast. [messageKey] maps to a localized string in the screen. */
    data class ShowToast(
        val messageKey: String,
        val type: AppToastType = AppToastType.Error,
    ) : LoginEffect
}

object LoginErrors {
    const val EMPTY_FIELDS = "empty_fields"
    const val SOCIAL_AUTH_FAILED = "social_auth_failed"
}

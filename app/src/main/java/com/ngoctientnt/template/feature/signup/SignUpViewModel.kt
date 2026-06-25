package com.ngoctientnt.template.feature.signup

import androidx.lifecycle.viewModelScope
import com.ngoctientnt.template.core.architecture.BaseViewModel
import com.ngoctientnt.template.core.auth.domain.model.SocialAuthIntent
import com.ngoctientnt.template.core.auth.domain.model.SocialIdentity
import com.ngoctientnt.template.core.auth.domain.model.SocialProvider
import com.ngoctientnt.template.core.auth.domain.usecase.RegisterUseCase
import com.ngoctientnt.template.core.auth.domain.usecase.SocialAuthUseCase
import com.ngoctientnt.template.core.auth.social.SocialAuthGateway
import com.ngoctientnt.template.core.config.AppConfig
import com.ngoctientnt.template.core.network.result.handleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val socialAuthUseCase: SocialAuthUseCase,
    socialAuthGateway: SocialAuthGateway,
) : BaseViewModel<SignUpUiState, SignUpIntent, SignUpEffect>(
    SignUpUiState(
        isGoogleEnabled = socialAuthGateway.isProviderConfigured(SocialProvider.GOOGLE),
        isFacebookEnabled = socialAuthGateway.isProviderConfigured(SocialProvider.FACEBOOK),
    ),
) {

    override fun onIntent(intent: SignUpIntent) {
        when (intent) {
            is SignUpIntent.DisplayNameChanged ->
                reduce { copy(displayName = intent.value, errorMessage = null) }
            is SignUpIntent.EmailChanged ->
                reduce { copy(email = intent.value, errorMessage = null) }
            is SignUpIntent.PasswordChanged ->
                reduce { copy(password = intent.value, errorMessage = null) }
            is SignUpIntent.ConfirmPasswordChanged ->
                reduce { copy(confirmPassword = intent.value, errorMessage = null) }
            SignUpIntent.SignUpClicked -> signUpWithEmail()
            SignUpIntent.GoogleSignInClicked -> launchSocialAuth(SocialProvider.GOOGLE)
            SignUpIntent.FacebookSignInClicked -> launchSocialAuth(SocialProvider.FACEBOOK)
            is SignUpIntent.SocialAuthCompleted -> completeSocialAuth(intent.identity)
            is SignUpIntent.SocialAuthFailed -> handleSocialAuthFailed(intent.errorCode)
            SignUpIntent.NavigateToLoginClicked -> navigateToLogin()
        }
    }

    private fun signUpWithEmail() {
        if (currentState.isLoading || currentState.socialLoadingProvider != null) return

        val displayName = currentState.displayName.trim()
        val email = currentState.email.trim()
        val password = currentState.password
        val confirmPassword = currentState.confirmPassword

        if (displayName.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            reduce { copy(errorMessage = SignUpErrors.EMPTY_FIELDS) }
            return
        }

        if (password.length < AppConfig.MIN_PASSWORD_LENGTH) {
            reduce { copy(errorMessage = SignUpErrors.PASSWORD_TOO_SHORT) }
            return
        }

        if (password != confirmPassword) {
            reduce { copy(errorMessage = SignUpErrors.PASSWORD_MISMATCH) }
            return
        }

        viewModelScope.launch {
            reduce { copy(isLoading = true, errorMessage = null) }
            registerUseCase(
                email = email,
                password = password,
                displayName = displayName,
            ).handleResult(
                onSuccess = {
                    reduce { copy(isLoading = false) }
                    sendEffect(SignUpEffect.NavigateToMain)
                },
                onError = { errorMessage ->
                    reduce { copy(isLoading = false, errorMessage = errorMessage) }
                },
            )
        }
    }

    private fun launchSocialAuth(provider: SocialProvider) {
        if (currentState.isLoading || currentState.socialLoadingProvider != null) return

        val isEnabled = when (provider) {
            SocialProvider.GOOGLE -> currentState.isGoogleEnabled
            SocialProvider.FACEBOOK -> currentState.isFacebookEnabled
        }
        if (!isEnabled) return

        reduce { copy(socialLoadingProvider = provider, errorMessage = null) }
        val effect = when (provider) {
            SocialProvider.GOOGLE -> SignUpEffect.LaunchGoogleSignIn
            SocialProvider.FACEBOOK -> SignUpEffect.LaunchFacebookSignIn
        }
        sendEffect(effect)
    }

    private fun completeSocialAuth(identity: SocialIdentity) {
        viewModelScope.launch {
            socialAuthUseCase(
                identity = identity,
                intent = SocialAuthIntent.SIGN_UP,
            ).handleResult(
                onSuccess = {
                    reduce { copy(socialLoadingProvider = null) }
                    sendEffect(SignUpEffect.NavigateToMain)
                },
                onError = { errorMessage ->
                    reduce {
                        copy(
                            socialLoadingProvider = null,
                            errorMessage = errorMessage,
                        )
                    }
                },
            )
        }
    }

    private fun handleSocialAuthFailed(errorCode: String?) {
        reduce {
            copy(
                socialLoadingProvider = null,
                errorMessage = errorCode,
            )
        }
    }

    private fun navigateToLogin() {
        sendEffect(SignUpEffect.NavigateToLogin)
    }
}

package com.ngoctientnt.template.feature.login

import androidx.lifecycle.viewModelScope
import com.ngoctientnt.template.core.architecture.BaseViewModel
import com.ngoctientnt.template.core.auth.domain.model.SocialAuthIntent
import com.ngoctientnt.template.core.auth.domain.model.SocialIdentity
import com.ngoctientnt.template.core.auth.domain.model.SocialProvider
import com.ngoctientnt.template.core.auth.domain.usecase.LoginUseCase
import com.ngoctientnt.template.core.auth.domain.usecase.SocialAuthUseCase
import com.ngoctientnt.template.core.auth.social.SocialAuthGateway
import com.ngoctientnt.template.core.network.result.handleResult
import com.ngoctientnt.template.ui.component.toast.AppToastType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val socialAuthUseCase: SocialAuthUseCase,
    socialAuthGateway: SocialAuthGateway,
) : BaseViewModel<LoginUiState, LoginIntent, LoginEffect>(
    LoginUiState(
        isGoogleEnabled = socialAuthGateway.isProviderConfigured(SocialProvider.GOOGLE),
        isFacebookEnabled = socialAuthGateway.isProviderConfigured(SocialProvider.FACEBOOK),
    ),
) {

    override fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.EmailChanged -> reduce { copy(email = intent.value) }
            is LoginIntent.PasswordChanged -> reduce { copy(password = intent.value) }
            LoginIntent.SignInClicked -> signIn()
            LoginIntent.GoogleSignInClicked -> launchSocialAuth(SocialProvider.GOOGLE)
            LoginIntent.FacebookSignInClicked -> launchSocialAuth(SocialProvider.FACEBOOK)
            is LoginIntent.SocialAuthCompleted -> completeSocialAuth(intent.identity)
            is LoginIntent.SocialAuthFailed -> handleSocialAuthFailed(intent.errorCode)
            LoginIntent.NavigateToSignUpClicked -> navigateToSignUp()
        }
    }

    private fun signIn() {
        if (currentState.isLoading || currentState.socialLoadingProvider != null) return

        val email = currentState.email.trim()
        val password = currentState.password

        if (email.isBlank() || password.isBlank()) {
            sendToast(LoginErrors.EMPTY_FIELDS)
            return
        }

        viewModelScope.launch {
            reduce { copy(isLoading = true) }
            loginUseCase(email, password).handleResult(
                onSuccess = {
                    reduce { copy(isLoading = false) }
                    sendEffect(LoginEffect.NavigateToMain)
                },
                onError = { errorKey ->
                    reduce { copy(isLoading = false) }
                    sendToast(errorKey)
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

        reduce { copy(socialLoadingProvider = provider) }
        val launchEffect = when (provider) {
            SocialProvider.GOOGLE -> LoginEffect.LaunchGoogleSignIn
            SocialProvider.FACEBOOK -> LoginEffect.LaunchFacebookSignIn
        }
        sendEffect(launchEffect)
    }

    private fun completeSocialAuth(identity: SocialIdentity) {
        viewModelScope.launch {
            socialAuthUseCase(
                identity = identity,
                intent = SocialAuthIntent.LOGIN,
            ).handleResult(
                onSuccess = {
                    reduce { copy(socialLoadingProvider = null) }
                    sendEffect(LoginEffect.NavigateToMain)
                },
                onError = { errorKey ->
                    reduce { copy(socialLoadingProvider = null) }
                    sendToast(errorKey)
                },
            )
        }
    }

    private fun handleSocialAuthFailed(errorCode: String?) {
        reduce { copy(socialLoadingProvider = null) }
        sendToast(errorCode ?: LoginErrors.SOCIAL_AUTH_FAILED)
    }

    private fun navigateToSignUp() {
        sendEffect(LoginEffect.NavigateToSignUp)
    }

    private fun sendToast(messageKey: String, type: AppToastType = AppToastType.Error) {
        sendEffect(LoginEffect.ShowToast(messageKey = messageKey, type = type))
    }
}

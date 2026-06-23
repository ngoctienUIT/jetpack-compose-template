package com.ngoctientnt.template.feature.login

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ngoctientnt.template.R
import com.ngoctientnt.template.app.navigation.LocalAppNavigator
import com.ngoctientnt.template.app.navigation.MainRoute
import com.ngoctientnt.template.app.navigation.SignUpRoute
import com.ngoctientnt.template.core.auth.domain.model.SocialIdentity
import com.ngoctientnt.template.core.auth.domain.model.SocialProvider
import com.ngoctientnt.template.di.SocialAuthLauncherEntryPoint
import com.ngoctientnt.template.ui.component.auth.SocialAuthErrorCodes
import com.ngoctientnt.template.ui.component.auth.SocialAuthLaunchResult
import com.ngoctientnt.template.ui.component.auth.SocialSignInSection
import com.ngoctientnt.template.core.network.result.ApiUiErrors
import com.ngoctientnt.template.ui.component.button.AppFilledButton
import com.ngoctientnt.template.ui.component.button.AppTextButton
import com.ngoctientnt.template.ui.component.input.AppTextField
import com.ngoctientnt.template.ui.component.auth.SocialAuthLauncher
import com.ngoctientnt.template.ui.component.toast.AppToastType
import com.ngoctientnt.template.ui.component.toast.LocalAppToastController
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val navigator = LocalAppNavigator.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    val coroutineScope = rememberCoroutineScope()
    val socialAuthLauncher = EntryPointAccessors.fromApplication(
        context.applicationContext,
        SocialAuthLauncherEntryPoint::class.java,
    ).socialAuthLauncher()
    val toast = LocalAppToastController.current
    val resolveLoginError = rememberLoginErrorMessageResolver()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                LoginEffect.NavigateToMain -> navigator.replaceAll(MainRoute())
                LoginEffect.NavigateToSignUp -> navigator.navigate(SignUpRoute)

                is LoginEffect.ShowToast -> {
                    val message = resolveLoginError(effect.messageKey)
                    when (effect.type) {
                        AppToastType.Success -> toast.showSuccess(message)
                        AppToastType.Error -> toast.showError(message)
                        AppToastType.Info -> toast.showInfo(message)
                        AppToastType.Warning -> toast.showWarning(message)
                        AppToastType.Default -> toast.show(message)
                    }
                }

                LoginEffect.LaunchGoogleSignIn -> {
                    if (activity == null) {
                        toast.showError(resolveLoginError(LoginErrors.SOCIAL_AUTH_FAILED))
                        return@collect
                    }
                    coroutineScope.launch {
                        launchSocialSignIn(
                            provider = SocialProvider.GOOGLE,
                            activity = activity,
                            launcher = socialAuthLauncher,
                            onCompleted = { viewModel.onIntent(LoginIntent.SocialAuthCompleted(it)) },
                            onFailed = { viewModel.onIntent(LoginIntent.SocialAuthFailed(it)) },
                        )
                    }
                }

                LoginEffect.LaunchFacebookSignIn -> {
                    if (activity == null) {
                        toast.showError(resolveLoginError(LoginErrors.SOCIAL_AUTH_FAILED))
                        return@collect
                    }
                    coroutineScope.launch {
                        launchSocialSignIn(
                            provider = SocialProvider.FACEBOOK,
                            activity = activity,
                            launcher = socialAuthLauncher,
                            onCompleted = { viewModel.onIntent(LoginIntent.SocialAuthCompleted(it)) },
                            onFailed = { viewModel.onIntent(LoginIntent.SocialAuthFailed(it)) },
                        )
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.login_title),
            style = MaterialTheme.typography.headlineMedium,
        )

        AppTextField(
            value = uiState.email,
            onValueChange = { viewModel.onIntent(LoginIntent.EmailChanged(it)) },
            label = stringResource(R.string.login_email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
        )

        AppTextField(
            value = uiState.password,
            onValueChange = { viewModel.onIntent(LoginIntent.PasswordChanged(it)) },
            label = stringResource(R.string.login_password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        )

        AppFilledButton(
            text = stringResource(R.string.login_sign_in),
            onClick = { viewModel.onIntent(LoginIntent.SignInClicked) },
            enabled = !uiState.isLoading && uiState.socialLoadingProvider == null,
            loading = uiState.isLoading,
            fullWidth = true,
            modifier = Modifier.padding(top = 24.dp),
        )

        AppTextButton(
            text = stringResource(R.string.login_forgot_password),
            onClick = { /* TODO: navigate to forgot-password */ },
            modifier = Modifier.padding(top = 8.dp),
        )

        SocialSignInSection(
            isGoogleEnabled = uiState.isGoogleEnabled,
            isFacebookEnabled = uiState.isFacebookEnabled,
            loadingProvider = uiState.socialLoadingProvider,
            onGoogleClick = { viewModel.onIntent(LoginIntent.GoogleSignInClicked) },
            onFacebookClick = { viewModel.onIntent(LoginIntent.FacebookSignInClicked) },
            modifier = Modifier.padding(top = 24.dp),
        )

        AppTextButton(
            text = stringResource(R.string.login_no_account),
            onClick = { viewModel.onIntent(LoginIntent.NavigateToSignUpClicked) },
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

// region — helpers

private suspend fun launchSocialSignIn(
    provider: SocialProvider,
    activity: ComponentActivity,
    launcher: SocialAuthLauncher,
    onCompleted: (SocialIdentity) -> Unit,
    onFailed: (String?) -> Unit,
) {
    when (val result = launcher.authenticate(provider = provider, activity = activity)) {
        is SocialAuthLaunchResult.Success -> onCompleted(result.identity)
        is SocialAuthLaunchResult.Cancelled -> onFailed(null)
        is SocialAuthLaunchResult.Error -> onFailed(result.errorCode)
    }
}

@Composable
private fun rememberLoginErrorMessageResolver(): (String) -> String {
    val emptyFieldsMessage = stringResource(R.string.login_error_empty_fields)
    val socialAuthFailedMessage = stringResource(R.string.login_error_social_auth_failed)
    val networkErrorMessage = stringResource(R.string.login_error_network)
    val unknownErrorMessage = stringResource(R.string.login_error_unknown)
    val socialNotConfiguredMessage = stringResource(R.string.auth_error_social_not_configured)
    val socialFailedMessage = stringResource(R.string.auth_error_social_failed)

    return { key ->
        when (key) {
            LoginErrors.EMPTY_FIELDS -> emptyFieldsMessage
            LoginErrors.SOCIAL_AUTH_FAILED -> socialAuthFailedMessage
            SocialAuthErrorCodes.NOT_CONFIGURED -> socialNotConfiguredMessage
            SocialAuthErrorCodes.FAILED -> socialFailedMessage
            ApiUiErrors.NETWORK -> networkErrorMessage
            ApiUiErrors.UNKNOWN -> unknownErrorMessage
            else -> key
        }
    }
}

// endregion

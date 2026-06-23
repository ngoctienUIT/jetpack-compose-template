package com.ngoctientnt.template.feature.signup

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
import com.ngoctientnt.template.app.navigation.LoginRoute
import com.ngoctientnt.template.app.navigation.MainRoute
import com.ngoctientnt.template.core.auth.domain.model.SocialIdentity
import com.ngoctientnt.template.core.auth.domain.model.SocialProvider
import com.ngoctientnt.template.core.config.AppConfig
import com.ngoctientnt.template.di.SocialAuthLauncherEntryPoint
import com.ngoctientnt.template.ui.component.auth.SocialAuthErrorCodes
import com.ngoctientnt.template.ui.component.auth.SocialAuthLaunchResult
import com.ngoctientnt.template.ui.component.auth.SocialSignInSection
import com.ngoctientnt.template.ui.component.auth.resolveSocialAuthErrorMessage
import com.ngoctientnt.template.ui.component.button.AppFilledButton
import com.ngoctientnt.template.ui.component.button.AppTextButton
import com.ngoctientnt.template.ui.component.input.AppTextField
import com.ngoctientnt.template.ui.component.network.resolveApiErrorMessage
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
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

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                SignUpEffect.NavigateToMain -> navigator.replaceAll(MainRoute())
                SignUpEffect.NavigateToLogin -> navigator.replace(LoginRoute)
                SignUpEffect.LaunchGoogleSignIn -> {
                    if (activity == null) return@collect
                    coroutineScope.launch {
                        handleSocialAuthResult(
                            result = socialAuthLauncher.authenticate(
                                provider = SocialProvider.GOOGLE,
                                activity = activity,
                            ),
                            onCompleted = { viewModel.onIntent(SignUpIntent.SocialAuthCompleted(it)) },
                            onFailed = { viewModel.onIntent(SignUpIntent.SocialAuthFailed(it)) },
                        )
                    }
                }
                SignUpEffect.LaunchFacebookSignIn -> {
                    if (activity == null) return@collect
                    coroutineScope.launch {
                        handleSocialAuthResult(
                            result = socialAuthLauncher.authenticate(
                                provider = SocialProvider.FACEBOOK,
                                activity = activity,
                            ),
                            onCompleted = { viewModel.onIntent(SignUpIntent.SocialAuthCompleted(it)) },
                            onFailed = { viewModel.onIntent(SignUpIntent.SocialAuthFailed(it)) },
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
            text = stringResource(R.string.signup_title),
            style = MaterialTheme.typography.headlineMedium,
        )

        AppTextField(
            value = uiState.displayName,
            onValueChange = { viewModel.onIntent(SignUpIntent.DisplayNameChanged(it)) },
            label = stringResource(R.string.signup_display_name),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
        )

        AppTextField(
            value = uiState.email,
            onValueChange = { viewModel.onIntent(SignUpIntent.EmailChanged(it)) },
            label = stringResource(R.string.login_email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        )

        AppTextField(
            value = uiState.password,
            onValueChange = { viewModel.onIntent(SignUpIntent.PasswordChanged(it)) },
            label = stringResource(R.string.login_password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        )

        AppTextField(
            value = uiState.confirmPassword,
            onValueChange = { viewModel.onIntent(SignUpIntent.ConfirmPasswordChanged(it)) },
            label = stringResource(R.string.signup_confirm_password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        )

        uiState.errorMessage?.let { errorMessage ->
            Text(
                text = resolveSignUpErrorMessage(errorMessage),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        AppFilledButton(
            text = stringResource(R.string.signup_create_account),
            onClick = { viewModel.onIntent(SignUpIntent.SignUpClicked) },
            enabled = !uiState.isLoading && uiState.socialLoadingProvider == null,
            loading = uiState.isLoading,
            fullWidth = true,
            modifier = Modifier.padding(top = 24.dp),
        )

        SocialSignInSection(
            isGoogleEnabled = uiState.isGoogleEnabled,
            isFacebookEnabled = uiState.isFacebookEnabled,
            loadingProvider = uiState.socialLoadingProvider,
            onGoogleClick = { viewModel.onIntent(SignUpIntent.GoogleSignInClicked) },
            onFacebookClick = { viewModel.onIntent(SignUpIntent.FacebookSignInClicked) },
            modifier = Modifier.padding(top = 24.dp),
        )

        AppTextButton(
            text = stringResource(R.string.signup_already_have_account),
            onClick = { viewModel.onIntent(SignUpIntent.NavigateToLoginClicked) },
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

private fun handleSocialAuthResult(
    result: SocialAuthLaunchResult,
    onCompleted: (SocialIdentity) -> Unit,
    onFailed: (String?) -> Unit,
) {
    when (result) {
        is SocialAuthLaunchResult.Success -> onCompleted(result.identity)
        is SocialAuthLaunchResult.Cancelled -> onFailed(null)
        is SocialAuthLaunchResult.Error -> onFailed(result.errorCode)
    }
}

@Composable
private fun resolveSignUpErrorMessage(errorMessage: String): String {
    return when (errorMessage) {
        SignUpErrors.EMPTY_FIELDS -> stringResource(R.string.signup_error_empty_fields)
        SignUpErrors.PASSWORD_MISMATCH -> stringResource(R.string.signup_error_password_mismatch)
        SignUpErrors.PASSWORD_TOO_SHORT ->
            stringResource(R.string.signup_error_password_too_short, AppConfig.MIN_PASSWORD_LENGTH)
        SocialAuthErrorCodes.NOT_CONFIGURED,
        SocialAuthErrorCodes.FAILED,
        -> resolveSocialAuthErrorMessage(errorMessage)
        else -> resolveApiErrorMessage(errorMessage)
    }
}

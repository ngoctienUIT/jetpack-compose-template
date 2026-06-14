package com.ngoctientnt.template.feature.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ngoctientnt.template.R
import com.ngoctientnt.template.app.navigation.LocalAppNavigator
import com.ngoctientnt.template.app.navigation.MainRoute
import com.ngoctientnt.template.ui.component.button.AppFilledButton
import com.ngoctientnt.template.ui.component.button.AppTextButton
import com.ngoctientnt.template.ui.component.input.AppTextField
import com.ngoctientnt.template.ui.component.network.resolveApiErrorMessage

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val navigator = LocalAppNavigator.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                LoginEffect.NavigateToMain -> navigator.replaceAll(MainRoute())
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
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

        uiState.errorMessage?.let { errorMessage ->
            Text(
                text = resolveLoginErrorMessage(errorMessage),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        AppFilledButton(
            text = stringResource(R.string.login_sign_in),
            onClick = { viewModel.onIntent(LoginIntent.SignInClicked) },
            enabled = !uiState.isLoading,
            loading = uiState.isLoading,
            fullWidth = true,
            modifier = Modifier.padding(top = 24.dp),
        )

        AppTextButton(
            text = stringResource(R.string.login_forgot_password),
            onClick = { },
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

@Composable
private fun resolveLoginErrorMessage(errorMessage: String): String {
    return when (errorMessage) {
        LoginErrors.EMPTY_FIELDS -> stringResource(R.string.login_error_empty_fields)
        else -> resolveApiErrorMessage(errorMessage)
    }
}

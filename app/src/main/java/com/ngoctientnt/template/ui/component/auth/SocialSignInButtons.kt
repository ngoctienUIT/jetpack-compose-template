package com.ngoctientnt.template.ui.component.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ngoctientnt.template.R
import com.ngoctientnt.template.core.auth.domain.model.SocialProvider
import com.ngoctientnt.template.ui.component.button.AppOutlinedButton

@Composable
fun SocialSignInSection(
    isGoogleEnabled: Boolean,
    isFacebookEnabled: Boolean,
    loadingProvider: SocialProvider?,
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SocialAuthDivider()

        AppOutlinedButton(
            text = stringResource(R.string.auth_continue_with_google),
            onClick = onGoogleClick,
            enabled = isGoogleEnabled,
            loading = loadingProvider == SocialProvider.GOOGLE,
            fullWidth = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Login,
                    contentDescription = null,
                )
            },
        )

        AppOutlinedButton(
            text = stringResource(R.string.auth_continue_with_facebook),
            onClick = onFacebookClick,
            enabled = isFacebookEnabled,
            loading = loadingProvider == SocialProvider.FACEBOOK,
            fullWidth = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Login,
                    contentDescription = null,
                )
            },
        )
    }
}

@Composable
private fun SocialAuthDivider(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider(
            modifier = Modifier.padding(bottom = 16.dp),
        )
        Text(
            text = stringResource(R.string.auth_or_continue_with),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp),
        )
    }
}

@Composable
fun resolveSocialAuthErrorMessage(errorCode: String): String {
    return when (errorCode) {
        SocialAuthErrorCodes.NOT_CONFIGURED -> stringResource(R.string.auth_error_social_not_configured)
        SocialAuthErrorCodes.FAILED -> stringResource(R.string.auth_error_social_failed)
        else -> stringResource(R.string.login_error_unknown)
    }
}

package com.ngoctientnt.template.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ngoctientnt.template.R
import com.ngoctientnt.template.core.network.NetworkStatus
import com.ngoctientnt.template.ui.component.button.AppTextButton
import com.ngoctientnt.template.ui.component.dialog.AppAlertDialog
import com.ngoctientnt.template.ui.component.theme.LocalAppComponentTheme

@Composable
fun NoInternetDialog(
    networkStatus: NetworkStatus,
    onRetry: () -> Unit,
) {
    val theme = LocalAppComponentTheme.current

    AppAlertDialog(
        visible = networkStatus == NetworkStatus.Unavailable,
        onDismissRequest = {},
        dismissOnBackPress = false,
        dismissOnClickOutside = false,
        title = {
            Text(
                text = stringResource(R.string.no_internet_title),
                style = theme.dialogTitleStyle,
            )
        },
        text = {
            Text(
                text = stringResource(R.string.no_internet_message),
                style = theme.dialogMessageStyle,
            )
        },
        confirmButton = {
            AppTextButton(
                text = stringResource(R.string.no_internet_retry),
                onClick = onRetry,
            )
        },
    )
}

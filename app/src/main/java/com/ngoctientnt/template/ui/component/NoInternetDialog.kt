package com.ngoctientnt.template.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ngoctientnt.template.R
import com.ngoctientnt.template.core.network.NetworkStatus
import com.ngoctientnt.template.ui.component.button.AppTextButton

@Composable
fun NoInternetDialog(
    networkStatus: NetworkStatus,
    onRetry: () -> Unit,
) {
    if (networkStatus != NetworkStatus.Unavailable) return

    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = stringResource(R.string.no_internet_title))
        },
        text = {
            Text(text = stringResource(R.string.no_internet_message))
        },
        confirmButton = {
            AppTextButton(
                text = stringResource(R.string.no_internet_retry),
                onClick = onRetry,
            )
        },
    )
}

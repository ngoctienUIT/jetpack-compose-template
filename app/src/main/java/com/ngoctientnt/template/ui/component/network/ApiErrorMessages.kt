package com.ngoctientnt.template.ui.component.network

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ngoctientnt.template.R
import com.ngoctientnt.template.core.network.result.ApiUiErrors

@Composable
fun resolveApiErrorMessage(errorMessage: String): String {
    return when (errorMessage) {
        ApiUiErrors.NETWORK -> stringResource(R.string.login_error_network)
        ApiUiErrors.UNKNOWN -> stringResource(R.string.login_error_unknown)
        else -> errorMessage
    }
}

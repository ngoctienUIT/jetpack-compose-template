package com.ngoctientnt.template.ui.component.dialog

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import com.ngoctientnt.template.core.theme.AppThemeMode
import com.ngoctientnt.template.ui.component.button.AppFilledButton
import com.ngoctientnt.template.ui.component.button.AppTextButton
import com.ngoctientnt.template.ui.component.theme.LocalAppComponentTheme
import com.ngoctientnt.template.ui.theme.TemplateTheme

/**
 * A pre-built confirmation dialog with optional destructive styling and
 * loading state.
 *
 * When [loading] is true, back-press and outside-click are automatically
 * blocked to prevent accidental dismissal mid-operation.
 *
 * ```kotlin
 * val dialogState = rememberAppDialogState()
 *
 * AppConfirmDialog(
 *     state = dialogState,
 *     title = "Delete account?",
 *     message = "This action cannot be undone.",
 *     confirmText = "Delete",
 *     dismissText = "Cancel",
 *     destructive = true,
 *     onConfirm = { viewModel.onDeleteConfirmed() },
 *     onDismiss = { dialogState.hide() },
 * )
 * ```
 */
@Composable
fun AppConfirmDialog(
    visible: Boolean,
    title: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    message: String? = null,
    dismissText: String? = null,
    destructive: Boolean = false,
    loading: Boolean = false,
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    shape: Shape = LocalAppComponentTheme.current.dialogShape,
) {
    val theme = LocalAppComponentTheme.current
    val confirmColors = if (destructive) {
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
        )
    } else {
        theme.filledButtonColors
    }

    AppAlertDialog(
        visible = visible,
        onDismissRequest = onDismiss,
        modifier = modifier,
        // Block dismiss while loading to prevent accidental cancellation.
        dismissOnBackPress = dismissOnBackPress && !loading,
        dismissOnClickOutside = dismissOnClickOutside && !loading,
        shape = shape,
        title = {
            Text(
                text = title,
                style = theme.dialogTitleStyle,
            )
        },
        text = message?.let { messageText ->
            {
                Text(
                    text = messageText,
                    style = theme.dialogMessageStyle,
                )
            }
        },
        confirmButton = {
            AppFilledButton(
                text = confirmText,
                onClick = onConfirm,
                loading = loading,
                colors = confirmColors,
            )
        },
        dismissButton = dismissText?.let { text ->
            {
                AppTextButton(
                    text = text,
                    onClick = onDismiss,
                    enabled = !loading,
                )
            }
        },
    )
}

/** State-based overload — visibility is controlled via [AppDialogState]. */
@Composable
fun AppConfirmDialog(
    state: AppDialogState,
    title: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit = { state.hide() },
    modifier: Modifier = Modifier,
    message: String? = null,
    dismissText: String? = null,
    destructive: Boolean = false,
    loading: Boolean = false,
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    shape: Shape = LocalAppComponentTheme.current.dialogShape,
) {
    AppConfirmDialog(
        visible = state.isVisible,
        title = title,
        confirmText = confirmText,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        modifier = modifier,
        message = message,
        dismissText = dismissText,
        destructive = destructive,
        loading = loading,
        dismissOnBackPress = dismissOnBackPress,
        dismissOnClickOutside = dismissOnClickOutside,
        shape = shape,
    )
}

@Preview(name = "Confirm Dialog Light")
@Composable
private fun AppConfirmDialogPreviewLight() {
    TemplateTheme {
        AppConfirmDialog(
            visible = true,
            title = "Sign out?",
            message = "You will need to sign in again to access your account.",
            confirmText = "Sign out",
            dismissText = "Cancel",
            onConfirm = {},
            onDismiss = {},
            destructive = true,
        )
    }
}

@Preview(name = "Confirm Dialog Dark")
@Composable
private fun AppConfirmDialogPreviewDark() {
    TemplateTheme(themeMode = AppThemeMode.DARK) {
        AppConfirmDialog(
            visible = true,
            title = "Sign out?",
            message = "You will need to sign in again to access your account.",
            confirmText = "Sign out",
            dismissText = "Cancel",
            onConfirm = {},
            onDismiss = {},
            destructive = true,
        )
    }
}

@Preview(name = "Confirm Dialog Loading")
@Composable
private fun AppConfirmDialogLoadingPreview() {
    TemplateTheme {
        AppConfirmDialog(
            visible = true,
            title = "Sign out?",
            message = "Signing out…",
            confirmText = "Sign out",
            dismissText = "Cancel",
            onConfirm = {},
            onDismiss = {},
            loading = true,
            destructive = true,
        )
    }
}

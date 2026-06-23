package com.ngoctientnt.template.ui.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.window.DialogProperties
import com.ngoctientnt.template.ui.component.theme.LocalAppComponentTheme

@Composable
fun AppAlertDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    icon: (@Composable () -> Unit)? = null,
    title: (@Composable () -> Unit)? = null,
    text: (@Composable () -> Unit)? = null,
    confirmButton: (@Composable () -> Unit)? = null,
    dismissButton: (@Composable () -> Unit)? = null,
    shape: Shape = LocalAppComponentTheme.current.dialogShape,
    containerColor: androidx.compose.ui.graphics.Color = AlertDialogDefaults.containerColor,
    iconContentColor: androidx.compose.ui.graphics.Color = AlertDialogDefaults.iconContentColor,
    titleContentColor: androidx.compose.ui.graphics.Color = AlertDialogDefaults.titleContentColor,
    textContentColor: androidx.compose.ui.graphics.Color = AlertDialogDefaults.textContentColor,
) {
    if (!visible) return

    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside,
        ),
        icon = icon,
        title = title,
        text = text,
        confirmButton = confirmButton ?: {},
        dismissButton = dismissButton,
        shape = shape,
        containerColor = containerColor,
        iconContentColor = iconContentColor,
        titleContentColor = titleContentColor,
        textContentColor = textContentColor,
    )
}

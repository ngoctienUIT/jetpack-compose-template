package com.ngoctientnt.template.ui.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ngoctientnt.template.ui.component.theme.LocalAppComponentTheme

enum class AppButtonSize(
    val minHeight: Dp,
    val contentPadding: PaddingValues,
) {
    Small(
        minHeight = 36.dp,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    ),
    Medium(
        minHeight = 48.dp,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
    ),
    Large(
        minHeight = 56.dp,
        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp),
    ),
}

@Composable
fun AppFilledButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    fullWidth: Boolean = false,
    size: AppButtonSize? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    colors: ButtonColors = LocalAppComponentTheme.current.filledButtonColors,
    shape: Shape = LocalAppComponentTheme.current.buttonShape,
    elevation: ButtonElevation = LocalAppComponentTheme.current.filledButtonElevation,
    contentPadding: PaddingValues? = null,
    textStyle: TextStyle = LocalAppComponentTheme.current.buttonTextStyle,
) {
    val theme = LocalAppComponentTheme.current
    val resolvedPadding = contentPadding ?: size?.contentPadding ?: theme.buttonContentPadding
    val resolvedMinHeight = size?.minHeight ?: theme.buttonMinHeight

    Button(
        onClick = onClick,
        modifier = modifier
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
            .defaultMinSize(minHeight = resolvedMinHeight),
        enabled = enabled && !loading,
        shape = shape,
        colors = colors,
        elevation = elevation,
        contentPadding = resolvedPadding,
    ) {
        AppButtonContent(
            text = text,
            loading = loading,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            textStyle = textStyle,
        )
    }
}

@Composable
fun AppElevatedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    fullWidth: Boolean = false,
    size: AppButtonSize? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    colors: ButtonColors = LocalAppComponentTheme.current.elevatedButtonColors,
    shape: Shape = LocalAppComponentTheme.current.buttonShape,
    elevation: ButtonElevation = LocalAppComponentTheme.current.elevatedButtonElevation,
    contentPadding: PaddingValues? = null,
    textStyle: TextStyle = LocalAppComponentTheme.current.buttonTextStyle,
) {
    val theme = LocalAppComponentTheme.current
    val resolvedPadding = contentPadding ?: size?.contentPadding ?: theme.buttonContentPadding
    val resolvedMinHeight = size?.minHeight ?: theme.buttonMinHeight

    ElevatedButton(
        onClick = onClick,
        modifier = modifier
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
            .defaultMinSize(minHeight = resolvedMinHeight),
        enabled = enabled && !loading,
        shape = shape,
        colors = colors,
        elevation = elevation,
        contentPadding = resolvedPadding,
    ) {
        AppButtonContent(
            text = text,
            loading = loading,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            textStyle = textStyle,
        )
    }
}

@Composable
fun AppOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    fullWidth: Boolean = false,
    size: AppButtonSize? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    colors: ButtonColors = LocalAppComponentTheme.current.outlinedButtonColors,
    shape: Shape = LocalAppComponentTheme.current.buttonShape,
    borderWidth: Dp = LocalAppComponentTheme.current.outlinedButtonBorderWidth,
    contentPadding: PaddingValues? = null,
    textStyle: TextStyle = LocalAppComponentTheme.current.buttonTextStyle,
) {
    val theme = LocalAppComponentTheme.current
    val resolvedPadding = contentPadding ?: size?.contentPadding ?: theme.buttonContentPadding
    val resolvedMinHeight = size?.minHeight ?: theme.buttonMinHeight

    val isInteractive = enabled && !loading

    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
            .defaultMinSize(minHeight = resolvedMinHeight),
        enabled = isInteractive,
        shape = shape,
        colors = colors,
        border = BorderStroke(
            width = borderWidth,
            color = if (isInteractive) {
                MaterialTheme.colorScheme.outline
            } else {
                MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
            },
        ),
        contentPadding = resolvedPadding,
    ) {
        AppButtonContent(
            text = text,
            loading = loading,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            textStyle = textStyle,
        )
    }
}

@Composable
fun AppTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    fullWidth: Boolean = false,
    size: AppButtonSize? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    colors: ButtonColors = LocalAppComponentTheme.current.textButtonColors,
    shape: Shape = LocalAppComponentTheme.current.buttonShape,
    contentPadding: PaddingValues? = null,
    textStyle: TextStyle = LocalAppComponentTheme.current.buttonTextStyle,
) {
    val theme = LocalAppComponentTheme.current
    val resolvedPadding = contentPadding ?: size?.contentPadding ?: theme.buttonContentPadding
    val resolvedMinHeight = size?.minHeight ?: theme.buttonMinHeight

    TextButton(
        onClick = onClick,
        modifier = modifier
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier)
            .defaultMinSize(minHeight = resolvedMinHeight),
        enabled = enabled && !loading,
        shape = shape,
        colors = colors,
        contentPadding = resolvedPadding,
    ) {
        AppButtonContent(
            text = text,
            loading = loading,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            textStyle = textStyle,
        )
    }
}

@Composable
private fun AppButtonContent(
    text: String,
    loading: Boolean,
    leadingIcon: (@Composable () -> Unit)?,
    trailingIcon: (@Composable () -> Unit)?,
    textStyle: TextStyle,
) {
    val contentColor = LocalContentColor.current

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(ButtonDefaults.IconSize),
                color = contentColor,
                strokeWidth = 2.dp,
            )
        } else {
            leadingIcon?.invoke()
        }

        if (text.isNotEmpty()) {
            Text(
                text = text,
                style = textStyle,
                modifier = Modifier.then(
                    when {
                        loading -> Modifier
                        leadingIcon != null -> Modifier.padding(start = ButtonDefaults.IconSpacing)
                        trailingIcon != null -> Modifier.padding(end = ButtonDefaults.IconSpacing)
                        else -> Modifier
                    },
                ),
            )
        }

        if (!loading) {
            trailingIcon?.invoke()
        }
    }
}

package com.ngoctientnt.template.ui.component.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object AppComponentDefaults {

    @Composable
    fun theme(
        buttonShape: Shape = MaterialTheme.shapes.medium,
        buttonMinHeight: Dp = 48.dp,
        buttonContentPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        buttonTextStyle: TextStyle = MaterialTheme.typography.labelLarge,
        filledButtonColors: ButtonColors = ButtonDefaults.buttonColors(),
        filledButtonElevation: ButtonElevation = ButtonDefaults.buttonElevation(),
        elevatedButtonColors: ButtonColors = ButtonDefaults.elevatedButtonColors(),
        elevatedButtonElevation: ButtonElevation = ButtonDefaults.elevatedButtonElevation(),
        outlinedButtonColors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
        outlinedButtonBorderWidth: Dp = 1.dp,
        textButtonColors: ButtonColors = ButtonDefaults.textButtonColors(),
        textFieldShape: Shape = MaterialTheme.shapes.small,
        textFieldTextStyle: TextStyle = MaterialTheme.typography.bodyLarge,
        textFieldColors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
        imageShape: Shape = MaterialTheme.shapes.small,
        imageContentScale: ContentScale = ContentScale.Crop,
        imageCrossfadeEnabled: Boolean = true,
        imagePlaceholderColor: Color = MaterialTheme.colorScheme.surfaceVariant,
        imageErrorColor: Color = MaterialTheme.colorScheme.errorContainer,
        imagePlaceholderIconSize: Dp = 24.dp,
        avatarSize: Dp = 48.dp,
        dialogShape: Shape = MaterialTheme.shapes.extraLarge,
        sheetShape: Shape = RoundedCornerShape(
            topStart = 28.dp,
            topEnd = 28.dp,
            bottomStart = 0.dp,
            bottomEnd = 0.dp,
        ),
        overlayContentPadding: Dp = 24.dp,
        sheetDragHandleWidth: Dp = 32.dp,
        sheetDragHandleHeight: Dp = 4.dp,
        sheetDragHandleColor: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
        dialogTitleStyle: TextStyle = MaterialTheme.typography.titleLarge,
        dialogMessageStyle: TextStyle = MaterialTheme.typography.bodyMedium,
        sheetTitleStyle: TextStyle = MaterialTheme.typography.titleLarge,
        toastShape: Shape = MaterialTheme.shapes.medium,
        toastContentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        toastIconSpacing: Dp = 12.dp,
        toastTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
        toastIconSize: Dp = 24.dp,
        toastElevation: Dp = 6.dp,
        toastHostPadding: Dp = 24.dp,
        toastSuccessContainerColor: Color = MaterialTheme.colorScheme.primaryContainer,
        toastSuccessContentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
        toastErrorContainerColor: Color = MaterialTheme.colorScheme.errorContainer,
        toastErrorContentColor: Color = MaterialTheme.colorScheme.onErrorContainer,
        toastInfoContainerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
        toastInfoContentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
        toastWarningContainerColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
        toastWarningContentColor: Color = MaterialTheme.colorScheme.onTertiaryContainer,
        toastDefaultContainerColor: Color = MaterialTheme.colorScheme.inverseSurface,
        toastDefaultContentColor: Color = MaterialTheme.colorScheme.inverseOnSurface,
    ): AppComponentTheme = AppComponentTheme(
        buttonShape = buttonShape,
        buttonMinHeight = buttonMinHeight,
        buttonContentPadding = buttonContentPadding,
        buttonTextStyle = buttonTextStyle,
        filledButtonColors = filledButtonColors,
        filledButtonElevation = filledButtonElevation,
        elevatedButtonColors = elevatedButtonColors,
        elevatedButtonElevation = elevatedButtonElevation,
        outlinedButtonColors = outlinedButtonColors,
        outlinedButtonBorderWidth = outlinedButtonBorderWidth,
        textButtonColors = textButtonColors,
        textFieldShape = textFieldShape,
        textFieldColors = textFieldColors,
        textFieldTextStyle = textFieldTextStyle,
        imageShape = imageShape,
        imageContentScale = imageContentScale,
        imageCrossfadeEnabled = imageCrossfadeEnabled,
        imagePlaceholderColor = imagePlaceholderColor,
        imageErrorColor = imageErrorColor,
        imagePlaceholderIconSize = imagePlaceholderIconSize,
        avatarSize = avatarSize,
        dialogShape = dialogShape,
        sheetShape = sheetShape,
        overlayContentPadding = overlayContentPadding,
        sheetDragHandleWidth = sheetDragHandleWidth,
        sheetDragHandleHeight = sheetDragHandleHeight,
        sheetDragHandleColor = sheetDragHandleColor,
        dialogTitleStyle = dialogTitleStyle,
        dialogMessageStyle = dialogMessageStyle,
        sheetTitleStyle = sheetTitleStyle,
        toastShape = toastShape,
        toastContentPadding = toastContentPadding,
        toastIconSpacing = toastIconSpacing,
        toastTextStyle = toastTextStyle,
        toastIconSize = toastIconSize,
        toastElevation = toastElevation,
        toastHostPadding = toastHostPadding,
        toastSuccessContainerColor = toastSuccessContainerColor,
        toastSuccessContentColor = toastSuccessContentColor,
        toastErrorContainerColor = toastErrorContainerColor,
        toastErrorContentColor = toastErrorContentColor,
        toastInfoContainerColor = toastInfoContainerColor,
        toastInfoContentColor = toastInfoContentColor,
        toastWarningContainerColor = toastWarningContainerColor,
        toastWarningContentColor = toastWarningContentColor,
        toastDefaultContainerColor = toastDefaultContainerColor,
        toastDefaultContentColor = toastDefaultContentColor,
    )

    @Composable
    fun roundedTheme(cornerRadius: Dp = 16.dp): AppComponentTheme {
        val shape = RoundedCornerShape(cornerRadius)
        val sheetShape = RoundedCornerShape(
            topStart = cornerRadius,
            topEnd = cornerRadius,
            bottomStart = 0.dp,
            bottomEnd = 0.dp,
        )
        return theme(
            buttonShape = shape,
            textFieldShape = shape,
            imageShape = shape,
            dialogShape = shape,
            sheetShape = sheetShape,
        )
    }

    @Composable
    fun pillTheme(): AppComponentTheme = roundedTheme(cornerRadius = 999.dp)

    @Composable
    fun brandTheme(
        cornerRadius: Dp = 12.dp,
        primaryButtonColor: Color = MaterialTheme.colorScheme.primary,
        onPrimaryButtonColor: Color = MaterialTheme.colorScheme.onPrimary,
        outlinedBorderWidth: Dp = 1.dp,
    ): AppComponentTheme {
        val shape = RoundedCornerShape(cornerRadius)
        val sheetShape = RoundedCornerShape(
            topStart = cornerRadius,
            topEnd = cornerRadius,
            bottomStart = 0.dp,
            bottomEnd = 0.dp,
        )
        return theme(
            buttonShape = shape,
            textFieldShape = shape,
            imageShape = shape,
            dialogShape = shape,
            sheetShape = sheetShape,
            outlinedButtonBorderWidth = outlinedBorderWidth,
            filledButtonColors = ButtonDefaults.buttonColors(
                containerColor = primaryButtonColor,
                contentColor = onPrimaryButtonColor,
            ),
            elevatedButtonColors = ButtonDefaults.elevatedButtonColors(
                containerColor = primaryButtonColor,
                contentColor = onPrimaryButtonColor,
            ),
            textButtonColors = ButtonDefaults.textButtonColors(
                contentColor = primaryButtonColor,
            ),
            textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = primaryButtonColor,
                cursorColor = primaryButtonColor,
                focusedLabelColor = primaryButtonColor,
            ),
        )
    }
}

fun AppComponentTheme.withButtonShape(shape: Shape): AppComponentTheme = copy(buttonShape = shape)

fun AppComponentTheme.withFilledButtonColors(colors: ButtonColors): AppComponentTheme =
    copy(filledButtonColors = colors)

fun AppComponentTheme.withTextFieldColors(colors: TextFieldColors): AppComponentTheme =
    copy(textFieldColors = colors)

fun AppComponentTheme.withImageShape(shape: Shape): AppComponentTheme = copy(imageShape = shape)

fun AppComponentTheme.withDialogShape(shape: Shape): AppComponentTheme = copy(dialogShape = shape)

fun AppComponentTheme.withSheetShape(shape: Shape): AppComponentTheme = copy(sheetShape = shape)

fun AppComponentTheme.withToastShape(shape: Shape): AppComponentTheme = copy(toastShape = shape)

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
    )

    @Composable
    fun roundedTheme(cornerRadius: Dp = 16.dp): AppComponentTheme {
        val shape = RoundedCornerShape(cornerRadius)
        return theme(
            buttonShape = shape,
            textFieldShape = shape,
            imageShape = shape,
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
        return theme(
            buttonShape = shape,
            textFieldShape = shape,
            imageShape = shape,
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

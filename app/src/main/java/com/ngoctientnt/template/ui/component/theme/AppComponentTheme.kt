package com.ngoctientnt.template.ui.component.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

@Immutable
data class AppComponentTheme(
    val buttonShape: Shape,
    val buttonMinHeight: Dp,
    val buttonContentPadding: PaddingValues,
    val buttonTextStyle: TextStyle,
    val filledButtonColors: ButtonColors,
    val filledButtonElevation: ButtonElevation,
    val elevatedButtonColors: ButtonColors,
    val elevatedButtonElevation: ButtonElevation,
    val outlinedButtonColors: ButtonColors,
    val outlinedButtonBorderWidth: Dp,
    val textButtonColors: ButtonColors,
    val textFieldShape: Shape,
    val textFieldColors: TextFieldColors,
    val textFieldTextStyle: TextStyle,
    val imageShape: Shape,
    val imageContentScale: ContentScale,
    val imageCrossfadeEnabled: Boolean,
    val imagePlaceholderColor: Color,
    val imageErrorColor: Color,
    val imagePlaceholderIconSize: Dp,
    val avatarSize: Dp,
    val dialogShape: Shape,
    val sheetShape: Shape,
    val overlayContentPadding: Dp,
    val sheetDragHandleWidth: Dp,
    val sheetDragHandleHeight: Dp,
    val sheetDragHandleColor: Color,
    val dialogTitleStyle: TextStyle,
    val dialogMessageStyle: TextStyle,
    val sheetTitleStyle: TextStyle,
    val toastShape: Shape,
    val toastContentPadding: PaddingValues,
    val toastIconSpacing: Dp,
    val toastTextStyle: TextStyle,
    val toastIconSize: Dp,
    val toastElevation: Dp,
    val toastHostPadding: Dp,
    val toastSuccessContainerColor: Color,
    val toastSuccessContentColor: Color,
    val toastErrorContainerColor: Color,
    val toastErrorContentColor: Color,
    val toastInfoContainerColor: Color,
    val toastInfoContentColor: Color,
    val toastWarningContainerColor: Color,
    val toastWarningContentColor: Color,
    val toastDefaultContainerColor: Color,
    val toastDefaultContentColor: Color,
)

val LocalAppComponentTheme = staticCompositionLocalOf<AppComponentTheme> {
    error("AppComponentTheme not provided. Wrap content with ProvideAppComponentTheme inside TemplateTheme.")
}

@Composable
fun ProvideAppComponentTheme(
    theme: AppComponentTheme = AppComponentDefaults.theme(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalAppComponentTheme provides theme,
        content = content,
    )
}

package com.ngoctientnt.template.ui.component.toast

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.ngoctientnt.template.ui.component.theme.LocalAppComponentTheme

object AppToastDefaults {
    const val DEFAULT_DURATION_MS = 3_000L
    const val SUCCESS_DURATION_MS = 2_500L
    const val ERROR_DURATION_MS = 4_000L
    const val INFO_DURATION_MS = 3_000L
    const val WARNING_DURATION_MS = 3_500L
}

@Immutable
data class AppToastVisuals(
    val containerColor: Color,
    val contentColor: Color,
    val icon: ImageVector,
)

@Composable
fun AppToastType.visuals(): AppToastVisuals {
    val theme = LocalAppComponentTheme.current

    return when (this) {
        AppToastType.Success -> AppToastVisuals(
            containerColor = theme.toastSuccessContainerColor,
            contentColor = theme.toastSuccessContentColor,
            icon = Icons.Outlined.CheckCircle,
        )

        AppToastType.Error -> AppToastVisuals(
            containerColor = theme.toastErrorContainerColor,
            contentColor = theme.toastErrorContentColor,
            icon = Icons.Outlined.ErrorOutline,
        )

        AppToastType.Info -> AppToastVisuals(
            containerColor = theme.toastInfoContainerColor,
            contentColor = theme.toastInfoContentColor,
            icon = Icons.Outlined.Info,
        )

        AppToastType.Warning -> AppToastVisuals(
            containerColor = theme.toastWarningContainerColor,
            contentColor = theme.toastWarningContentColor,
            icon = Icons.Outlined.WarningAmber,
        )

        AppToastType.Default -> AppToastVisuals(
            containerColor = theme.toastDefaultContainerColor,
            contentColor = theme.toastDefaultContentColor,
            icon = Icons.Outlined.Notifications,
        )
    }
}

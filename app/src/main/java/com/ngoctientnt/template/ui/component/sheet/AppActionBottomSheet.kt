package com.ngoctientnt.template.ui.component.sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ngoctientnt.template.core.theme.AppThemeMode
import com.ngoctientnt.template.ui.theme.TemplateTheme

data class AppSheetAction(
    val label: String,
    val icon: ImageVector? = null,
    val destructive: Boolean = false,
    val onClick: () -> Unit,
)

/**
 * A pre-built bottom sheet displaying a list of [AppSheetAction] items.
 *
 * The sheet auto-dismisses after any action is tapped. If you need the
 * dismiss to be optional, provide your own [AppModalBottomSheet] with
 * custom action rows.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppActionBottomSheet(
    state: AppBottomSheetState,
    actions: List<AppSheetAction>,
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    onDismissRequest: () -> Unit = {},
) {
    AppModalBottomSheet(
        state = state,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    ) {
        AppBottomSheetLayout(
            title = title,
            subtitle = subtitle,
            scrollable = actions.size > 6,
        ) {
            actions.forEachIndexed { index, action ->
                AppSheetActionRow(
                    action = action,
                    onDismiss = {
                        state.hide()
                        onDismissRequest()
                    },
                )
                if (index < actions.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = MaterialTheme.colorScheme.outlineVariant,
                    )
                }
            }
        }
    }
}

/** Overload for one-shot use without a persistent state holder. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppActionBottomSheet(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    actions: List<AppSheetAction>,
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
) {
    AppModalBottomSheet(
        visible = visible,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    ) {
        AppBottomSheetLayout(
            title = title,
            subtitle = subtitle,
            scrollable = actions.size > 6,
        ) {
            actions.forEachIndexed { index, action ->
                AppSheetActionRow(
                    action = action,
                    onDismiss = onDismissRequest,
                )
                if (index < actions.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = MaterialTheme.colorScheme.outlineVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun AppSheetActionRow(
    action: AppSheetAction,
    onDismiss: () -> Unit,
) {
    val contentColor = if (action.destructive) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { role = Role.Button }
            .clickable {
                action.onClick()
                onDismiss()
            }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (action.icon != null) {
            Icon(
                imageVector = action.icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = contentColor,
            )
            Spacer(modifier = Modifier.width(16.dp))
        }

        Text(
            text = action.label,
            style = MaterialTheme.typography.bodyLarge,
            color = contentColor,
            modifier = Modifier.weight(1f),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Action Bottom Sheet Light")
@Composable
private fun AppActionBottomSheetPreviewLight() {
    TemplateTheme {
        AppActionBottomSheet(
            visible = true,
            title = "More options",
            subtitle = "Choose an action",
            onDismissRequest = {},
            actions = listOf(
                AppSheetAction(
                    label = "Settings",
                    icon = Icons.Outlined.Settings,
                    onClick = {},
                ),
                AppSheetAction(
                    label = "Delete account",
                    icon = Icons.Outlined.Delete,
                    destructive = true,
                    onClick = {},
                ),
            ),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Action Bottom Sheet Dark")
@Composable
private fun AppActionBottomSheetPreviewDark() {
    TemplateTheme(themeMode = AppThemeMode.DARK) {
        AppActionBottomSheet(
            visible = true,
            title = "More options",
            onDismissRequest = {},
            actions = listOf(
                AppSheetAction(
                    label = "Settings",
                    icon = Icons.Outlined.Settings,
                    onClick = {},
                ),
            ),
        )
    }
}

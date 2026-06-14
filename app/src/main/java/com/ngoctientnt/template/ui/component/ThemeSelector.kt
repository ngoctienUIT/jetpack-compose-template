package com.ngoctientnt.template.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.ngoctientnt.template.R
import com.ngoctientnt.template.core.theme.AppThemeMode
import com.ngoctientnt.template.ui.component.button.AppFilledButton

@Composable
fun ThemeSelector(
    selectedThemeMode: AppThemeMode,
    onThemeModeSelected: (AppThemeMode) -> Unit,
    onApply: () -> Unit,
    canApply: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.selectableGroup()) {
        Text(
            text = stringResource(R.string.settings_theme),
            style = MaterialTheme.typography.titleMedium,
        )

        AppThemeMode.selectable.forEach { themeMode ->
            ThemeOption(
                themeMode = themeMode,
                selected = selectedThemeMode == themeMode,
                onSelected = { onThemeModeSelected(themeMode) },
            )
        }

        AppFilledButton(
            text = stringResource(R.string.theme_apply),
            onClick = onApply,
            enabled = canApply,
            fullWidth = true,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}

@Composable
private fun ThemeOption(
    themeMode: AppThemeMode,
    selected: Boolean,
    onSelected: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onSelected,
                role = Role.RadioButton,
            )
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Text(
            text = stringResource(themeMode.displayNameRes),
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

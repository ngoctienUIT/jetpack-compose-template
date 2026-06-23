package com.ngoctientnt.template.ui.component.sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ngoctientnt.template.ui.component.theme.LocalAppComponentTheme

@Composable
fun AppBottomSheetLayout(
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    scrollable: Boolean = true,
    actions: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val theme = LocalAppComponentTheme.current
    val contentPadding = theme.overlayContentPadding
    val scrollState = rememberScrollState()
    val bodyModifier = if (scrollable) {
        Modifier.verticalScroll(scrollState)
    } else {
        Modifier
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = contentPadding)
            .padding(bottom = contentPadding),
    ) {
        if (title != null) {
            Text(
                text = title,
                style = theme.sheetTitleStyle,
            )
        }

        if (subtitle != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        if (title != null || subtitle != null) {
            Spacer(modifier = Modifier.height(16.dp))
        }

        Column(
            modifier = bodyModifier.fillMaxWidth(),
            content = content,
        )

        if (actions != null) {
            Spacer(modifier = Modifier.height(16.dp))
            actions()
        }
    }
}

package com.ngoctientnt.template.ui.component.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Stable
class AppDialogState(
    initialVisible: Boolean = false,
) {
    var isVisible by mutableStateOf(initialVisible)
        private set

    fun show() {
        isVisible = true
    }

    fun hide() {
        isVisible = false
    }

    fun toggle() {
        isVisible = !isVisible
    }
}

@Composable
fun rememberAppDialogState(
    initialVisible: Boolean = false,
): AppDialogState = remember { AppDialogState(initialVisible) }

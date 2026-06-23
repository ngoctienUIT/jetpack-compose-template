package com.ngoctientnt.template.ui.component.sheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.rememberModalBottomSheetState

@OptIn(ExperimentalMaterial3Api::class)
@Stable
class AppBottomSheetState(
    val sheetState: SheetState,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberAppBottomSheetState(
    skipPartiallyExpanded: Boolean = false,
    initialVisible: Boolean = false,
): AppBottomSheetState {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded,
    )
    return remember(sheetState) {
        AppBottomSheetState(
            sheetState = sheetState,
            initialVisible = initialVisible,
        )
    }
}

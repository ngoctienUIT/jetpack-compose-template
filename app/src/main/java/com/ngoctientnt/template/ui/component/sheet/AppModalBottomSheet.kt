package com.ngoctientnt.template.ui.component.sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.ngoctientnt.template.ui.component.theme.LocalAppComponentTheme

/**
 * Base bottom sheet wrapper with proper enter/exit animations.
 *
 * The sheet stays in composition until the hide animation completes —
 * avoid `if (!visible) return` anti-pattern that skips the exit animation.
 *
 * Prefer the [AppBottomSheetState] overload for screens where the sheet
 * can be shown/hidden multiple times, as it preserves [SheetState] across
 * recompositions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppModalBottomSheet(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    showDragHandle: Boolean = true,
    shape: Shape = LocalAppComponentTheme.current.sheetShape,
    title: (@Composable () -> Unit)? = null,
    actions: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    // Drive sheetState from the visible flag so the sheet animates in/out
    // rather than being abruptly added/removed from composition.
    LaunchedEffect(visible) {
        if (visible) sheetState.show() else sheetState.hide()
    }

    // Keep mounted while visible OR while the hide animation is still running.
    if (visible || sheetState.isVisible) {
        val theme = LocalAppComponentTheme.current

        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            sheetState = sheetState,
            shape = shape,
            dragHandle = if (showDragHandle) {
                {
                    BottomSheetDefaults.DragHandle(
                        width = theme.sheetDragHandleWidth,
                        height = theme.sheetDragHandleHeight,
                        shape = RoundedCornerShape(50),
                        color = theme.sheetDragHandleColor,
                    )
                }
            } else {
                null
            },
        ) {
            Column {
                title?.invoke()
                if (title != null) {
                    Spacer(modifier = Modifier.height(theme.overlayContentPadding / 2))
                }
                content()
                actions?.let { footer ->
                    Spacer(modifier = Modifier.height(theme.overlayContentPadding / 2))
                    footer()
                }
                // Bottom spacing is handled by AppBottomSheetLayout's own padding;
                // do not add extra Spacer here to avoid double-padding.
            }
        }
    }
}

/**
 * State-based overload. Recommended for feature screens — callers control
 * visibility via [AppBottomSheetState.show] / [AppBottomSheetState.hide].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppModalBottomSheet(
    state: AppBottomSheetState,
    onDismissRequest: () -> Unit = {},
    modifier: Modifier = Modifier,
    showDragHandle: Boolean = true,
    shape: Shape = LocalAppComponentTheme.current.sheetShape,
    title: (@Composable () -> Unit)? = null,
    actions: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    AppModalBottomSheet(
        visible = state.isVisible,
        onDismissRequest = {
            state.hide()
            onDismissRequest()
        },
        modifier = modifier,
        sheetState = state.sheetState,
        showDragHandle = showDragHandle,
        shape = shape,
        title = title,
        actions = actions,
        content = content,
    )
}

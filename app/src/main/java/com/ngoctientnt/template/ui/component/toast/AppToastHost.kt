package com.ngoctientnt.template.ui.component.toast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.ngoctientnt.template.ui.component.button.AppTextButton
import com.ngoctientnt.template.ui.component.theme.LocalAppComponentTheme
import com.ngoctientnt.template.ui.theme.TemplateTheme
import kotlinx.coroutines.delay

private const val ANIM_ENTER_MS = 200
private const val ANIM_EXIT_MS = 200

/**
 * Renders the current toast from [controller] with enter/exit animations.
 *
 * Place this once inside the app root scaffold, above the nav host, so it
 * floats over all content. Edge-to-edge insets are applied automatically.
 */
@Composable
fun AppToastHost(
    controller: AppToastController,
    modifier: Modifier = Modifier,
) {
    val toast = controller.currentToast ?: return
    val theme = LocalAppComponentTheme.current
    val position = controller.resolvePosition(toast)
    val duration = controller.resolveDuration(toast)
    val visuals = toast.type.visuals()

    // Local visibility drives enter/exit animation.
    // Keyed on toast.id so each unique toast restarts the timer independently.
    var visible by remember(toast.id) { mutableStateOf(false) }

    LaunchedEffect(toast.id) {
        visible = true
        delay(duration)
        visible = false
        delay((ANIM_EXIT_MS + 50).toLong())
        controller.onToastHidden()
    }

    val slideOffset: (Int) -> Int = { size ->
        when (position) {
            AppToastPosition.Top -> -(size / 2)
            AppToastPosition.Bottom -> size / 2
        }
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(ANIM_ENTER_MS)) + slideInVertically(tween(ANIM_ENTER_MS), slideOffset),
            exit = fadeOut(tween(ANIM_EXIT_MS)) + slideOutVertically(tween(ANIM_EXIT_MS), slideOffset),
        ) {
            val insetModifier = when (position) {
                AppToastPosition.Top -> Modifier.statusBarsPadding()
                AppToastPosition.Bottom -> Modifier.navigationBarsPadding()
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(insetModifier)
                    .padding(horizontal = theme.overlayContentPadding)
                    .padding(vertical = theme.toastHostPadding)
                    .semantics { liveRegion = LiveRegionMode.Polite },
                shape = theme.toastShape,
                color = visuals.containerColor,
                shadowElevation = theme.toastElevation,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(theme.toastContentPadding),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = visuals.icon,
                        contentDescription = null,
                        modifier = Modifier.size(theme.toastIconSize),
                        tint = visuals.contentColor,
                    )
                    Spacer(modifier = Modifier.width(theme.toastIconSpacing))
                    Text(
                        text = toast.message,
                        style = theme.toastTextStyle,
                        color = visuals.contentColor,
                        modifier = Modifier.weight(1f),
                    )
                    toast.action?.let { action ->
                        AppTextButton(
                            text = action.label,
                            onClick = {
                                action.onClick()
                                controller.dismiss()
                            },
                            colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                                contentColor = visuals.contentColor,
                            ),
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Toast Success")
@Composable
private fun AppToastSuccessPreview() {
    TemplateTheme {
        val controller = rememberAppToastController()
        LaunchedEffect(Unit) { controller.showSuccess("Changes saved successfully") }
        AppToastHost(controller = controller)
    }
}

@Preview(name = "Toast Error")
@Composable
private fun AppToastErrorPreview() {
    TemplateTheme {
        val controller = rememberAppToastController()
        LaunchedEffect(Unit) { controller.showError("Something went wrong. Please try again.") }
        AppToastHost(controller = controller)
    }
}

@Preview(name = "Toast Warning with action")
@Composable
private fun AppToastWarningPreview() {
    TemplateTheme {
        val controller = rememberAppToastController()
        LaunchedEffect(Unit) {
            controller.showWarning(
                message = "Connection is unstable",
                action = AppToastAction("Retry") {},
            )
        }
        AppToastHost(controller = controller)
    }
}

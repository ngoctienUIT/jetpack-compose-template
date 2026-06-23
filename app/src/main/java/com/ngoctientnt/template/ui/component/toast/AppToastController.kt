package com.ngoctientnt.template.ui.component.toast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.CompositionLocalProvider
import java.util.concurrent.atomic.AtomicLong

/**
 * Controller for displaying and queuing toast messages.
 *
 * Usage:
 * ```kotlin
 * val toast = LocalAppToastController.current
 * toast.showSuccess("Saved!")
 * toast.showError("Failed", action = AppToastAction("Retry") { retry() })
 * ```
 *
 * Mount [AppToastHost] once at the app root inside [ProvideAppToastController].
 */
@Stable
class AppToastController(
    private val defaultDurationMillis: Long = AppToastDefaults.DEFAULT_DURATION_MS,
    private val defaultPosition: AppToastPosition = AppToastPosition.Bottom,
) {
    private val idCounter = AtomicLong(0)
    private val queue = ArrayDeque<AppToastData>()

    var currentToast by mutableStateOf<AppToastData?>(null)
        private set

    val hasVisibleToast: Boolean
        get() = currentToast != null

    // region — show helpers

    fun show(
        message: String,
        type: AppToastType = AppToastType.Default,
        durationMillis: Long? = null,
        position: AppToastPosition? = null,
        action: AppToastAction? = null,
    ) {
        enqueue(
            AppToastData(
                message = message,
                type = type,
                durationMillis = durationMillis,
                position = position,
                action = action,
            ),
        )
    }

    fun showSuccess(
        message: String,
        durationMillis: Long? = null,
        position: AppToastPosition? = null,
        action: AppToastAction? = null,
    ) = show(message, AppToastType.Success, durationMillis, position, action)

    fun showError(
        message: String,
        durationMillis: Long? = null,
        position: AppToastPosition? = null,
        action: AppToastAction? = null,
    ) = show(message, AppToastType.Error, durationMillis, position, action)

    fun showInfo(
        message: String,
        durationMillis: Long? = null,
        position: AppToastPosition? = null,
        action: AppToastAction? = null,
    ) = show(message, AppToastType.Info, durationMillis, position, action)

    fun showWarning(
        message: String,
        durationMillis: Long? = null,
        position: AppToastPosition? = null,
        action: AppToastAction? = null,
    ) = show(message, AppToastType.Warning, durationMillis, position, action)

    // endregion

    /** Immediately dismisses the current toast and shows the next one in queue. */
    fun dismiss() {
        currentToast = null
        showNext()
    }

    // Called by AppToastHost after the exit animation completes.
    internal fun onToastHidden() {
        currentToast = null
        showNext()
    }

    internal fun resolveDuration(data: AppToastData): Long =
        data.durationMillis ?: typeDuration(data.type) ?: defaultDurationMillis

    internal fun resolvePosition(data: AppToastData): AppToastPosition =
        data.position ?: defaultPosition

    private fun typeDuration(type: AppToastType): Long? = when (type) {
        AppToastType.Error -> AppToastDefaults.ERROR_DURATION_MS
        AppToastType.Success -> AppToastDefaults.SUCCESS_DURATION_MS
        AppToastType.Warning -> AppToastDefaults.WARNING_DURATION_MS
        AppToastType.Info -> AppToastDefaults.INFO_DURATION_MS
        AppToastType.Default -> null
    }

    private fun enqueue(data: AppToastData) {
        val item = data.copy(id = idCounter.incrementAndGet())
        queue.addLast(item)
        if (currentToast == null) showNext()
    }

    private fun showNext() {
        currentToast = queue.removeFirstOrNull()
    }
}

val LocalAppToastController = staticCompositionLocalOf<AppToastController> {
    error("AppToastController not provided. Wrap content with ProvideAppToastController.")
}

@Composable
fun rememberAppToastController(
    defaultDurationMillis: Long = AppToastDefaults.DEFAULT_DURATION_MS,
    defaultPosition: AppToastPosition = AppToastPosition.Bottom,
): AppToastController = remember(defaultDurationMillis, defaultPosition) {
    AppToastController(
        defaultDurationMillis = defaultDurationMillis,
        defaultPosition = defaultPosition,
    )
}

@Composable
fun ProvideAppToastController(
    controller: AppToastController = rememberAppToastController(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalAppToastController provides controller,
        content = content,
    )
}

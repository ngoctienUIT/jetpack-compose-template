package com.ngoctientnt.template.ui.component.toast

import androidx.compose.runtime.Immutable

@Immutable
data class AppToastAction(
    val label: String,
    val onClick: () -> Unit,
)

/**
 * Represents a single toast notification.
 *
 * [id] is assigned by [AppToastController] and used to uniquely identify each
 * enqueued toast. Callers should not set it manually.
 */
@Immutable
data class AppToastData(
    val id: Long = 0L,
    val message: String,
    val type: AppToastType = AppToastType.Default,
    val durationMillis: Long? = null,
    val position: AppToastPosition? = null,
    val action: AppToastAction? = null,
)

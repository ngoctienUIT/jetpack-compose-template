package com.ngoctientnt.template.feature.explore

data class ExploreUiState(
    val retryKey: Int = 0,
)

sealed interface ExploreIntent {
    data object RetryRequested : ExploreIntent
    data object RefreshRequested : ExploreIntent
}

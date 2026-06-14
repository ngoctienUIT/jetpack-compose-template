package com.ngoctientnt.template.feature.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ngoctientnt.template.feature.explore.domain.ExploreItem
import com.ngoctientnt.template.feature.explore.domain.usecase.GetExploreItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class ExploreViewModel @Inject constructor(
    getExploreItemsUseCase: GetExploreItemsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    val items: Flow<PagingData<ExploreItem>> = getExploreItemsUseCase()
        .cachedIn(viewModelScope)

    fun onIntent(intent: ExploreIntent) {
        when (intent) {
            ExploreIntent.RetryRequested -> reduce { copy(retryKey = retryKey + 1) }
            ExploreIntent.RefreshRequested -> reduce { copy(retryKey = retryKey + 1) }
        }
    }

    private inline fun reduce(block: ExploreUiState.() -> ExploreUiState) {
        _uiState.update(block)
    }
}

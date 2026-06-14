package com.ngoctientnt.template.feature.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ngoctientnt.template.core.theme.AppThemeMode
import com.ngoctientnt.template.core.theme.ThemeManager
import com.ngoctientnt.template.core.theme.ThemeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ThemeViewModel @Inject constructor(
    themeRepository: ThemeRepository,
    private val themeManager: ThemeManager,
) : ViewModel() {

    val currentThemeMode: StateFlow<AppThemeMode> = themeRepository.currentThemeMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AppThemeMode.current(),
        )

    private val _pendingThemeMode = MutableStateFlow(AppThemeMode.current())
    val pendingThemeMode: StateFlow<AppThemeMode> = _pendingThemeMode.asStateFlow()

    val canApplyTheme: StateFlow<Boolean> = combine(
        currentThemeMode,
        pendingThemeMode,
    ) { current, pending ->
        current != pending
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false,
    )

    init {
        viewModelScope.launch {
            currentThemeMode.collect { themeMode ->
                _pendingThemeMode.value = themeMode
            }
        }
    }

    fun selectThemeMode(themeMode: AppThemeMode) {
        _pendingThemeMode.value = themeMode
    }

    fun applyThemeMode() {
        viewModelScope.launch {
            themeManager.setThemeMode(_pendingThemeMode.value)
        }
    }
}

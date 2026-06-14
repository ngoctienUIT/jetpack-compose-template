package com.ngoctientnt.template.feature.locale

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ngoctientnt.template.core.locale.AppLanguage
import com.ngoctientnt.template.core.locale.LocaleManager
import com.ngoctientnt.template.core.locale.LocaleRepository
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
class LocaleViewModel @Inject constructor(
    localeRepository: LocaleRepository,
    private val localeManager: LocaleManager,
) : ViewModel() {

    val currentLanguage: StateFlow<AppLanguage> = localeRepository.currentLanguage
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AppLanguage.current(),
        )

    private val _pendingLanguage = MutableStateFlow(AppLanguage.current())
    val pendingLanguage: StateFlow<AppLanguage> = _pendingLanguage.asStateFlow()

    val canApplyLanguage: StateFlow<Boolean> = combine(
        currentLanguage,
        pendingLanguage,
    ) { current, pending ->
        current != pending
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false,
    )

    init {
        viewModelScope.launch {
            currentLanguage.collect { language ->
                _pendingLanguage.value = language
            }
        }
    }

    fun selectLanguage(language: AppLanguage) {
        _pendingLanguage.value = language
    }

    fun applyLanguage() {
        viewModelScope.launch {
            localeManager.setLanguage(_pendingLanguage.value)
        }
    }
}

package com.ngoctientnt.template.feature.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ngoctientnt.template.app.navigation.BottomNavTab
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(restoreTab(savedStateHandle))
    val selectedTab: StateFlow<BottomNavTab> = _selectedTab.asStateFlow()

    private var hasAppliedRouteTab = false

    fun applyInitialTabOnce(routeTab: BottomNavTab) {
        if (hasAppliedRouteTab) return
        hasAppliedRouteTab = true
        selectTab(routeTab)
    }

    fun selectTab(tab: BottomNavTab) {
        if (_selectedTab.value == tab) return
        _selectedTab.value = tab
        savedStateHandle[SELECTED_TAB_KEY] = tab.name
    }

    companion object {
        private const val SELECTED_TAB_KEY = "selected_tab"

        private fun restoreTab(savedStateHandle: SavedStateHandle): BottomNavTab {
            return BottomNavTab.fromName(savedStateHandle.get<String>(SELECTED_TAB_KEY))
        }
    }
}

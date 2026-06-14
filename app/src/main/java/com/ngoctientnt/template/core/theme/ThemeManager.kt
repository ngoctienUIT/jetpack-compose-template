package com.ngoctientnt.template.core.theme

import androidx.appcompat.app.AppCompatDelegate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeManager @Inject constructor(
    private val themeRepository: ThemeRepository,
) {
    suspend fun readStoredThemeMode(): AppThemeMode = themeRepository.getThemeMode()

    fun applyThemeMode(themeMode: AppThemeMode) {
        AppCompatDelegate.setDefaultNightMode(themeMode.toNightMode())
    }

    suspend fun setThemeMode(themeMode: AppThemeMode) {
        themeRepository.setThemeMode(themeMode)
        applyThemeMode(themeMode)
    }
}

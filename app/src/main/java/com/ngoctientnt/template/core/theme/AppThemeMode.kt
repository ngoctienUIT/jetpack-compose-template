package com.ngoctientnt.template.core.theme

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import com.ngoctientnt.template.R

/**
 * Supported app theme modes. To add a new mode, extend this enum and update
 * [ThemeRepository] persistence if needed.
 */
enum class AppThemeMode(
    val storageValue: String?,
    @StringRes val displayNameRes: Int,
) {
    SYSTEM(null, R.string.theme_system),
    LIGHT("light", R.string.theme_light),
    DARK("dark", R.string.theme_dark),
    ;

    fun toNightMode(): Int = when (this) {
        SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
        DARK -> AppCompatDelegate.MODE_NIGHT_YES
    }

    fun isDarkTheme(isSystemInDarkTheme: Boolean): Boolean = when (this) {
        SYSTEM -> isSystemInDarkTheme
        LIGHT -> false
        DARK -> true
    }

    companion object {
        val selectable: List<AppThemeMode> = entries

        fun fromStorageValue(value: String?): AppThemeMode {
            if (value.isNullOrBlank()) return SYSTEM
            return entries.find { it.storageValue == value } ?: SYSTEM
        }

        fun fromNightMode(nightMode: Int): AppThemeMode = when (nightMode) {
            AppCompatDelegate.MODE_NIGHT_NO -> LIGHT
            AppCompatDelegate.MODE_NIGHT_YES -> DARK
            else -> SYSTEM
        }

        fun current(): AppThemeMode = fromNightMode(AppCompatDelegate.getDefaultNightMode())
    }
}

package com.ngoctientnt.template.core.theme

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ngoctientnt.template.core.config.DataStoreNames
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(
    name = DataStoreNames.THEME,
)

@Singleton
class ThemeRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    val currentThemeMode: Flow<AppThemeMode> = context.themeDataStore.data.map { preferences ->
        AppThemeMode.fromStorageValue(preferences[THEME_MODE_KEY])
    }

    suspend fun getThemeMode(): AppThemeMode {
        return currentThemeMode.first()
    }

    suspend fun setThemeMode(themeMode: AppThemeMode) {
        context.themeDataStore.edit { preferences ->
            if (themeMode.storageValue == null) {
                preferences.remove(THEME_MODE_KEY)
            } else {
                preferences[THEME_MODE_KEY] = themeMode.storageValue
            }
        }
    }

    companion object {
        private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
    }
}

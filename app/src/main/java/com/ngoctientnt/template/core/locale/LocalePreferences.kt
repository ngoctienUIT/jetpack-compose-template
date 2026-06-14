package com.ngoctientnt.template.core.locale

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.localeDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "locale_preferences",
)

@Singleton
class LocaleRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    val currentLanguage: Flow<AppLanguage> = context.localeDataStore.data.map { preferences ->
        AppLanguage.fromLanguageTag(preferences[LANGUAGE_TAG_KEY])
    }

    suspend fun getLanguage(): AppLanguage {
        return currentLanguage.first()
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.localeDataStore.edit { preferences ->
            if (language.languageTag == null) {
                preferences.remove(LANGUAGE_TAG_KEY)
            } else {
                preferences[LANGUAGE_TAG_KEY] = language.languageTag
            }
        }
    }

    companion object {
        private val LANGUAGE_TAG_KEY = stringPreferencesKey("language_tag")
    }
}

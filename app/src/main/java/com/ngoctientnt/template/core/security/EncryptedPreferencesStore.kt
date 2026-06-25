package com.ngoctientnt.template.core.security

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class EncryptedPreferencesStore(
    private val dataStore: DataStore<Preferences>,
) {
    suspend fun getString(key: Preferences.Key<String>): String? {
        return dataStore.data.first()[key]
    }

    suspend fun putString(key: Preferences.Key<String>, value: String) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun remove(key: Preferences.Key<String>) {
        dataStore.edit { preferences ->
            preferences.remove(key)
        }
    }

    suspend fun edit(block: suspend (MutablePreferences) -> Unit) {
        dataStore.edit(block)
    }

    fun getStringBlocking(key: Preferences.Key<String>): String? = runBlocking {
        withContext(Dispatchers.IO) {
            getString(key)
        }
    }

    fun putStringBlocking(key: Preferences.Key<String>, value: String) = runBlocking {
        withContext(Dispatchers.IO) {
            putString(key, value)
        }
    }

    suspend fun warmUp() {
        dataStore.data.first()
    }
}

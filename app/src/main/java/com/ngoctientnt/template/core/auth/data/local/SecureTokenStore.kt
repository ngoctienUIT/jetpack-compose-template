package com.ngoctientnt.template.core.auth.data.local

import androidx.datastore.preferences.core.stringPreferencesKey
import com.ngoctientnt.template.core.auth.domain.model.AuthTokens
import com.ngoctientnt.template.core.config.DataStoreNames
import com.ngoctientnt.template.core.config.SecureStorageNames
import com.ngoctientnt.template.core.security.EncryptedPreferencesStoreFactory
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Singleton
class SecureTokenStore @Inject constructor(
    encryptedPreferencesStoreFactory: EncryptedPreferencesStoreFactory,
) {
    private val store = encryptedPreferencesStoreFactory.create(
        dataStoreFileName = DataStoreNames.AUTH_ENCRYPTED,
        keysetSharedPrefsName = SecureStorageNames.AUTH_KEYSET,
        masterKeyUri = MASTER_KEY_URI,
        legacySharedPrefsName = SecureStorageNames.AUTH_PREFS,
    )

    private val tokensState = MutableStateFlow(readTokensSync())

    val tokens: Flow<AuthTokens?> = tokensState.asStateFlow()

    fun readTokensSync(): AuthTokens? {
        val accessToken = store.getStringBlocking(ACCESS_TOKEN_KEY)
        val refreshToken = store.getStringBlocking(REFRESH_TOKEN_KEY)
        if (accessToken.isNullOrBlank() || refreshToken.isNullOrBlank()) {
            return null
        }
        return AuthTokens(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    suspend fun saveTokens(tokens: AuthTokens) {
        store.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = tokens.accessToken
            preferences[REFRESH_TOKEN_KEY] = tokens.refreshToken
        }
        tokensState.update { tokens }
    }

    suspend fun clearTokens() {
        store.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
        }
        tokensState.update { null }
    }

    companion object {
        private const val MASTER_KEY_URI = "android-keystore://auth_encrypted_master_key"
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }
}

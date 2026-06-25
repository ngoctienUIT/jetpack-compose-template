package com.ngoctientnt.template.data.local.security

import android.util.Base64
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ngoctientnt.template.core.config.DataStoreNames
import com.ngoctientnt.template.core.config.SecureStorageNames
import com.ngoctientnt.template.core.security.EncryptedPreferencesStoreFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabasePassphraseProvider @Inject constructor(
    encryptedPreferencesStoreFactory: EncryptedPreferencesStoreFactory,
) {
    private val store = encryptedPreferencesStoreFactory.create(
        dataStoreFileName = DataStoreNames.DATABASE_PASSPHRASE,
        keysetSharedPrefsName = SecureStorageNames.DATABASE_PASSPHRASE_KEYSET,
        masterKeyUri = MASTER_KEY_URI,
        legacySharedPrefsName = SecureStorageNames.DATABASE_PASSPHRASE_PREFS,
    )

    @Synchronized
    fun getPassphrase(): ByteArray {
        val existing = store.getStringBlocking(PASSPHRASE_KEY)
        if (existing != null) {
            return Base64.decode(existing, Base64.NO_WRAP)
        }

        val passphrase = ByteArray(PASSPHRASE_LENGTH).also { bytes ->
            java.security.SecureRandom().nextBytes(bytes)
        }
        store.putStringBlocking(
            PASSPHRASE_KEY,
            Base64.encodeToString(passphrase, Base64.NO_WRAP),
        )
        return passphrase
    }

    companion object {
        private const val MASTER_KEY_URI = "android-keystore://database_passphrase_master_key"
        private const val PASSPHRASE_LENGTH = 32
        private val PASSPHRASE_KEY = stringPreferencesKey("database_passphrase")
    }
}

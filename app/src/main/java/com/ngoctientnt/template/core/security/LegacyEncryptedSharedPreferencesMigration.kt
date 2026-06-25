@file:Suppress("DEPRECATION")

package com.ngoctientnt.template.core.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Opens legacy [EncryptedSharedPreferences] for one-time migration to encrypted DataStore.
 * Deprecated APIs are isolated here only.
 */
internal object LegacyEncryptedSharedPreferencesMigration {

    fun openLegacyStore(context: Context, prefsName: String): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            context,
            prefsName,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }
}

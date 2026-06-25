package com.ngoctientnt.template.core.security

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.PreferencesFileSerializer
import androidx.datastore.tink.AeadSerializer
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplate
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.PredefinedAeadParameters
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.ngoctientnt.template.di.ApplicationScope
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@Singleton
class EncryptedPreferencesStoreFactory @Inject constructor(
    @ApplicationContext private val context: Context,
    @ApplicationScope private val applicationScope: CoroutineScope,
) {
    private val stores = ConcurrentHashMap<String, EncryptedPreferencesStore>()

    fun create(
        dataStoreFileName: String,
        keysetSharedPrefsName: String,
        masterKeyUri: String,
        legacySharedPrefsName: String? = null,
    ): EncryptedPreferencesStore {
        return stores.getOrPut(dataStoreFileName) {
            buildStore(
                dataStoreFileName = dataStoreFileName,
                keysetSharedPrefsName = keysetSharedPrefsName,
                masterKeyUri = masterKeyUri,
                legacySharedPrefsName = legacySharedPrefsName,
            )
        }
    }

    private fun buildStore(
        dataStoreFileName: String,
        keysetSharedPrefsName: String,
        masterKeyUri: String,
        legacySharedPrefsName: String?,
    ): EncryptedPreferencesStore {
        ensureTinkRegistered()

        val keysetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(context, "keyset", keysetSharedPrefsName)
            .withKeyTemplate(KeyTemplate.createFrom(PredefinedAeadParameters.AES256_GCM))
            .withMasterKeyUri(masterKeyUri)
            .build()
            .keysetHandle

        val aead = keysetHandle.getPrimitive(Aead::class.java)
        val aeadSerializer = AeadSerializer(
            aead = aead,
            wrappedSerializer = PreferencesFileSerializer,
            associatedData = dataStoreFileName.encodeToByteArray(),
        )

        val migrations = buildList {
            if (legacySharedPrefsName != null) {
                add(
                    SharedPreferencesMigration(
                        produceSharedPreferences = {
                            LegacyEncryptedSharedPreferencesMigration.openLegacyStore(
                                context,
                                legacySharedPrefsName,
                            )
                        },
                    ),
                )
            }
        }

        val dataStore: DataStore<Preferences> = DataStoreFactory.create(
            serializer = aeadSerializer,
            scope = applicationScope,
            migrations = migrations,
            corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
        ) {
            File(context.filesDir, "datastore/$dataStoreFileName.preferences_pb")
        }

        val store = EncryptedPreferencesStore(dataStore)

        if (legacySharedPrefsName != null) {
            runBlocking {
                withContext(Dispatchers.IO) {
                    store.warmUp()
                    context.deleteSharedPreferences(legacySharedPrefsName)
                }
            }
        }

        return store
    }

    companion object {
        @Volatile
        private var tinkRegistered = false

        private fun ensureTinkRegistered() {
            if (tinkRegistered) return
            synchronized(this) {
                if (!tinkRegistered) {
                    AeadConfig.register()
                    tinkRegistered = true
                }
            }
        }
    }
}

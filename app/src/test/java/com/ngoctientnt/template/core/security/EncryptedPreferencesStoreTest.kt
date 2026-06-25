package com.ngoctientnt.template.core.security

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.stringPreferencesKey
import java.io.File
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class EncryptedPreferencesStoreTest {

    @Test
    fun writeReadRoundTrip() = runTest {
        val tempDir = File.createTempFile("encrypted_prefs_test", null).apply {
            delete()
            mkdirs()
        }
        val dataStore = PreferenceDataStoreFactory.create {
            File(tempDir, "test.preferences_pb")
        }
        val store = EncryptedPreferencesStore(dataStore)
        val key = stringPreferencesKey("test_key")

        store.putString(key, "secret")
        assertEquals("secret", store.getString(key))

        store.remove(key)
        assertNull(store.getString(key))
    }
}

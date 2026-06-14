package com.ngoctientnt.template.core.appinfo.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ngoctientnt.template.core.appinfo.config.AppInfoConfig
import com.ngoctientnt.template.core.appinfo.model.AppUdid
import com.ngoctientnt.template.core.config.DataStoreNames
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private val Context.appInfoDataStore: DataStore<Preferences> by preferencesDataStore(
    name = DataStoreNames.APP_INFO,
)

@Singleton
class UdidStore @Inject constructor(
    @ApplicationContext private val context: Context,
    config: AppInfoConfig,
) {
    private val udidKey = stringPreferencesKey(config.udidPreferenceKey)
    private val mutex = Mutex()

    suspend fun getOrCreate(): AppUdid {
        return mutex.withLock {
            val existing = context.appInfoDataStore.data.map { preferences ->
                preferences[udidKey]
            }.first()

            if (existing != null) {
                return@withLock AppUdid(value = existing)
            }

            val generated = UUID.randomUUID().toString()
            context.appInfoDataStore.edit { preferences ->
                preferences[udidKey] = generated
            }
            AppUdid(value = generated)
        }
    }
}

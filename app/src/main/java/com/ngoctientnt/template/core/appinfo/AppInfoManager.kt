package com.ngoctientnt.template.core.appinfo

import com.ngoctientnt.template.core.appinfo.config.AppInfoConfig
import com.ngoctientnt.template.core.appinfo.model.AppInfo
import com.ngoctientnt.template.core.appinfo.model.AppPackageInfo
import com.ngoctientnt.template.core.appinfo.model.AppUdid
import com.ngoctientnt.template.core.appinfo.model.DeviceInfo
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Singleton
class AppInfoManager @Inject constructor(
    private val repository: AppInfoRepository,
    private val appInfoConfig: AppInfoConfig,
) {
    private val mutex = Mutex()
    private val _appInfo = MutableStateFlow<AppInfo?>(null)
    val appInfo: StateFlow<AppInfo?> = _appInfo.asStateFlow()

    suspend fun initialize() {
        ensureLoaded()
    }

    suspend fun ensureLoaded(): AppInfo {
        return mutex.withLock {
            _appInfo.value ?: repository.loadAppInfo().also { loaded ->
                _appInfo.value = loaded
            }
        }
    }

    suspend fun refresh(): AppInfo {
        return mutex.withLock {
            repository.loadAppInfo().also { loaded ->
                _appInfo.value = loaded
            }
        }
    }

    fun peekAppInfo(): AppInfo? = _appInfo.value

    suspend fun requireAppInfo(): AppInfo = peekAppInfo() ?: ensureLoaded()

    val deviceInfo: DeviceInfo?
        get() = peekAppInfo()?.device

    val packageInfo: AppPackageInfo?
        get() = peekAppInfo()?.packageInfo

    val udid: AppUdid?
        get() = peekAppInfo()?.udid

    suspend fun udidValue(): String = requireAppInfo().udid.value

    suspend fun toHttpHeaders(): Map<String, String> =
        requireAppInfo().toHttpHeaders(appInfoConfig)

    suspend fun toUserAgent(): String = requireAppInfo().toUserAgent()
}

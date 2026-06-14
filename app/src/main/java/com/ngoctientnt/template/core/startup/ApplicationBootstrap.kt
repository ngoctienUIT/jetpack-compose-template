package com.ngoctientnt.template.core.startup

import com.ngoctientnt.template.core.appinfo.AppInfoManager
import com.ngoctientnt.template.core.locale.LocaleManager
import com.ngoctientnt.template.core.logging.AppLogger
import com.ngoctientnt.template.core.notification.NotificationHelper
import com.ngoctientnt.template.core.theme.ThemeManager
import com.ngoctientnt.template.di.ApplicationScope
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Singleton
class ApplicationBootstrap @Inject constructor(
    @ApplicationScope private val applicationScope: CoroutineScope,
    private val localeManager: LocaleManager,
    private val themeManager: ThemeManager,
    private val appInfoManager: AppInfoManager,
    private val notificationHelper: NotificationHelper,
) {
    fun start() {
        runBlocking(Dispatchers.IO) {
            runCatching {
                localeManager.applyLanguage(localeManager.readStoredLanguage())
                themeManager.applyThemeMode(themeManager.readStoredThemeMode())
            }.onFailure { error ->
                AppLogger.e(TAG, "Failed to restore locale/theme preferences", error)
            }
        }

        applicationScope.launch(Dispatchers.IO) {
            runCatching {
                appInfoManager.initialize()
            }.onFailure { error ->
                AppLogger.e(TAG, "Failed to initialize app info", error)
            }
        }

        notificationHelper.createNotificationChannel()
    }

    companion object {
        private const val TAG = "ApplicationBootstrap"
    }
}

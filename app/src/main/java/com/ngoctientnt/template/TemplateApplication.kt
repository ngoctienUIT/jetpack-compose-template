package com.ngoctientnt.template

import android.app.Application
import com.ngoctientnt.template.core.locale.LocaleManager
import com.ngoctientnt.template.core.notification.NotificationHelper
import com.ngoctientnt.template.core.theme.ThemeManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@HiltAndroidApp
class TemplateApplication : Application() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var localeManager: LocaleManager

    @Inject
    lateinit var themeManager: ThemeManager

    override fun onCreate() {
        super.onCreate()

        runBlocking(Dispatchers.IO) {
            val storedLanguage = localeManager.readStoredLanguage()
            localeManager.applyLanguage(storedLanguage)

            val storedThemeMode = themeManager.readStoredThemeMode()
            themeManager.applyThemeMode(storedThemeMode)
        }

        notificationHelper.createNotificationChannel()
    }
}

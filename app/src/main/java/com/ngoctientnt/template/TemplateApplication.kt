package com.ngoctientnt.template

import android.app.Application
import com.ngoctientnt.template.core.locale.LocaleManager
import com.ngoctientnt.template.core.notification.NotificationHelper
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

    override fun onCreate() {
        super.onCreate()

        val storedLanguage = runBlocking(Dispatchers.IO) {
            localeManager.readStoredLanguage()
        }
        localeManager.applyLanguage(storedLanguage)

        notificationHelper.createNotificationChannel()
    }
}

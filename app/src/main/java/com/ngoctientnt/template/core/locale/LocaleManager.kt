package com.ngoctientnt.template.core.locale

import androidx.appcompat.app.AppCompatDelegate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocaleManager @Inject constructor(
    private val localeRepository: LocaleRepository,
) {
    suspend fun readStoredLanguage(): AppLanguage = localeRepository.getLanguage()

    fun applyLanguage(language: AppLanguage) {
        AppCompatDelegate.setApplicationLocales(language.toLocaleListCompat())
    }

    suspend fun setLanguage(language: AppLanguage) {
        localeRepository.setLanguage(language)
        applyLanguage(language)
    }
}

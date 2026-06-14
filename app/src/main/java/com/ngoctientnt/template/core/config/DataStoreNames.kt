package com.ngoctientnt.template.core.config

// Keep in sync with res/xml/backup_rules.xml
object DataStoreNames {
    const val APP_INFO = "app_info_store"
    const val LOCALE = "locale_preferences"
    const val THEME = "theme_preferences"
    // Auth tokens are stored in EncryptedSharedPreferences — see SecureStorageNames.AUTH_PREFS
    const val AUTH = "auth_secure_prefs"
}

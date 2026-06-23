package com.ngoctientnt.template.core.config

import com.ngoctientnt.template.BuildConfig

object AppConfig {
    val apiBaseUrl: String get() = BuildConfig.API_BASE_URL
    val environment: String get() = BuildConfig.ENVIRONMENT
    val buildType: String get() = BuildConfig.BUILD_TYPE
    val versionName: String get() = BuildConfig.VERSION_NAME
    val versionCode: Int get() = BuildConfig.VERSION_CODE

    val isDebug: Boolean get() = BuildConfig.DEBUG
    val isProductionFlavor: Boolean get() = environment == "production"
    val isStagingFlavor: Boolean get() = environment == "staging"

    val googleWebClientId: String get() = BuildConfig.GOOGLE_WEB_CLIENT_ID
    val facebookAppId: String get() = BuildConfig.FACEBOOK_APP_ID
    val facebookClientToken: String get() = BuildConfig.FACEBOOK_CLIENT_TOKEN

    val splashDelayMs: Long get() = BuildConfig.SPLASH_DELAY_MS
    val imageDiskCacheDir: String get() = BuildConfig.IMAGE_DISK_CACHE_DIR

    const val MIN_PASSWORD_LENGTH = 8
}

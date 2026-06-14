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

    const val SPLASH_DELAY_MS = 1_500L

    const val IMAGE_DISK_CACHE_DIR = "image_cache"
}

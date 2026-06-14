package com.ngoctientnt.template.core.appinfo.config

data class AppInfoConfig(
    val udidPreferenceKey: String = "device_udid",
    val includeAndroidId: Boolean = false,
    val includeScreenMetrics: Boolean = true,
    val includeHttpHeaders: Boolean = true,
    val includeUdidInHeaders: Boolean = true,
)

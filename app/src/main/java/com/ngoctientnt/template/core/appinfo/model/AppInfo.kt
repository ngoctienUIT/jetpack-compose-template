package com.ngoctientnt.template.core.appinfo.model

import com.ngoctientnt.template.core.appinfo.config.AppInfoConfig

data class AppInfo(
    val device: DeviceInfo,
    val packageInfo: AppPackageInfo,
    val udid: AppUdid,
) {
    fun toHttpHeaders(config: AppInfoConfig = AppInfoConfig()): Map<String, String> {
        if (!config.includeHttpHeaders) return emptyMap()

        return buildMap {
            if (config.includeUdidInHeaders) {
                put("X-App-UDID", udid.value)
            }
            put("X-App-Version", packageInfo.versionName)
            put("X-App-Build", packageInfo.versionCode.toString())
            put("X-App-Package", packageInfo.packageName)
            packageInfo.environment?.let { put("X-App-Environment", it) }
            put("X-Device-Model", device.model)
            put("X-Device-Brand", device.brand)
            put("X-Device-OS", "Android ${device.osVersion}")
            put("X-Device-SDK", device.sdkInt.toString())
            if (config.includeAndroidId) {
                device.androidId?.let { put("X-Device-Android-Id", it) }
            }
        }
    }

    fun toUserAgent(): String {
        return buildString {
            append(packageInfo.appName)
            append('/')
            append(packageInfo.versionName)
            append(" (Android ")
            append(device.osVersion)
            append("; ")
            append(device.model)
            append("; Build/")
            append(device.sdkInt)
            append(')')
        }
    }
}

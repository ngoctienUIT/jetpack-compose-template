package com.ngoctientnt.template.core.appinfo.model

data class AppPackageInfo(
    val appName: String,
    val packageName: String,
    val versionName: String,
    val versionCode: Long,
    val buildType: String?,
    val environment: String?,
)

package com.ngoctientnt.template.core.appinfo.model

data class DeviceInfo(
    val brand: String,
    val manufacturer: String,
    val model: String,
    val device: String,
    val product: String,
    val hardware: String,
    val osVersion: String,
    val sdkInt: Int,
    val androidId: String?,
    val isPhysicalDevice: Boolean,
    val screenWidthPx: Int,
    val screenHeightPx: Int,
    val screenDensity: Float,
    val locale: String,
)

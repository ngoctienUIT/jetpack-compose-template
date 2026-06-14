package com.ngoctientnt.template.ui.component.image

data class AppImageLoaderConfig(
    val crossfadeEnabled: Boolean = true,
    val crossfadeDurationMillis: Int = 300,
    val allowHardware: Boolean = true,
    val respectCacheHeaders: Boolean = true,
    val memoryCacheMaxSizePercent: Double = 0.25,
    val diskCacheMaxSizePercent: Double = 0.02,
    val diskCacheDirectoryName: String = "image_cache",
)

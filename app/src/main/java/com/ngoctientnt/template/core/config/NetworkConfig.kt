package com.ngoctientnt.template.core.config

data class NetworkConfig(
    val connectTimeoutSeconds: Long = 30,
    val readTimeoutSeconds: Long = 30,
    val writeTimeoutSeconds: Long = 30,
    val callTimeoutSeconds: Long = 60,
    val enableHttpLogging: Boolean = false,
    val attachAppInfoHeaders: Boolean = true,
)

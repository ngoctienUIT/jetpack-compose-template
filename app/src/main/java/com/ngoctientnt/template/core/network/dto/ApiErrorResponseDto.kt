package com.ngoctientnt.template.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponseDto(
    val message: String? = null,
    val code: String? = null,
)

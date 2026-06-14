package com.ngoctientnt.template.feature.explore.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ExploreItemDto(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String? = null,
)

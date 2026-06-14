package com.ngoctientnt.template.core.network.paging.dto

import kotlinx.serialization.Serializable

@Serializable
data class PagedResponseDto<T>(
    val items: List<T>,
    val page: Int? = null,
    val totalPages: Int? = null,
    val totalCount: Int? = null,
    val nextCursor: String? = null,
    val hasMore: Boolean? = null,
)

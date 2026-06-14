package com.ngoctientnt.template.core.network.paging.mapper

import com.ngoctientnt.template.core.network.paging.PagedResponse
import com.ngoctientnt.template.core.network.paging.dto.PagedResponseDto

fun <T> PagedResponseDto<T>.toPageBasedResponse(): PagedResponse<T> {
    val nextPage = when {
        page != null && totalPages != null && page < totalPages -> page + 1
        else -> null
    }

    return PagedResponse(
        items = items,
        nextPage = nextPage,
        totalCount = totalCount,
    )
}

fun <T> PagedResponseDto<T>.toCursorBasedResponse(): PagedResponse<T> {
    val resolvedNextCursor = when {
        hasMore == false -> null
        !nextCursor.isNullOrBlank() -> nextCursor
        else -> null
    }

    return PagedResponse(
        items = items,
        nextCursor = resolvedNextCursor,
        totalCount = totalCount,
    )
}

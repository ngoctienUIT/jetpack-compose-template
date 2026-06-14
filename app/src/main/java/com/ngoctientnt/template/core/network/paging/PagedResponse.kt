package com.ngoctientnt.template.core.network.paging

data class PagedResponse<T>(
    val items: List<T>,
    val nextPage: Int? = null,
    val nextCursor: String? = null,
    val totalCount: Int? = null,
) {
    val hasNextPage: Boolean
        get() = nextPage != null

    val hasNextCursor: Boolean
        get() = !nextCursor.isNullOrBlank()
}

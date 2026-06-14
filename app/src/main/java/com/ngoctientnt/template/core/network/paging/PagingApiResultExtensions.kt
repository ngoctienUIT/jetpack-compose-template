package com.ngoctientnt.template.core.network.paging

import androidx.paging.PagingSource.LoadResult
import com.ngoctientnt.template.core.network.result.ApiResult
import com.ngoctientnt.template.core.network.result.ApiUiErrors
import com.ngoctientnt.template.core.network.result.toApiException

fun <T> PagingApiResult<T>.itemsOrEmpty(): List<T> {
    return (this as? ApiResult.Success)?.data?.items.orEmpty()
}

fun <T : Any> PagingApiResult<T>.toPageLoadResult(currentPage: Int): LoadResult<Int, T> {
    return when (this) {
        is ApiResult.Success -> {
            val response = data
            LoadResult.Page(
                data = response.items,
                prevKey = if (currentPage <= PagingDefaults.INITIAL_PAGE) {
                    null
                } else {
                    currentPage - 1
                },
                nextKey = response.nextPage,
            )
        }
        else -> LoadResult.Error(toApiException())
    }
}

fun <T : Any> PagingApiResult<T>.toCursorLoadResult(currentCursor: String?): LoadResult<String, T> {
    return when (this) {
        is ApiResult.Success -> {
            val response = data
            LoadResult.Page(
                data = response.items,
                prevKey = currentCursor,
                nextKey = response.nextCursor?.takeIf { it.isNotBlank() },
            )
        }
        else -> LoadResult.Error(toApiException())
    }
}

suspend fun <T> PagingApiResult<T>.handlePagingResultSuspend(
    onSuccess: suspend (PagedResponse<T>) -> Unit,
    onError: suspend (String) -> Unit,
) {
    when (this) {
        is ApiResult.Success -> onSuccess(data)
        else -> onError(
            when (this) {
                is ApiResult.HttpError -> message
                is ApiResult.NetworkError -> ApiUiErrors.NETWORK
                is ApiResult.Unknown -> ApiUiErrors.UNKNOWN
                is ApiResult.Success -> ""
            },
        )
    }
}

object PagingDefaults {
    const val INITIAL_PAGE = 1
    const val PAGE_SIZE = 20
}

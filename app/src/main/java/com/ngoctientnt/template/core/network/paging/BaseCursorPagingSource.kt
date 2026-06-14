package com.ngoctientnt.template.core.network.paging

import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import androidx.paging.PagingSource
import androidx.paging.PagingState

abstract class BaseCursorPagingSource<Value : Any> : PagingSource<String, Value>() {

    abstract suspend fun fetchPage(cursor: String?, loadSize: Int): PagingApiResult<Value>

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Value> {
        return fetchPage(cursor = params.key, loadSize = params.loadSize)
            .toCursorLoadResult(params.key)
    }

    override fun getRefreshKey(state: PagingState<String, Value>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}

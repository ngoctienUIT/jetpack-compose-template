package com.ngoctientnt.template.core.network.paging

import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import androidx.paging.PagingSource
import androidx.paging.PagingState

abstract class BasePagePagingSource<Value : Any> : PagingSource<Int, Value>() {

    abstract suspend fun fetchPage(page: Int, loadSize: Int): PagingApiResult<Value>

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Value> {
        val page = params.key ?: PagingDefaults.INITIAL_PAGE
        return fetchPage(page = page, loadSize = params.loadSize).toPageLoadResult(page)
    }

    override fun getRefreshKey(state: PagingState<Int, Value>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}

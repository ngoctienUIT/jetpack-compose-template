package com.ngoctientnt.template.feature.explore.data

import com.ngoctientnt.template.core.network.NetworkManager
import com.ngoctientnt.template.core.network.paging.BasePagePagingSource
import com.ngoctientnt.template.core.network.paging.PagedResponse
import com.ngoctientnt.template.core.network.paging.PagingApiResult
import com.ngoctientnt.template.core.network.paging.mapper.toPageBasedResponse
import com.ngoctientnt.template.feature.explore.data.remote.ExploreApiService
import com.ngoctientnt.template.feature.explore.data.remote.mapper.toDomain
import com.ngoctientnt.template.feature.explore.domain.ExploreItem
import javax.inject.Inject

class ExplorePagingSource @Inject constructor(
    private val networkManager: NetworkManager,
    private val exploreApiService: ExploreApiService,
) : BasePagePagingSource<ExploreItem>() {

    override suspend fun fetchPage(page: Int, loadSize: Int): PagingApiResult<ExploreItem> {
        return networkManager.safePagingApiCall {
            val pageResponse = exploreApiService.getItems(page = page, pageSize = loadSize)
                .toPageBasedResponse()

            PagedResponse(
                items = pageResponse.items.toDomain(),
                nextPage = pageResponse.nextPage,
                totalCount = pageResponse.totalCount,
            )
        }
    }
}

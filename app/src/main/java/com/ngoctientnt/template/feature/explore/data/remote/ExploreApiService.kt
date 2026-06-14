package com.ngoctientnt.template.feature.explore.data.remote

import com.ngoctientnt.template.core.network.paging.dto.PagedResponseDto
import com.ngoctientnt.template.feature.explore.data.remote.dto.ExploreItemDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ExploreApiService {

    @GET("explore/items")
    suspend fun getItems(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
    ): PagedResponseDto<ExploreItemDto>
}

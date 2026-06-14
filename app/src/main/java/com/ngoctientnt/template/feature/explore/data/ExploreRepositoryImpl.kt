package com.ngoctientnt.template.feature.explore.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ngoctientnt.template.core.network.paging.PagingDefaults
import com.ngoctientnt.template.feature.explore.domain.ExploreItem
import com.ngoctientnt.template.feature.explore.domain.repository.ExploreRepository
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.flow.Flow

class ExploreRepositoryImpl @Inject constructor(
    private val explorePagingSourceProvider: Provider<ExplorePagingSource>,
) : ExploreRepository {

    override fun getExploreItemsPaged(): Flow<PagingData<ExploreItem>> = Pager(
        config = PagingConfig(
            pageSize = PagingDefaults.PAGE_SIZE,
            initialLoadSize = PagingDefaults.PAGE_SIZE,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = { explorePagingSourceProvider.get() },
    ).flow
}

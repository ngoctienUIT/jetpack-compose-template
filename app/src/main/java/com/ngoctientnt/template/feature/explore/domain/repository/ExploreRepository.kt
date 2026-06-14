package com.ngoctientnt.template.feature.explore.domain.repository

import androidx.paging.PagingData
import com.ngoctientnt.template.feature.explore.domain.ExploreItem
import kotlinx.coroutines.flow.Flow

interface ExploreRepository {
    fun getExploreItemsPaged(): Flow<PagingData<ExploreItem>>
}

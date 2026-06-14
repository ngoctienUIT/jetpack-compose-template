package com.ngoctientnt.template.feature.explore.domain.usecase

import androidx.paging.PagingData
import com.ngoctientnt.template.feature.explore.domain.ExploreItem
import com.ngoctientnt.template.feature.explore.domain.repository.ExploreRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetExploreItemsUseCase @Inject constructor(
    private val repository: ExploreRepository,
) {
    operator fun invoke(): Flow<PagingData<ExploreItem>> = repository.getExploreItemsPaged()
}

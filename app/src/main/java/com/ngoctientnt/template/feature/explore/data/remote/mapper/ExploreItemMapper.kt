package com.ngoctientnt.template.feature.explore.data.remote.mapper

import com.ngoctientnt.template.feature.explore.data.remote.dto.ExploreItemDto
import com.ngoctientnt.template.feature.explore.domain.ExploreItem

fun ExploreItemDto.toDomain(): ExploreItem = ExploreItem(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl,
)

fun List<ExploreItemDto>.toDomain(): List<ExploreItem> = map { it.toDomain() }

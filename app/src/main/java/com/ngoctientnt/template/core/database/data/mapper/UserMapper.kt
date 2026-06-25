package com.ngoctientnt.template.core.database.data.mapper

import com.ngoctientnt.template.core.database.domain.model.User
import com.ngoctientnt.template.data.local.entity.UserEntity

fun UserEntity.toDomain(): User = User(
    id = id,
    name = name,
    createdAt = createdAt,
)

fun User.toEntity(): UserEntity = UserEntity(
    id = id,
    name = name,
    createdAt = createdAt,
)

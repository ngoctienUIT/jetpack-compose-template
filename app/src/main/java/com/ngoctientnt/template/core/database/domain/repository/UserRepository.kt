package com.ngoctientnt.template.core.database.domain.repository

import androidx.paging.PagingData
import com.ngoctientnt.template.core.database.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeAll(): Flow<List<User>>
    fun getUsersPaged(): Flow<PagingData<User>>
    suspend fun save(user: User): Result<Unit>
    suspend fun delete(user: User): Result<Unit>
    suspend fun replaceAll(users: List<User>): Result<Unit>
}

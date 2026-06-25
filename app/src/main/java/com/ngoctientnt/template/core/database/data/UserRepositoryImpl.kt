package com.ngoctientnt.template.core.database.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.ngoctientnt.template.core.database.data.local.UserLocalDataSource
import com.ngoctientnt.template.core.database.data.mapper.toDomain
import com.ngoctientnt.template.core.database.data.mapper.toEntity
import com.ngoctientnt.template.core.database.domain.model.User
import com.ngoctientnt.template.core.database.domain.repository.UserRepository
import com.ngoctientnt.template.core.network.paging.PagingDefaults
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
) : UserRepository {

    override fun observeAll(): Flow<List<User>> =
        userLocalDataSource.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getUsersPaged(): Flow<PagingData<User>> = Pager(
        config = PagingConfig(
            pageSize = PagingDefaults.PAGE_SIZE,
            initialLoadSize = PagingDefaults.PAGE_SIZE,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = { userLocalDataSource.pagingSource() },
    ).flow.map { pagingData ->
        pagingData.map { entity -> entity.toDomain() }
    }

    override suspend fun save(user: User): Result<Unit> = runCatching {
        userLocalDataSource.insert(user.toEntity())
    }

    override suspend fun delete(user: User): Result<Unit> = runCatching {
        userLocalDataSource.delete(user.toEntity())
    }

    override suspend fun replaceAll(users: List<User>): Result<Unit> = runCatching {
        userLocalDataSource.replaceAll(users.map { it.toEntity() })
    }
}

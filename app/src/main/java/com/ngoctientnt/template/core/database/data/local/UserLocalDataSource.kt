package com.ngoctientnt.template.core.database.data.local

import androidx.paging.PagingSource
import com.ngoctientnt.template.data.local.dao.UserDao
import com.ngoctientnt.template.data.local.entity.UserEntity
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class UserLocalDataSource @Inject constructor(
    private val userDao: UserDao,
) {
    fun observeAll(): Flow<List<UserEntity>> = userDao.observeAll()

    fun pagingSource(): PagingSource<Int, UserEntity> = userDao.pagingSource()

    suspend fun getById(id: Int): UserEntity? = userDao.getById(id)

    suspend fun insert(entity: UserEntity) = userDao.insert(entity)

    suspend fun update(entity: UserEntity) = userDao.update(entity)

    suspend fun delete(entity: UserEntity) = userDao.delete(entity)

    suspend fun deleteAll() = userDao.deleteAll()

    suspend fun replaceAll(entities: List<UserEntity>) = userDao.replaceAll(entities)
}

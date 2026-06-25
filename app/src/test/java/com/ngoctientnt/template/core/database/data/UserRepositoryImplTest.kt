package com.ngoctientnt.template.core.database.data

import androidx.paging.PagingSource
import com.ngoctientnt.template.core.database.data.local.UserLocalDataSource
import com.ngoctientnt.template.core.database.data.mapper.toEntity
import com.ngoctientnt.template.core.database.domain.model.User
import com.ngoctientnt.template.data.local.dao.UserDao
import com.ngoctientnt.template.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest {

    private lateinit var fakeUserDao: FakeUserDao
    private lateinit var repository: UserRepositoryImpl

    @Before
    fun setUp() {
        fakeUserDao = FakeUserDao()
        repository = UserRepositoryImpl(UserLocalDataSource(fakeUserDao))
    }

    @Test
    fun observeAll_mapsEntitiesToDomain() = runTest {
        fakeUserDao.setEntities(
            listOf(UserEntity(id = 1, name = "Alice", createdAt = 1000L)),
        )

        val users = repository.observeAll().first()

        assertEquals(1, users.size)
        assertEquals(User(id = 1, name = "Alice", createdAt = 1000L), users.first())
    }

    @Test
    fun save_insertsMappedEntity() = runTest {
        val result = repository.save(User(name = "Alice", createdAt = 1000L))

        assertTrue(result.isSuccess)
        assertEquals("Alice", fakeUserDao.lastInserted?.name)
    }

    @Test
    fun save_returnsFailureWhenInsertFails() = runTest {
        fakeUserDao.shouldFailOnInsert = true

        val result = repository.save(User(name = "Alice"))

        assertTrue(result.isFailure)
    }

    @Test
    fun delete_removesMappedEntity() = runTest {
        val user = User(id = 1, name = "Alice", createdAt = 1000L)

        val result = repository.delete(user)

        assertTrue(result.isSuccess)
        assertEquals(user.toEntity(), fakeUserDao.lastDeleted)
    }

    private class FakeUserDao : UserDao {
        private val entities = MutableStateFlow<List<UserEntity>>(emptyList())

        var lastInserted: UserEntity? = null
        var lastDeleted: UserEntity? = null
        var shouldFailOnInsert = false

        fun setEntities(value: List<UserEntity>) {
            entities.value = value
        }

        override fun observeAll(): Flow<List<UserEntity>> = entities

        override fun pagingSource(): PagingSource<Int, UserEntity> {
            error("Not implemented in fake")
        }

        override suspend fun getById(id: Int): UserEntity? = entities.value.find { it.id == id }

        override suspend fun insert(entity: UserEntity) {
            if (shouldFailOnInsert) error("Insert failed")
            lastInserted = entity
            entities.value = entities.value + entity.copy(id = entities.value.size + 1)
        }

        override suspend fun update(entity: UserEntity) {
            entities.value = entities.value.map { if (it.id == entity.id) entity else it }
        }

        override suspend fun delete(entity: UserEntity) {
            lastDeleted = entity
            entities.value = entities.value.filterNot { it.id == entity.id }
        }

        override suspend fun deleteAll() {
            entities.value = emptyList()
        }

        override suspend fun replaceAll(entities: List<UserEntity>) {
            deleteAll()
            entities.forEach { insert(it) }
        }
    }
}

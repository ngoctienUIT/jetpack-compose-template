package com.ngoctientnt.template.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ngoctientnt.template.data.local.dao.UserDao
import com.ngoctientnt.template.data.local.entity.UserEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDao = database.userDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insert_and_getById_returnsEntity() = runTest {
        userDao.insert(UserEntity(name = "Alice"))

        val users = userDao.observeAll().first()
        assertEquals(1, users.size)

        val saved = userDao.getById(users.first().id)
        assertEquals("Alice", saved?.name)
    }

    @Test
    fun observeAll_emitsInsertedUsers() = runTest {
        userDao.insert(UserEntity(name = "Alice"))
        userDao.insert(UserEntity(name = "Bob"))

        val users = userDao.observeAll().first()

        assertEquals(2, users.size)
        assertTrue(users.any { it.name == "Alice" })
        assertTrue(users.any { it.name == "Bob" })
    }

    @Test
    fun update_modifiesExistingUser() = runTest {
        userDao.insert(UserEntity(name = "Alice"))
        val saved = userDao.observeAll().first().first()

        userDao.update(saved.copy(name = "Alice Updated"))

        val updated = userDao.getById(saved.id)
        assertEquals("Alice Updated", updated?.name)
    }

    @Test
    fun delete_removesUser() = runTest {
        userDao.insert(UserEntity(name = "Alice"))
        val saved = userDao.observeAll().first().first()

        userDao.delete(saved)

        assertNull(userDao.getById(saved.id))
        assertTrue(userDao.observeAll().first().isEmpty())
    }

    @Test
    fun deleteAll_clearsTable() = runTest {
        userDao.insert(UserEntity(name = "Alice"))
        userDao.insert(UserEntity(name = "Bob"))

        userDao.deleteAll()

        assertTrue(userDao.observeAll().first().isEmpty())
    }

    @Test
    fun replaceAll_replacesExistingRows() = runTest {
        userDao.insert(UserEntity(name = "Alice"))
        userDao.replaceAll(listOf(UserEntity(name = "Bob"), UserEntity(name = "Carol")))

        val users = userDao.observeAll().first()
        assertEquals(2, users.size)
        assertTrue(users.none { it.name == "Alice" })
    }
}

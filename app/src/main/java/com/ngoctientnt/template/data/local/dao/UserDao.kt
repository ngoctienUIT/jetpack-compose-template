package com.ngoctientnt.template.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.ngoctientnt.template.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query(
        "SELECT * FROM ${UserEntity.TABLE_NAME} " +
            "ORDER BY ${UserEntity.COLUMN_CREATED_AT} DESC",
    )
    fun observeAll(): Flow<List<UserEntity>>

    @Query(
        "SELECT * FROM ${UserEntity.TABLE_NAME} " +
            "ORDER BY ${UserEntity.COLUMN_CREATED_AT} DESC",
    )
    fun pagingSource(): PagingSource<Int, UserEntity>

    @Query(
        "SELECT * FROM ${UserEntity.TABLE_NAME} " +
            "WHERE ${UserEntity.COLUMN_ID} = :id",
    )
    suspend fun getById(id: Int): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: UserEntity)

    @Update
    suspend fun update(entity: UserEntity)

    @Delete
    suspend fun delete(entity: UserEntity)

    @Query("DELETE FROM ${UserEntity.TABLE_NAME}")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAll(entities: List<UserEntity>) {
        deleteAll()
        entities.forEach { insert(it) }
    }
}

package com.ngoctientnt.template.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Template Room entity for local user storage.
 *
 * TODO: Replace or extend this entity for your use case.
 */
@Entity(
    tableName = UserEntity.TABLE_NAME,
    indices = [Index(value = [UserEntity.COLUMN_CREATED_AT])],
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID)
    val id: Int = 0,
    @ColumnInfo(name = COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = COLUMN_CREATED_AT)
    val createdAt: Long = System.currentTimeMillis(),
) {
    companion object {
        const val TABLE_NAME = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_CREATED_AT = "createdAt"
    }
}

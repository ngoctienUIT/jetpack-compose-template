package com.ngoctientnt.template.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ngoctientnt.template.data.local.dao.UserDao
import com.ngoctientnt.template.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = AppDatabase.DATABASE_VERSION,
    exportSchema = true,
    // autoMigrations = [AutoMigration(from = 1, to = 2)],
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "app_database"
    }
}

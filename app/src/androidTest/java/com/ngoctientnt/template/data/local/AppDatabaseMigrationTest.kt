package com.ngoctientnt.template.data.local

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ngoctientnt.template.data.local.entity.UserEntity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseMigrationTest {

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java,
    )

    @Test
    fun migrateFrom1_containsExpectedSchema() {
        helper.createDatabase(TEST_DB, 1).apply {
            execSQL(
                "INSERT INTO ${UserEntity.TABLE_NAME} " +
                    "(${UserEntity.COLUMN_NAME}, ${UserEntity.COLUMN_CREATED_AT}) " +
                    "VALUES ('Alice', 1000)",
            )
            close()
        }

        helper.runMigrationsAndValidate(
            TEST_DB,
            1,
            true,
            *ManualMigrations.ALL,
        )
    }

    // TODO: When bumping to version 2, add:
    // @Test
    // fun migrateFrom1To2() {
    //     helper.createDatabase(TEST_DB, 1).apply { close() }
    //     helper.runMigrationsAndValidate(TEST_DB, 2, true, *ManualMigrations.ALL)
    // }

    companion object {
        private const val TEST_DB = "migration-test"
    }
}

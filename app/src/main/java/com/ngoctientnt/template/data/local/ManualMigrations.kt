package com.ngoctientnt.template.data.local

import androidx.room.migration.Migration

object ManualMigrations {

    val ALL: Array<Migration> = arrayOf(
        // MIGRATION_2_3,
    )

    // TODO: Add manual migrations only when AutoMigration cannot handle the schema change.
    // val MIGRATION_2_3 = migration(2, 3) { db ->
    //     db.execSQL("...")
    // }
}

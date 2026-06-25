package com.ngoctientnt.template.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ngoctientnt.template.BuildConfig
import com.ngoctientnt.template.core.logging.AppLogger
import com.ngoctientnt.template.data.local.AppDatabase
import com.ngoctientnt.template.data.local.ManualMigrations
import com.ngoctientnt.template.data.local.dao.UserDao
import com.ngoctientnt.template.data.local.security.DatabasePassphraseProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        passphraseProvider: DatabasePassphraseProvider,
    ): AppDatabase {
        val passphrase = if (!BuildConfig.DEBUG) {
            passphraseProvider.getPassphrase()
        } else {
            null
        }

        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME,
        ).apply {
            if (passphrase != null) {
                try {
                    openHelperFactory(SupportOpenHelperFactory(passphrase))
                } finally {
                    passphrase.fill(0)
                }
            }
            if (BuildConfig.DEBUG) {
                fallbackToDestructiveMigration(dropAllTables = true)
            } else {
                addMigrations(*ManualMigrations.ALL)
            }
            addCallback(
                object : RoomDatabase.Callback() {
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        AppLogger.d(TAG, "AppDatabase opened (version=${db.version})")
                    }
                },
            )
        }.build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    private const val TAG = "DatabaseModule"
}

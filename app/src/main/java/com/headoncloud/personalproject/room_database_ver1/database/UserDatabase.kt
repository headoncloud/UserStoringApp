package com.headoncloud.personalproject.room_database_ver1.database

import android.app.Application
import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.headoncloud.personalproject.room_database_ver1.User

@Database(
    entities = [User::class],
    version = 2)
abstract class UserDatabase : RoomDatabase() {
        companion object {
            val migration_from_1_to_2 = object : Migration(1, 2){
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL("ALTER TABLE user ADD COLUMN birth Text")
                }

            }

            private const val DATABASE_NAME = "user.db"
            private var instance: UserDatabase? = null

            @Synchronized
            fun getInstance(context: Context): UserDatabase {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java,
                        DATABASE_NAME
                    )
                        .allowMainThreadQueries()
                        .addMigrations(migrations = arrayOf(migration_from_1_to_2))
                        .build()
                }
                return instance!!
            }
        }

    abstract fun userDao(): UserDAOs

}
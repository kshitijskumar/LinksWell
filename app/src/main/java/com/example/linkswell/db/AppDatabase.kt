package com.example.linkswell.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.linkswell.db.AppDatabase.Companion.VERSION
import com.example.linkswell.db.dao.LinksDao
import com.example.linkswell.db.entity.LinkEntity

@Database(
    entities = [LinkEntity::class],
    version = VERSION,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun linksDao(): LinksDao

    companion object {
        const val VERSION = 1
        private const val APP_DB_NAME = "app_db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    APP_DB_NAME,
                )
                    .build().also {
                        INSTANCE = it
                    }
            }
        }
    }
}
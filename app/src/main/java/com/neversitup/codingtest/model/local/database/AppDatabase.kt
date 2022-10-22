package com.neversitup.codingtest.model.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import com.neversitup.codingtest.model.local.database.dao.RecordDao
import com.neversitup.codingtest.model.local.database.entity.Record

@Database(
    entities = [Record::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getRecordDao() : RecordDao

    companion object {
        @Volatile
        private var instance : AppDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context : Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context : Context) =
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "record_database.db"
                ).build()
    }
}
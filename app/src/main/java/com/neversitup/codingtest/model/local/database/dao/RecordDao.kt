package com.neversitup.codingtest.model.local.database.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.neversitup.codingtest.model.local.database.entity.Record

@Dao
interface RecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRecord(record: Record) : Long

    @Query("SELECT * FROM records")
    suspend fun getRecords() : List<Record>

}
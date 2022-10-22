package com.neversitup.codingtest.repository

import com.neversitup.codingtest.model.local.database.AppDatabase
import com.neversitup.codingtest.model.local.database.entity.Record
import com.neversitup.codingtest.network.RetrofitInstance

class MainRepository(
        val db : AppDatabase
) {
    suspend fun getCurrentPricesForBTC() = RetrofitInstance.api.getCurrentPricesForBTC()

    suspend fun getRecordLists() = db.getRecordDao().getRecords()

    suspend fun upsert(record : Record) = db.getRecordDao().upsertRecord(record)
}

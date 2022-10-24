package com.neversitup.codingtest.model.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.sql.Timestamp

@Entity(tableName = "records")
data class Record(
        @PrimaryKey(autoGenerate = true)
        val id: Int? = 0,
        @ColumnInfo(name = "time_stamp")
        val timestamp : String,
        @ColumnInfo(name = "usd_value")
        val usdValue: String,
        @ColumnInfo(name = "gbp_value")
        val gbpValue: String,
        @ColumnInfo(name = "eur_value")
        val eurValue: String,
) : Serializable
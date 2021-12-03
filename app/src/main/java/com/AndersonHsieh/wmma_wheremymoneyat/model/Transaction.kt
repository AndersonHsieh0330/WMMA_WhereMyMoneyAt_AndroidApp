package com.AndersonHsieh.wmma_wheremymoneyat.model;

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

@Entity(tableName = "transactions")
data class Transaction(
    //room
    @PrimaryKey(autoGenerate = true)

    //the id in postgreSQL is generated in the API code
    val id: Long,

    //retrofit
    @SerializedName("name")
    var name: String,

    //room
    @ColumnInfo(name = "amount")

    //retrofit
    @SerializedName("amount")
    var amount: Double,

    //room
    @ColumnInfo(name = "time")

    //retrofit
    @SerializedName("time")
    var time: String
)

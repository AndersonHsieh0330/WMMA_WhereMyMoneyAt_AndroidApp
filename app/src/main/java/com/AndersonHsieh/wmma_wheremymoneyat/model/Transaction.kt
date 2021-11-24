package com.AndersonHsieh.wmma_wheremymoneyat.model;

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Transaction(val id:Long,
                       @SerializedName("name") var name:String,
                       @SerializedName("amount")
                       var amount: Double,
                       @SerializedName("timeStamp")
                       var timeStamp:LocalDateTime) {


}

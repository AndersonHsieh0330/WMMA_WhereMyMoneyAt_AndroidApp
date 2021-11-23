package com.AndersonHsieh.wmma_wheremymoneyat.model;

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

class Transaction(id:Long, name:String, amount: Double, timeStamp:LocalDateTime) {

    var id: Long = id

    @SerializedName("name")
    var name: String = name

    @SerializedName("amount")
    //actual id from the json response corresponding to this field is "amount"
    var amount: Double = amount

    @SerializedName("timeStamp")
    var timeStamp: LocalDateTime = timeStamp

}

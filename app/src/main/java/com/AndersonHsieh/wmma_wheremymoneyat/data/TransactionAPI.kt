package com.AndersonHsieh.wmma_wheremymoneyat.data

import com.AndersonHsieh.wmma_wheremymoneyat.model.Transaction
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TransactionAPI {

    @GET("?")
    fun getTransactions(@Query("all")isSelectedAll: Boolean,
                        @Query("from")from:String,
                        @Query("to")to:String):Call<List<Transaction>>;
}
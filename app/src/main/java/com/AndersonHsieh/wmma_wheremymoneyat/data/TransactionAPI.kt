package com.AndersonHsieh.wmma_wheremymoneyat.data

import com.AndersonHsieh.wmma_wheremymoneyat.model.Transaction
import retrofit2.Call
import retrofit2.http.GET

interface TransactionAPI {

    @GET("?all=true&from=2021-11-08&to=2021-11-09")
    fun getTransactions():Call<List<Transaction>>;
}
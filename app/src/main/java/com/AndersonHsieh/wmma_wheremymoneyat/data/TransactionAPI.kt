package com.AndersonHsieh.wmma_wheremymoneyat.data

import com.AndersonHsieh.wmma_wheremymoneyat.model.Transaction
import retrofit2.Call
import retrofit2.http.GET

interface TransactionAPI {

    @GET()
    fun getTransactions():Call<List<Transaction>>;
}
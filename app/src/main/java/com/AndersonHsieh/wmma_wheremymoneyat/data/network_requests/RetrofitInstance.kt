package com.AndersonHsieh.wmma_wheremymoneyat.data.network_requests

import com.AndersonHsieh.wmma_wheremymoneyat.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    //the lazy block is only executed on first time use of the variable
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.WMMA_API_BASEURI)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiAcessPoint: TransactionAPI by lazy {
        retrofit.create(TransactionAPI::class.java);
    }
}
package com.AndersonHsieh.wmma_wheremymoneyat.data

import com.AndersonHsieh.wmma_wheremymoneyat.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitInstance {

    //the lazy block is only executed on first time use of the variable
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.WMMA_API_BASEURI)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiAcessPoint by lazy {
        retrofit.create(TransactionAPI::class.java);
    }
}
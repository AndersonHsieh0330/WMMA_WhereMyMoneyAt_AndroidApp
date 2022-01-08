package com.AndersonHsieh.wmma_wheremymoneyat.data.network_requests

import android.net.TrafficStats
import com.AndersonHsieh.wmma_wheremymoneyat.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
        //the lazy block is only executed on first time use of the variable

        private val retrofit by lazy {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            Retrofit.Builder()
                .baseUrl(Constants.WMMA_API_BASEURI)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val apiAccessPoint: TransactionAPI by lazy{
            retrofit.create(TransactionAPI::class.java)
        }
}
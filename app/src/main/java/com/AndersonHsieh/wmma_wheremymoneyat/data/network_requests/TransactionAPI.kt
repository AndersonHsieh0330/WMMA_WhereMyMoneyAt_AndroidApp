package com.AndersonHsieh.wmma_wheremymoneyat.data.network_requests

import com.AndersonHsieh.wmma_wheremymoneyat.model.Transaction
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface TransactionAPI {

    @GET("?")
    fun getTransactions(
        @Query("all") isSelectedAll: Boolean,
        @Query("from") from: String,
        @Query("to") to: String
    ): Call<List<Transaction>>

    @DELETE("{id}")
    fun deleteTransaction(@Path("id") id: Long): Call<ResponseBody>

    @PUT("?")
    fun putTransactions(
        @Query("name") name: String,
        @Query("amount") amount: Double
    ): Call<ResponseBody>

    @POST("{id}?")
    fun postTransactions(
        @Path("id") id: Long,
        @Query("name") name: String,
        @Query("amount") amount: Double
    ): Call<ResponseBody>
}
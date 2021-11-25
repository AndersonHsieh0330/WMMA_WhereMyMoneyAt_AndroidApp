package com.AndersonHsieh.wmma_wheremymoneyat.data

import android.util.Log
import com.AndersonHsieh.wmma_wheremymoneyat.model.Transaction
import com.AndersonHsieh.wmma_wheremymoneyat.model.TransactionDAO
import retrofit2.Call

//because parameter(transactionDAO) is needed to initialize TransactionRepository
//we use the singleton pattern provided by Google's architecture components sample code instead of the "object" keyword
class TransactionRepository private constructor(private val transactionDAO:TransactionDAO){
    companion object{
        @Volatile
        private var instance: TransactionRepository? = null
        fun getInstance(transactionDAO: TransactionDAO) = instance ?: TransactionRepository(transactionDAO)
    }

    fun getTransaction(): Call<List<Transaction>> {
        Log.d("andydebug", "API GET request sent ")
        return RetrofitInstance.apiAcessPoint.getTransactions();
    }

}
//javascript object notation
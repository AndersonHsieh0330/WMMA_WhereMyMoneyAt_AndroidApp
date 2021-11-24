package com.AndersonHsieh.wmma_wheremymoneyat.data

import com.AndersonHsieh.wmma_wheremymoneyat.model.Transaction
import com.AndersonHsieh.wmma_wheremymoneyat.model.TransactionDAO

//because parameter(transactionDAO) is needed to initialize TransactionRepository
//we use the singleton pattern provided by Google's architecture components sample code instead of the "object" keyword
class TransactionRepository private constructor(private val transactionDAO:TransactionDAO){
    companion object{
        @Volatile
        private var instance: TransactionRepository? = null
        fun getInstance(transactionDAO: TransactionDAO) = instance ?: TransactionRepository(transactionDAO)
    }

    fun getTransaction():List<Transaction>{
        return RetrofitInstance.apiAcessPoint.getTransactions();
    }

}
//javascript object notation
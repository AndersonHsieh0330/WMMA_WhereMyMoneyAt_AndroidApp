package com.AndersonHsieh.wmma_wheremymoneyat.data

import com.AndersonHsieh.wmma_wheremymoneyat.model.Transaction
import com.AndersonHsieh.wmma_wheremymoneyat.model.TransactionDAO

//singleton pattern
class TransactionRepository private constructor(private val transactionDAO:TransactionDAO){
    companion object{
        @Volatile
        private var instance: TransactionRepository? = null
        fun getInstance() = instance ?: TransactionRepository
    }

    fun getTransaction():List<Transaction>{
        return RetrofitInstance.apiAcessPoint.getTransactions();
    }

}
//javascript object notation
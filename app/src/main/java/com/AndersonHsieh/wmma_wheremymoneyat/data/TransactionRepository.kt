package com.AndersonHsieh.wmma_wheremymoneyat.data

import com.AndersonHsieh.wmma_wheremymoneyat.model.TransactionDAO

//singleton pattern
class TransactionRepository private constructor(private val transactionDAO:TransactionDAO){
    companion object{
        @Volatile
        private var instance: TransactionRepository? = null
        fun getInstance(transactionDAO: TransactionDAO) = instance ?: TransactionRepository(transactionDAO)
    }


}
//javascript object notation
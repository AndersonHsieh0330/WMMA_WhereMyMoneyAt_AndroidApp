package com.AndersonHsieh.wmma_wheremymoneyat.data.persistent

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.AndersonHsieh.wmma_wheremymoneyat.model.Transaction

@Dao
interface TransactionDAO {
    //caching strategy as below
    //A UNIT of request is organized by a collection of transactions such as All, 2021-01, 2021-02....2030-12
    //once a GET request is made, all the transactions from TransactionAPI is stored into SQLite
    //once a list of transaction is fetched, all following edits to remote db is replicated for local db(PUT, POST, DELETE)
    //once user goes offline, user can see the latest updated data from SQLite
    //but is not allowed to make a new GET request from the API

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    //means that if there are duplicated transactions ready to be inserted
    //just ignore it
    fun addTransaction(transaction: Transaction)


    @Query(value = "SELECT * FROM transactions ORDER BY time")
    fun readAllTransactions():List<Transaction>

//    This local SQLite db only stores the current collection of Transactions
//    Thus no filtering by time needed when we want to access the cache
//    @Query(value = "SELECT * FROM transactions WHERE time = :year-:month ORDER BY time")
//    fun readTransactionsByYearMonth(year:String, month:String):List<Transaction>

    @Query(value = "UPDATE transactions SET name = :name, amount = :amount WHERE id = :id")
    fun updateTransaction(id:Long, name:String, amount:Double)

    @Query(value = "DELETE FROM transactions WHERE id = :id")
    fun deleteTransaction(id:Long)

    @Query(value = "DELETE FROM transactions;")
    fun clearDB()


}
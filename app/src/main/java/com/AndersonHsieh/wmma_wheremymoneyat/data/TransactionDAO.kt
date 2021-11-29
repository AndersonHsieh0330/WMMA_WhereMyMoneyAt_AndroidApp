package com.AndersonHsieh.wmma_wheremymoneyat.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.AndersonHsieh.wmma_wheremymoneyat.model.Transaction

@Dao
interface TransactionDAO {
    //caching strategy as below
    //each request to the TransactionAPI is stored into SQlite
    //A unit of request is by a collection of transactions such as All, 2021-01, 2021-02....2030-12
    //once a list of transaction is fetched, all of the transactions are stored and the unit is marked as "Sync"
    //once a modification is made to the unit of transactions, mark the unit as "Not-Sync"
    //if user request a unit that is "Sync", just SELECT from SQLite instead of fetching from TransactionAPI
    //if user request a unit that is "Not-Sync", fetch from TransactionAPI and stored the unit in SQlite

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    //means that if there are duplicated transactions ready to be inserted
    //just ignore it
    fun addTransaction(transaction: Transaction)


    //this is a special unit
    //if this unit is "Sync", all other units will be fetched from SQLite instead of TransactionAPI
    @Query(value = "SELECT * FROM transactions ORDER BY time")
    fun readAllTransactions():LiveData<List<Transaction>>


    @Query(value = "SELECT * FROM transactions Where time = :year-:month ORDER BY time ")
    fun readTransactionsByYearMonth(year:String, month:String):LiveData<List<Transaction>>

    @Delete
    fun deleteTransaction(transaction: Transaction)


}
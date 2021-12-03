package com.AndersonHsieh.wmma_wheremymoneyat.data.persistent

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.AndersonHsieh.wmma_wheremymoneyat.model.Transaction

@Database(entities = [Transaction::class], version = 1, exportSchema = false)
abstract class TransactionDataBase():RoomDatabase() {

    abstract fun transactionDAO() : TransactionDAO

    companion object{

        @Volatile
        private var instance: TransactionDataBase? = null

        fun getInstance(context:Context): TransactionDataBase {
            val tempInstance = instance
            //make a copy so the returned instance(tempInstance) is immutable
            if (tempInstance != null) {
                return tempInstance

            }
            synchronized(this) {
                //everything in this block will be protected from concurrent execute by multiple threads
                return Room.databaseBuilder(
                    context.applicationContext,
                    TransactionDataBase::class.java,
                    "transaction_database"
                ).allowMainThreadQueries().build()
            }
        }
    }
}
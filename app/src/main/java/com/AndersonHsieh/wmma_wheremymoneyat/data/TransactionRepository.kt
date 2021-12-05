package com.AndersonHsieh.wmma_wheremymoneyat.data

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import com.AndersonHsieh.wmma_wheremymoneyat.data.network_requests.RetrofitInstance
import com.AndersonHsieh.wmma_wheremymoneyat.data.persistent.TransactionDataBase
import com.AndersonHsieh.wmma_wheremymoneyat.model.Transaction
import com.AndersonHsieh.wmma_wheremymoneyat.util.Constants
import okhttp3.ResponseBody
import retrofit2.Call
import java.time.LocalDate

//because parameter(transactionDAO) is needed to initialize TransactionRepository
//we use the singleton pattern provided by Google's architecture components sample code instead of the "object" keyword
class TransactionRepository private constructor() {

    companion object {
        @Volatile
        private var instance: TransactionRepository? = null
        fun getInstance(): TransactionRepository {
            val tempInstance = instance
            //make a copy so the returned instance(tempInstance) is immutable
            if (tempInstance != null) {
                return tempInstance

            }
            synchronized(this) {
                //everything in this block will be protected from concurrent execute by multiple threads
                return TransactionRepository()
            }
        }
    }

    fun getTransaction(context: Context): Call<List<Transaction>> {
        val sp = context.getSharedPreferences(
            Constants.SHAREDPREFERENCES_Transaction_TAG,
            Context.MODE_PRIVATE
        )
        Log.d(Constants.LOGGING_TAG, "API GET request sent ")

        //retrieve selected year month directly from sharedpreference
        val arrayOfDates = convertToValidTimeIntervalString(
            year = sp.getInt(Constants.SP_YEAR, 2021),
            month = sp.getInt(Constants.SP_MONTH, 1)
        )

        return RetrofitInstance.apiAcessPoint.getTransactions(
            sp.getBoolean(
                Constants.SP_SELECT_ALL,
                true
            ), arrayOfDates[0], arrayOfDates[1]
        )
    }

    fun deleteTransaction(context: Context, id: Long): Call<ResponseBody> {
        //Note that SQLite is only storing the current COLLECTION of transactions
        Log.d(Constants.LOGGING_TAG, "deleteTransaction from local db")
        TransactionDataBase.getInstance(context).transactionDAO().deleteTransaction(id)

        Log.d(Constants.LOGGING_TAG, "API DELETE request sent ")
        return RetrofitInstance.apiAcessPoint.deleteTransaction(id)
    }

    fun putTransaction(context: Context, name: String, amount: Double): Call<ResponseBody> {
        Log.d(Constants.LOGGING_TAG, "API PUT request sent ")

        //This caching logic is very inefficient and will be updated later
        //However this is done because the id column of remote DB(PostgreSQL)
        //and local db(SQLite) must be aligned to be equal
        //The id value in PostgreSQL db is generated in the spring RestAPI code
        //thus to retrieve the correct id, just reload the entire collection

        Log.d(Constants.LOGGING_TAG, "clean local db")
        TransactionDataBase.getInstance(context).transactionDAO().clearDB()
        getCachedTransaction(context)

        return RetrofitInstance.apiAcessPoint.putTransactions(name, amount)
    }

    fun postTransaction(
        context: Context,
        id: Long,
        name: String,
        amount: Double
    ): Call<ResponseBody> {
        Log.d(Constants.LOGGING_TAG, "API GET request sent ")
        Log.d(Constants.LOGGING_TAG, "UpdateTransaction in local db")

        //Note that SQLite is only storing the current COLLECTION of transactions
        TransactionDataBase.getInstance(context).transactionDAO()
            .updateTransaction(id, name, amount)
        return RetrofitInstance.apiAcessPoint.postTransactions(id, name, amount)
    }

    fun getCachedTransaction(context: Context): List<Transaction> {
        val sp = context.getSharedPreferences(
            Constants.SHAREDPREFERENCES_Transaction_TAG,
            Context.MODE_PRIVATE
        )
        Log.d(Constants.LOGGING_TAG, "readAllTransaction in local db")

        return TransactionDataBase.getInstance(context).transactionDAO().readAllTransactions()
    }

    fun cacheTransactions(context: Context, mutableList: List<Transaction>) {
        val sqliteDB = TransactionDataBase.getInstance(context).transactionDAO()
        sqliteDB.clearDB()
        Log.d(Constants.LOGGING_TAG, "clean local db")
        mutableList.forEach { sqliteDB.addTransaction(it) }
        Log.d(Constants.LOGGING_TAG, "addTransactions to local db")

    }


    fun getYearMonthFromSharedPreference(context: Context): Array<Int> {
        val sp = context.getSharedPreferences(
            Constants.SHAREDPREFERENCES_Transaction_TAG,
            Context.MODE_PRIVATE
        )
        if (!sp.contains(Constants.SP_YEAR) || !sp.contains(Constants.SP_MONTH)) {
            //only gets here when user first downloads the app and doesn't have sharedpreference initialized
            val now = LocalDate.now()
            with(sp.edit()) {
                putInt(Constants.SP_YEAR, now.year)
                putInt(Constants.SP_MONTH, now.monthValue)
                commit()
            }
        }

        //note that year goes first the in array
        //this helps when observing this array from the view level
        return arrayOf(
            sp.getInt(Constants.SP_YEAR, 2021),
            sp.getInt(Constants.SP_MONTH, 1)
        )


    }

    fun getIsSelectedAllFromSharedPreference(context: Context): Boolean {
        val sp = context.getSharedPreferences(
            Constants.SHAREDPREFERENCES_Transaction_TAG,
            Context.MODE_PRIVATE
        )
        return sp.getBoolean(Constants.SP_SELECT_ALL, true)
    }

    fun changeSelectedTimeInSharedPreference(
        year: Int,
        month: Int,
        isAll: Boolean,
        context: Context
    ) {
        val sp = context.getSharedPreferences(
            Constants.SHAREDPREFERENCES_Transaction_TAG,
            Context.MODE_PRIVATE
        )
        with(sp.edit()) {
            putInt(Constants.SP_YEAR, year)
            putInt(Constants.SP_MONTH, month)
            putBoolean(Constants.SP_SELECT_ALL, isAll)
            commit()

        }
    }

    private fun convertToValidTimeIntervalString(year: Int, month: Int): Array<String> {

        val endDate = when (month) {
            1 -> 31
            2 -> if (year % 4 == 0) 29 else 28 //check for leap year
            3 -> 31
            4 -> 30
            5 -> 31
            6 -> 30
            7 -> 31
            8 -> 31
            9 -> 30
            10 -> 31
            11 -> 30
            12 -> 31
            else -> 0
        }

        //note that the "from" date goes first
        Log.d(Constants.LOGGING_TAG, "$year-$month-31")
        return arrayOf("$year-$month-01", "$year-$month-31")
    }

    fun isConnectedToInternet(context: Context): Boolean {
        //define this method in repository for reusability
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            return true
        }
        return false
    }

}


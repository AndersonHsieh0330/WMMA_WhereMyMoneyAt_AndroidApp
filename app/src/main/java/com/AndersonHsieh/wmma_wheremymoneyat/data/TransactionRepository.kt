package com.AndersonHsieh.wmma_wheremymoneyat.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.AndersonHsieh.wmma_wheremymoneyat.model.Transaction
import com.AndersonHsieh.wmma_wheremymoneyat.util.Constants
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime

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

    fun getTransaction(context: Context): MutableList<Transaction> {
        var result = mutableListOf<Transaction>()
        val sp = context.getSharedPreferences(
            Constants.SHAREDPREFERENCES_Transaction_TAG,
            Context.MODE_PRIVATE
        )

        //retrieve selected year month directly from sharedpreference
        val arrayOfDates = convertToValidTimeIntervalString(
            year = sp.getInt(Constants.SP_YEAR, 2021),
            month = sp.getInt(Constants.SP_MONTH, 1)
        )

        if (isConnectedToInternet(context)) {
            Log.d(Constants.LOGGING_TAG, "API GET request sent ")

            RetrofitInstance.apiAcessPoint.getTransactions(
                sp.getBoolean(
                    Constants.SP_SELECT_ALL,
                    true
                ), arrayOfDates[0], arrayOfDates[1]
            ).enqueue(object : Callback<List<Transaction>> {
                override fun onResponse(
                    call: retrofit2.Call<List<Transaction>>,
                    response: Response<List<Transaction>>
                ) {
                    //convert the List<transaction> returned from API to MutableList<Transactions>
                    //to allow removing items in recyclerview
                    val mutableList = mutableListOf<Transaction>()
                    for (item in response.body()!!) {
                        mutableList.add(item)
                    }
                    mutableList.reverse()
                    //TODO(add this mutablelist ot cash)
                    result = mutableList
                }

                override fun onFailure(call: Call<List<Transaction>>, t: Throwable) {
                    result = mutableListOf(
                        Transaction(
                            -1,
                            "failed",
                            0.0,
                            LocalDateTime.now().toString()
                        )
                    )

                }

            })

            return result
        } else {
            //TODO(package this to a neew function getCachedTransactions())
            result = if (sp.getBoolean(Constants.SP_SELECT_ALL, false)) {
                TransactionDataBase.getInstance(context).transactionDAO().readAllTransactions()
            } else {
                TransactionDataBase.getInstance(context).transactionDAO()
                    .readTransactionsByYearMonth(arrayOfDates[0], arrayOfDates[1])
            }
            return result
        }
    }

    fun deleteTransaction(id: Long): Call<ResponseBody> {
        Log.d(Constants.LOGGING_TAG, "API DELETE request sent ")
        return RetrofitInstance.apiAcessPoint.deleteTransaction(id)
    }

    fun putTransaction(name: String, amount: Double): Call<ResponseBody> {
        Log.d(Constants.LOGGING_TAG, "API PUT request sent ")
        return RetrofitInstance.apiAcessPoint.putTransactions(name, amount)
    }

    fun postTransaction(id: Long, name: String, amount: Double): Call<ResponseBody> {

        return RetrofitInstance.apiAcessPoint.postTransactions(id, name, amount)
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

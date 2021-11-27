package com.AndersonHsieh.wmma_wheremymoneyat.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.AndersonHsieh.wmma_wheremymoneyat.model.Transaction
import com.AndersonHsieh.wmma_wheremymoneyat.model.TransactionDAO
import com.AndersonHsieh.wmma_wheremymoneyat.ui.main_activity.MainActivity
import com.AndersonHsieh.wmma_wheremymoneyat.util.Constants
import retrofit2.Call
import java.time.LocalDate

//because parameter(transactionDAO) is needed to initialize TransactionRepository
//we use the singleton pattern provided by Google's architecture components sample code instead of the "object" keyword
class TransactionRepository private constructor(private val transactionDAO: TransactionDAO) {

    private lateinit var sp: SharedPreferences

    companion object {

        @Volatile
        private var instance: TransactionRepository? = null
        fun getInstance(transactionDAO: TransactionDAO) =
            instance ?: TransactionRepository(transactionDAO)
    }

    fun getTransaction(): Call<List<Transaction>> {
        Log.d("andydebug", "API GET request sent ")
        //retrieve selected year month directly from sharedpreference
        val arrayOfDates = convertToValidTimeIntervalString(
            year = sp.getInt(Constants.SP_YEAR, 2021),
            month = sp.getInt(Constants.SP_MONTH, 1)
        );
        return RetrofitInstance.apiAcessPoint.getTransactions(sp.getBoolean(Constants.SP_SELECT_ALL, true), arrayOfDates[0], arrayOfDates[1]);
    }

    fun initSharedPreference(context: Context, sharedPreferenceTag: String) {
        //this method is executed in main activity(App starts)
        //sharedpreference instance is kept and the context of main activity never gets destroyed
        sp = context.getSharedPreferences(sharedPreferenceTag, Context.MODE_PRIVATE)
    }

    fun getYearMonthFromSharedPreference(): Array<Int> {
        //note that year goes first the in array
        //this helps when observing this array from the view level
        return arrayOf(
            sp.getInt(Constants.SP_YEAR, 2021),
            sp.getInt(Constants.SP_MONTH, 1)
        )

    }

    fun getIsSelectedAllFromSharedPreference(): Boolean {
        return sp.getBoolean(Constants.SP_SELECT_ALL, true)
    }

    fun changeSelectedTimeInSharedPreference(year: Int, month: Int, isAll: Boolean) {
        if (sp.contains(Constants.SP_YEAR) || sp.contains(Constants.SP_MONTH) || sp.contains(
                Constants.SP_SELECT_ALL
            )
        ) {
            with(sp.edit()) {
                putInt(Constants.SP_YEAR, year)
                putInt(Constants.SP_MONTH, month)
                putBoolean(Constants.SP_SELECT_ALL, isAll)
                commit()

            }
        } else {
            //app is first downloaded, no sharedpreference properties defined
            val now = LocalDate.now()
            with(sp.edit()) {
                putInt(Constants.SP_YEAR, now.year)
                putInt(Constants.SP_MONTH, now.monthValue)
                putBoolean(Constants.SP_SELECT_ALL, false)
                commit()
            }

        }
    }

    private fun convertToValidTimeIntervalString(year:Int, month: Int):Array<String>{

        val endDate = when(month){
            1 -> 31
            2 -> if(year%4==0)29 else 28 //check leap year
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
        return arrayOf("$year-$month-01", "$year-$month-$endDate")
    }
}

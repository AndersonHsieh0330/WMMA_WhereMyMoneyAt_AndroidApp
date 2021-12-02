package com.AndersonHsieh.wmma_wheremymoneyat.ui.main_activity

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.AndersonHsieh.wmma_wheremymoneyat.data.TransactionRepository
import com.AndersonHsieh.wmma_wheremymoneyat.model.Transaction
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

class TransactionViewModel(private val repository: TransactionRepository, application: Application) : AndroidViewModel(
    application
) {
    //this view model is shared among home fragment and main activity
    //Custom ViewModel factory is created in order to pass arguments into view model
    //use AndroidViewModel class because application context is needed for using SharedPreference in repository

    val transactions: MutableLiveData<MutableList<Transaction>> = MutableLiveData()

    val selectedYearMonth: MutableLiveData<Array<Int>> = MutableLiveData()

    val isSelectedAll: MutableLiveData<Boolean> = MutableLiveData()

    fun getTransactions() {
        transactions.value = repository.getTransaction(getApplication())
    }

    fun deleteTransactions(id:Long):Call<ResponseBody>{
        return repository.deleteTransaction(id)
    }


    fun getSelectedYearMonth() {
        selectedYearMonth.value = repository.getYearMonthFromSharedPreference(getApplication())
    }

    fun getIsSelectedAll() {
        isSelectedAll.value = repository.getIsSelectedAllFromSharedPreference(getApplication())
    }


    fun changeSelectedTimeInSharedPreference(year: Int, month: Int, isAll: Boolean) {
        repository.changeSelectedTimeInSharedPreference(year, month, isAll, getApplication());
    }

    fun isConnectedToInternet():Boolean {
        return repository.isConnectedToInternet(getApplication())
    }

}
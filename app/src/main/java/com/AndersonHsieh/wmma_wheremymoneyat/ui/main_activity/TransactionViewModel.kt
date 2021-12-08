package com.AndersonHsieh.wmma_wheremymoneyat.ui.main_activity

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.AndersonHsieh.wmma_wheremymoneyat.data.TransactionRepository
import com.AndersonHsieh.wmma_wheremymoneyat.model.Transaction
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionViewModel(private val repository: TransactionRepository, application: Application) : AndroidViewModel(
    application
) {
    //this view model is shared among home fragment and main activity
    //Custom ViewModel factory is created in order to pass arguments into view model
    //use AndroidViewModel class because application context is needed for using SharedPreference in repository

    //return a LiveData for read-only access from the views
    var transactions: MutableLiveData<List<Transaction>> = MutableLiveData()

    val selectedYearMonth: MutableLiveData<Array<Int>> = MutableLiveData()

    val isSelectedAll: MutableLiveData<Boolean> = MutableLiveData()

    fun getTransactions() {
        //this function is an exception from other data related functions done in repository layer
        if(repository.isConnectedToInternet(getApplication())){
            //implement network connection check here in viewmodel
                // due to the limitation of different data types returned by web api(Call) and SQLite
            getTransactionsFromWebAPI()
        }else{
            getCachedTransactions()
        }
    }

    fun getCachedTransactions(){
        transactions.value = repository.getCachedTransaction(getApplication())

    }

    fun cacheTransactions(context: Context, data: List<Transaction>){
        repository.cacheTransactions(context, data)
    }

    fun getTransactionsFromWebAPI(){
        repository.getTransaction(getApplication()).enqueue(object : Callback<List<Transaction>> {
            override fun onResponse(
                call: retrofit2.Call<List<Transaction>>,
                response: Response<List<Transaction>>
            ) {
                //convert the List<transaction> returned from API to MutableList<Transactions>
                //to allow removing items in recyclerview
                val mutableList = mutableListOf<Transaction>()
                for(item in response.body()!!){
                    mutableList.add(item)
                }
                transactions.value = mutableList
                cacheTransactions(getApplication(), response.body()!!)
            }

            override fun onFailure(call: Call<List<Transaction>>, t: Throwable) {
                //if somehow network request fails, due to reason other than network connectivity
                //we load the local data
                transactions.value = repository.getCachedTransaction(getApplication())
            }

        })
    }

    fun deleteTransactions(id:Long):Call<ResponseBody>{
        return repository.deleteTransaction(getApplication(), id)
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
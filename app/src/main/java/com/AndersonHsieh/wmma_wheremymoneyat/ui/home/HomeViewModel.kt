package com.AndersonHsieh.wmma_wheremymoneyat.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.AndersonHsieh.wmma_wheremymoneyat.data.TransactionRepository
import com.AndersonHsieh.wmma_wheremymoneyat.model.Transaction
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder
import java.time.LocalDateTime


class HomeViewModel(private val repository: TransactionRepository) : ViewModel() {
    //Custom ViewModel factory is created in order to pass arguments into view model

    val transactions: MutableLiveData<List<Transaction>> = MutableLiveData()

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    fun getTransactions(){
        repository.getTransaction().enqueue(object : Callback<List<Transaction>>{
            override fun onResponse(
                call: retrofit2.Call<List<Transaction>>,
                response: Response<List<Transaction>>
            ) {
                transactions.value = response.body()
            }

            override fun onFailure(call: Call<List<Transaction>>, t: Throwable) {
                transactions.value = listOf(Transaction(-1,"failed",-1.0, "RIGHTNOW"))
            }

        })
    }

}
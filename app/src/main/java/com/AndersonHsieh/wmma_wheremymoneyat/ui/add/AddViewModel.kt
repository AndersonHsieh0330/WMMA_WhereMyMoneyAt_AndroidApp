package com.AndersonHsieh.wmma_wheremymoneyat.ui.add

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.AndersonHsieh.wmma_wheremymoneyat.data.TransactionRepository
import okhttp3.ResponseBody
import retrofit2.Call

class AddViewModel(private val repository: TransactionRepository) : ViewModel() {


    fun putTransactions(name:String, amount:Double): Call<ResponseBody> {
        return repository.putTransaction(name, amount)
    }

    fun isConnectedToInternet(context:Context):Boolean{
        return repository.isConnectedToInternet(context)
    }

}
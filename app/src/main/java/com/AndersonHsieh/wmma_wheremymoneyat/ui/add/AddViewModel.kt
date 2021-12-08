package com.AndersonHsieh.wmma_wheremymoneyat.ui.add

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.AndersonHsieh.wmma_wheremymoneyat.data.TransactionRepository
import okhttp3.ResponseBody
import retrofit2.Call

class AddViewModel(private val repository: TransactionRepository, application: Application) : AndroidViewModel(
    application
) {


    fun putTransactions(name:String, amount:Double): Call<ResponseBody> {
        return repository.putTransaction(getApplication(),name, amount)
    }

    fun isConnectedToInternet(context:Context):Boolean{
        return repository.isConnectedToInternet(context)
    }

}
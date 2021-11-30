package com.AndersonHsieh.wmma_wheremymoneyat.ui.edit_activity

import androidx.lifecycle.ViewModel
import com.AndersonHsieh.wmma_wheremymoneyat.data.TransactionRepository
import okhttp3.ResponseBody
import retrofit2.Call

class EditViewModel(private val repository: TransactionRepository):ViewModel() {

    fun postTransactions(id:Long, name:String, amount:Double): Call<ResponseBody> {
        return repository.postTransaction(id, name, amount)
    }
}
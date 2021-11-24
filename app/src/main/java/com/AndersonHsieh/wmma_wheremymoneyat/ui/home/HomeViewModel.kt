package com.AndersonHsieh.wmma_wheremymoneyat.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.AndersonHsieh.wmma_wheremymoneyat.data.TransactionRepository
import com.AndersonHsieh.wmma_wheremymoneyat.model.Transaction

class HomeViewModel(private val repository: TransactionRepository) : ViewModel() {
    //Custom ViewModel factory is created in order to pass arguments into view model

    val transactions: MutableLiveData<List<Transaction>> = MutableLiveData()

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    fun getTransactions(){
        transactions.value = repository.getTransaction()
    }

}
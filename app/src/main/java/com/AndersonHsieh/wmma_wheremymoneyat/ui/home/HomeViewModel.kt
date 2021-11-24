package com.AndersonHsieh.wmma_wheremymoneyat.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.AndersonHsieh.wmma_wheremymoneyat.data.TransactionRepository

class HomeViewModel(private val repository: TransactionRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
}
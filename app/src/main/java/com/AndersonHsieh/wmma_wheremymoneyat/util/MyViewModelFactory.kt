package com.AndersonHsieh.wmma_wheremymoneyat.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.AndersonHsieh.wmma_wheremymoneyat.data.TransactionRepository
import com.AndersonHsieh.wmma_wheremymoneyat.ui.about.AboutViewModel
import com.AndersonHsieh.wmma_wheremymoneyat.ui.add.AddViewModel
import com.AndersonHsieh.wmma_wheremymoneyat.ui.home.HomeViewModel
import com.AndersonHsieh.wmma_wheremymoneyat.ui.main_activity.MainActivity
import com.AndersonHsieh.wmma_wheremymoneyat.ui.main_activity.TransactionViewModel

class MyViewModelFactory(
    private val repository: TransactionRepository
):ViewModelProvider.Factory {

    //keep this as an instance
    private val sharedTransactionViewModel:TransactionViewModel by lazy {
        TransactionViewModel(repository)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        //to avoid duplicate ViewModelFactory with specified for each fragment
        //use when clause to check for different view model requested
        return with(modelClass) {
            when {
//                isAssignableFrom(AboutViewModel::class.java) -> AboutViewModel(repository)
//                isAssignableFrom(AddViewModel::class.java) -> AddViewModel(repository)
                isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository)
                isAssignableFrom(TransactionViewModel::class.java)-> sharedTransactionViewModel

                else ->
                    throw IllegalArgumentException(
                        "Unknown ViewModel class: ${modelClass.name}")
            }

        } as T

    }

}
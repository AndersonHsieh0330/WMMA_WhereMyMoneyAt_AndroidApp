package com.AndersonHsieh.wmma_wheremymoneyat.util

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.AndersonHsieh.wmma_wheremymoneyat.data.TransactionRepository
import com.AndersonHsieh.wmma_wheremymoneyat.ui.add.AddViewModel
import com.AndersonHsieh.wmma_wheremymoneyat.ui.edit_activity.EditViewModel
import com.AndersonHsieh.wmma_wheremymoneyat.ui.main_activity.TransactionViewModel

class MyViewModelFactory(
    private val repository: TransactionRepository
) : ViewModelProvider.Factory {

    constructor(repository: TransactionRepository, application: Application) : this(repository) {
        //this constructor is used for viewModels that need application context as a parameter
        app = application
    }

    private lateinit var app: Application

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        //to avoid duplicate ViewModelFactory with specified for each fragment
        //use when clause to check for different view model requested
        return with(modelClass) {
            when {
//                isAssignableFrom(AboutViewModel::class.java) -> AboutViewModel(repository)
                isAssignableFrom(EditViewModel::class.java) -> EditViewModel(repository)
                isAssignableFrom(AddViewModel::class.java) -> AddViewModel(repository)
                isAssignableFrom(TransactionViewModel::class.java) -> TransactionViewModel(
                    repository,
                    app
                )
                else ->
                    throw IllegalArgumentException(
                        "Unknown ViewModel class: ${modelClass.name}"
                    )
            }

        } as T

    }

}
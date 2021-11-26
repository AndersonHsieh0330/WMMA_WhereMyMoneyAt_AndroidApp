package com.AndersonHsieh.wmma_wheremymoneyat.ui.main_activity

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.AndersonHsieh.wmma_wheremymoneyat.R
import com.AndersonHsieh.wmma_wheremymoneyat.data.TransactionRepository
import com.AndersonHsieh.wmma_wheremymoneyat.databinding.ActivityMainBinding
import com.AndersonHsieh.wmma_wheremymoneyat.model.TransactionDAO
import com.AndersonHsieh.wmma_wheremymoneyat.util.MyViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        navView.setupWithNavController(navController)


        val factory = MyViewModelFactory(TransactionRepository.getInstance(TransactionDAO()))

        val viewModel: TransactionViewModel by lazy {
            //only initialized on first time use of the variable "viewModel"
            ViewModelProvider(this, factory).get(TransactionViewModel::class.java)
        }

        viewModel.getTransactions()

    }
}
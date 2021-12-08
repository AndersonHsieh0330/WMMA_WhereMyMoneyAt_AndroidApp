package com.AndersonHsieh.wmma_wheremymoneyat.ui.main_activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.AndersonHsieh.wmma_wheremymoneyat.R
import com.AndersonHsieh.wmma_wheremymoneyat.R.*
import com.AndersonHsieh.wmma_wheremymoneyat.data.TransactionRepository
import com.AndersonHsieh.wmma_wheremymoneyat.databinding.ActivityMainBinding
import com.AndersonHsieh.wmma_wheremymoneyat.ui.month_picker.YearMonthPickerDialog
import com.AndersonHsieh.wmma_wheremymoneyat.util.Constants
import com.AndersonHsieh.wmma_wheremymoneyat.util.MyViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TransactionViewModel

    //views
    private lateinit var navView: BottomNavigationView
    private lateinit var monthYearPickerBTN: Button
    private lateinit var infoBTN: ImageButton
    private lateinit var favoriteBTN: ImageButton

    private var isFavorited:Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //because TransactionViewModel is shared between activity and homefragment
        //viewModel must be initialized before inflating fragment
        //else the line "by activityViewModels()" in homefragment will cause app to crash
        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(TransactionRepository.getInstance(), application)
        )[TransactionViewModel::class.java]

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.window.statusBarColor = ContextCompat.getColor(this, color.lightPinkishPurple);

        initUI()

        val navController = findNavController(id.nav_host_fragment_activity_main)
        Log.d(Constants.LOGGING_TAG, "onCreate: ${navController.currentBackStackEntry}")
        navView.setupWithNavController(navController)

        viewModel.getTransactions()
        viewModel.getSelectedYearMonth()
        viewModel.getIsSelectedAll()

        viewModel.isSelectedAll.observe(this, Observer {
            if (it) {
                binding.MainActivityYearMonthPickerBTN.text = "All"
            } else {
                //this block is only triggered when user unchecks the "select all" checkbox
                //without modifying the year and month
                val monthYear = viewModel.selectedYearMonth.value!!
                val month: String =
                    if (monthYear[1] >= 10) (monthYear[1].toString()) else "0${monthYear[1]}"
                val yearMonthDisplayed = monthYear[0].toString() + "-" + month
                binding.MainActivityYearMonthPickerBTN.text = yearMonthDisplayed
            }
        })

        viewModel.selectedYearMonth.observe(this, Observer {
            if (!viewModel.isSelectedAll.value!!) {
                //only set display button text
                val month: String = if (it[1] >= 10) (it[1].toString()) else "0${it[1]}"
                val yearMonthDisplayed = it[0].toString() + "-" + month
                binding.MainActivityYearMonthPickerBTN.text = yearMonthDisplayed
            } else {
                binding.MainActivityYearMonthPickerBTN.text = "All"
            }
        })


    }

    fun initUI() {
        favoriteBTN = binding.MainActivityFavoriteBTN
        infoBTN = binding.MainActivityInfoBTN
        navView = binding.navView
        monthYearPickerBTN = binding.MainActivityYearMonthPickerBTN

        favoriteBTN.setOnClickListener{
            Log.d(Constants.LOGGING_TAG, "initUI: ")

            if(isFavorited){
            favoriteBTN.setImageResource(R.drawable.ic_baseline_favorite_24)
                isFavorited = false
            }else{
                favoriteBTN.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                isFavorited = true
            }
        }

        monthYearPickerBTN.setOnClickListener {
            if (viewModel.isConnectedToInternet()) {
                val dialog = YearMonthPickerDialog()
                dialog.show(supportFragmentManager, Constants.YEAR_MONTH_PICKER_LAUNCH_TAG)
            } else {
                //user is only allowed to view the collection of transactions cached in SQLite when offline
                Toast.makeText(applicationContext, string.offline, Toast.LENGTH_SHORT).show()
            }
        }

        infoBTN.setOnClickListener {
            val currentFragTag =
                this.supportFragmentManager.findFragmentById(id.nav_host_fragment_activity_main)?.childFragmentManager?.fragments?.get(
                    0
                )?.toString()
            if (currentFragTag?.contains("AboutFragment") == true) {
                Log.d(Constants.LOGGING_TAG, "about")
                AlertDialog.Builder(this)
                    .setTitle(string.note)
                    .setMessage(string.aboutFragmentInfo)
                    .setPositiveButton(string.OK, null)
                    .show()
            } else if (currentFragTag?.contains("HomeFragment") == true) {
                AlertDialog.Builder(this)
                    .setTitle(string.note)
                    .setMessage(string.homeFragmentInfo)
                    .setPositiveButton(string.OK, null)
                    .show()
            } else if (currentFragTag?.contains("AddFragment") == true) {
                AlertDialog.Builder(this)
                    .setTitle(string.note)
                    .setMessage(string.addFragmentInfo)
                    .setPositiveButton(string.OK, null)
                    .show()
            } else {
                Toast.makeText(this, string.unexpected_error, Toast.LENGTH_SHORT).show()
            }

            Log.d(Constants.LOGGING_TAG, "$currentFragTag")

        }

    }

    override fun onRestart() {
        super.onRestart()
        //once editActivity is closed, update the data by making a GET request
        viewModel.getTransactions()
    }


}
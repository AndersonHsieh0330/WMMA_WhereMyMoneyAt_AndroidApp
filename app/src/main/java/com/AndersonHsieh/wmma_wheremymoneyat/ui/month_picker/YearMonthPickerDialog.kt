package com.AndersonHsieh.wmma_wheremymoneyat.ui.month_picker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.AndersonHsieh.wmma_wheremymoneyat.databinding.YearMonthPickerBinding
import com.AndersonHsieh.wmma_wheremymoneyat.ui.main_activity.TransactionViewModel
import com.AndersonHsieh.wmma_wheremymoneyat.util.Constants

class YearMonthPickerDialog : DialogFragment() {

    private lateinit var binding: YearMonthPickerBinding
    val viewModel : TransactionViewModel by activityViewModels()

    //UI elements
    private lateinit var yearPicker:NumberPicker
    private lateinit var monthPicker:NumberPicker
    private lateinit var closeBTN:Button
    private lateinit var selectAllCheckbox:CheckBox




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.dialog?.setCanceledOnTouchOutside(false)
        binding = YearMonthPickerBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    private fun initUI(){

        yearPicker = binding.yearPicker
        monthPicker = binding.monthPicker
        selectAllCheckbox = binding.yearMonthPickerSelectAllCheckbox
        closeBTN = binding.yearMonthPickerClosebtn

        viewModel.getSelectedYearMonth()
        viewModel.getIsSelectedAll()
        viewModel.selectedYearMonth.observe(this, androidx.lifecycle.Observer {
            yearPicker.value = it[0]
            monthPicker.value = it[1]
        })

        viewModel.isSelectedAll.observe(this, androidx.lifecycle.Observer {
            selectAllCheckbox.isChecked = it
        })



        with(yearPicker){
            maxValue =2030
            minValue = 2021
        }

        with(monthPicker){
            maxValue =12
            minValue = 1
        }

        closeBTN.setOnClickListener(View.OnClickListener {
            viewModel.changeSelectedTimeInSharedPreference(yearPicker.value, monthPicker.value, selectAllCheckbox.isChecked)
            viewModel.getSelectedYearMonth()
            viewModel.getIsSelectedAll()
            viewModel.getTransactions()
            removeFragment()
        })
    }

    private fun removeFragment() {
        val currentFragManager = parentFragmentManager
        val currentFrag = currentFragManager.findFragmentByTag(Constants.YEAR_MONTH_PICKER_LAUNCH_TAG)
        if (currentFrag != null) {
            currentFragManager.beginTransaction().remove(currentFrag).commit()
        } else {
            Log.d(Constants.LOGGING_TAG, "removeFragment: currentFrag not found")
        }
    }

}
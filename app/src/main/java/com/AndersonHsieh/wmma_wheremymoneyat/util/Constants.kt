package com.AndersonHsieh.wmma_wheremymoneyat.util

class Constants {

    companion object {
        //logging tag
        const val LOGGING_TAG = "andy_debug"

        //Rest API related
        const val WMMA_API_BASEURI = "https://where-my-money-at.herokuapp.com/api/v1/transaction/"

        //SharedPreference related
        const val SHAREDPREFERENCES_Transaction_TAG = "transaction_properties"
        const val SP_YEAR = "year"
        const val SP_MONTH = "month"
        const val SP_SELECT_ALL = "selectedAll"

        //YearMonthPickerDialog related
        const val YEAR_MONTH_PICKER_LAUNCH_TAG = "year_month_picker_start"

        //Transaction model related
        const val TRANSACTION_ID = "transaction_id"
        const val TRANSACTION_NAME = "transaction_name"
        const val TRANSACTION_AMOUNT = "transaction_amount"
        const val TRANSACTION_TIME = "transaction_time"



    }
}
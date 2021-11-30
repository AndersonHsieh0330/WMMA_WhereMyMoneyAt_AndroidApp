package com.AndersonHsieh.wmma_wheremymoneyat.ui.edit_activity

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.AndersonHsieh.wmma_wheremymoneyat.R
import com.AndersonHsieh.wmma_wheremymoneyat.data.TransactionRepository
import com.AndersonHsieh.wmma_wheremymoneyat.databinding.ActivityEditBinding
import com.AndersonHsieh.wmma_wheremymoneyat.ui.add.AddViewModel
import com.AndersonHsieh.wmma_wheremymoneyat.util.Constants
import com.AndersonHsieh.wmma_wheremymoneyat.util.MyViewModelFactory
import com.google.android.material.card.MaterialCardView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private lateinit var viewModel:EditViewModel

    private lateinit var checkBTN: MaterialCardView
    private lateinit var nameInput: EditText
    private lateinit var amountInput: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        var id = -1L
        var name = "error"
        var amount = -1.0
        if(bundle!= null){
            id = bundle.getLong(Constants.TRANSACTION_ID)
            name = bundle.getString(Constants.TRANSACTION_NAME).toString()
            amount = bundle.getDouble(Constants.TRANSACTION_AMOUNT)
        }
        viewModel = ViewModelProvider(this, MyViewModelFactory(TransactionRepository.getInstance()))[EditViewModel::class.java]

        initUI(id, name, amount)
    }

    private fun initUI(id:Long, name:String, amount:Double) {
        checkBTN = binding.editTransactionCheckBTN
        nameInput = binding.editTransactionNameEdittext
        amountInput = binding.editTransactionAmountEdittext

        //set initial value of edittext to the original
        nameInput.setText(name)
        amountInput.setText(amount.toString())

        //set listeners
        binding.editTransactionCloseBTN.setOnClickListener {
            finish()
        }
        checkBTN.setOnClickListener {
            checkInputAndSendRequest(id)
        }
    }

    private fun checkInputAndSendRequest(id:Long){
        try {
            val currentNameInput = nameInput.text.toString()
            val currentAmountInput = amountInput.text.toString()
            if (currentNameInput != "" && currentAmountInput != "") {
                viewModel.postTransactions(id, currentNameInput, currentAmountInput.toDouble()).enqueue(object :
                    Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        nameInput.text.clear()
                        amountInput.text.clear()
                        Toast.makeText(applicationContext, R.string.success, Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(applicationContext, R.string.failed, Toast.LENGTH_SHORT).show()
                    }

                })

            } else {
                Toast.makeText(applicationContext, R.string.invalidInput, Toast.LENGTH_SHORT).show()
            }
        } catch (exception: NumberFormatException) {
            Log.d(
                Constants.LOGGING_TAG,
                "putTransaction: ${resources.getString(R.string.invalidInput)}"
            )
            Toast.makeText(applicationContext, R.string.invalidInput, Toast.LENGTH_SHORT).show()
        }

    }
}
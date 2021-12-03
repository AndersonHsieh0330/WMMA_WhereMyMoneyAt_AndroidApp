package com.AndersonHsieh.wmma_wheremymoneyat.ui.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.AndersonHsieh.wmma_wheremymoneyat.R
import com.AndersonHsieh.wmma_wheremymoneyat.data.TransactionRepository
import com.AndersonHsieh.wmma_wheremymoneyat.databinding.FragmentAddBinding
import com.AndersonHsieh.wmma_wheremymoneyat.util.Constants
import com.AndersonHsieh.wmma_wheremymoneyat.util.MyViewModelFactory
import com.google.android.material.card.MaterialCardView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFragment : Fragment() {

    private lateinit var viewModel: AddViewModel
    private var _binding: FragmentAddBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var addBTN: MaterialCardView
    private lateinit var nameInput: EditText
    private lateinit var amountInput: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(TransactionRepository.getInstance())
        )[AddViewModel::class.java]
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI();
    }

    private fun initUI() {
        addBTN = binding.addTransactionAddBTN
        nameInput = binding.addTransactionNameEdittext
        amountInput = binding.addTransactionAmountEdittext

        addBTN.setOnClickListener {
            checkInputAndSendRequest()
        }

    }

    private fun checkInputAndSendRequest() {
        if (viewModel.isConnectedToInternet(activity?.applicationContext!!)) {
            try {
                val currentNameInput = nameInput.text.toString()
                val currentAmountInput = amountInput.text.toString()
                if (currentNameInput != "" && currentAmountInput != "") {
                    viewModel.putTransactions(currentNameInput, currentAmountInput.toDouble())
                        .enqueue(object :
                            Callback<ResponseBody> {
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {
                                nameInput.text.clear()
                                amountInput.text.clear()
                                Toast.makeText(context, R.string.success, Toast.LENGTH_SHORT).show()
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show()
                            }

                        })

                } else {
                    Toast.makeText(this.context, R.string.invalidInput, Toast.LENGTH_SHORT).show()
                }
            } catch (exception: NumberFormatException) {
                Log.d(
                    Constants.LOGGING_TAG,
                    "putTransaction: ${resources.getString(R.string.invalidInput)}"
                )
                Toast.makeText(this.context, R.string.invalidInput, Toast.LENGTH_SHORT).show()
            }
        } else {
            //user is only allowed to view the collection of transactions cached in SQLite when offline
            //not allowed to add a new transaction
            Toast.makeText(activity?.applicationContext, R.string.offline, Toast.LENGTH_SHORT)
                .show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
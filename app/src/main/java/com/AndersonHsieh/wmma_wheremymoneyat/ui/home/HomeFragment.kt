package com.AndersonHsieh.wmma_wheremymoneyat.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.AndersonHsieh.wmma_wheremymoneyat.databinding.FragmentHomeBinding
import com.AndersonHsieh.wmma_wheremymoneyat.model.Transaction
import com.AndersonHsieh.wmma_wheremymoneyat.ui.main_activity.TransactionViewModel
import com.AndersonHsieh.wmma_wheremymoneyat.util.Constants
import com.AndersonHsieh.wmma_wheremymoneyat.util.RecyclerAdapter
import java.time.LocalDateTime

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var dataSet:MutableList<Transaction>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val viewModel :TransactionViewModel by activityViewModels()

        //update the actual data in view model
        viewModel.getTransactions()

        //register observer of HomeViewModel, which belongs to HomeFragment
        viewModel.transactions.observe(viewLifecycleOwner, Observer {

            if(it!=null){
               dataSet = it
            }else{
                dataSet = mutableListOf(Transaction(-1, "failed", 0.0, LocalDateTime.now().toString()))
            }

            //for testing
            initUI(viewModel);


        })
    }

    private fun initUI(viewModel: TransactionViewModel){
        val layoutManager = LinearLayoutManager(this.context)
        val adapter = RecyclerAdapter(dataSet,viewModel)
        val transactionRecyclerView = binding.homeTransactionRecyclerview
        transactionRecyclerView.adapter = adapter
        transactionRecyclerView.layoutManager = layoutManager

    }    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
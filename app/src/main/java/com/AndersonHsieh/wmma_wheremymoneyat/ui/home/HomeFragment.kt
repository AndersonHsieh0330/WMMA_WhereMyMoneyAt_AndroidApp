package com.AndersonHsieh.wmma_wheremymoneyat.ui.home

import android.os.Bundle
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
import com.AndersonHsieh.wmma_wheremymoneyat.util.RecyclerAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var dataSet:List<Transaction>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    interface onSelectedDate

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
            dataSet = it}else{
                dataSet = listOf(Transaction(0,"failed",0.0,"2011"))
            }

            //for testing
            initUI();


        })






    }

    private fun initUI(){
        val layoutManager = LinearLayoutManager(this.context)
        val adapter = RecyclerAdapter(dataSet)
        val transactionRecyclerView = binding.homeTransactionRecyclerview
        transactionRecyclerView.adapter = adapter
        transactionRecyclerView.layoutManager = layoutManager

    }    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}